/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/** 采购订单、收货和供应商发票的确定性三单匹配与付款冻结决策。 */
@Service
public class ProcureToPayMatchService {
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> variances = new ArrayList<>();
        if (!request.vendorMatched()) blockers.add("发票供应商与采购订单供应商不一致");
        if (!request.currencyMatched()) blockers.add("发票币种与采购订单币种不一致");
        if (!request.taxValidated()) blockers.add("税率、税额或发票合法性校验未通过");
        if (request.duplicateInvoice()) blockers.add("检测到重复发票号码或重复金额组合");
        if (!request.receiptPosted()) blockers.add("收货记录尚未过账");
        if (request.invoiceQuantity().compareTo(request.orderedQuantity()) > 0) blockers.add("发票数量超过采购订单数量");
        if (request.invoiceQuantity().compareTo(request.receivedQuantity()) > 0) blockers.add("发票数量超过已收货数量");

        BigDecimal priceVariance = percentDifference(request.invoiceUnitPrice(), request.poUnitPrice());
        BigDecimal expectedTotal = request.invoiceQuantity().multiply(request.poUnitPrice()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalVariance = percentDifference(request.invoiceTotal(), expectedTotal);
        if (priceVariance.compareTo(request.tolerancePercent()) > 0) variances.add("发票单价偏差超过容差");
        if (totalVariance.compareTo(request.tolerancePercent()) > 0) variances.add("发票总额偏差超过容差");

        Decision decision = !blockers.isEmpty() ? Decision.HOLD
                : !variances.isEmpty() ? Decision.REVIEW : Decision.MATCH;
        return new Assessment(request.invoiceNo(), decision, expectedTotal, priceVariance,
                totalVariance, decision != Decision.MATCH, List.copyOf(blockers), List.copyOf(variances));
    }

    private BigDecimal percentDifference(BigDecimal actual, BigDecimal expected) {
        return actual.subtract(expected).abs().multiply(HUNDRED)
                .divide(expected, 4, RoundingMode.HALF_UP);
    }

    public record Request(@NotBlank String invoiceNo, @NotBlank String purchaseOrderNo,
                          @NotBlank String receiptNo,
                          @NotNull @DecimalMin("0.0001") BigDecimal orderedQuantity,
                          @NotNull @DecimalMin("0.0001") BigDecimal receivedQuantity,
                          @NotNull @DecimalMin("0.0001") BigDecimal invoiceQuantity,
                          @NotNull @DecimalMin("0.0001") BigDecimal poUnitPrice,
                          @NotNull @DecimalMin("0.0001") BigDecimal invoiceUnitPrice,
                          @NotNull @DecimalMin("0.01") BigDecimal invoiceTotal,
                          @NotNull @DecimalMin("0.0") BigDecimal tolerancePercent,
                          boolean vendorMatched, boolean currencyMatched, boolean taxValidated,
                          boolean duplicateInvoice, boolean receiptPosted) {}

    public record Assessment(String invoiceNo, Decision decision, BigDecimal expectedTotal,
                             BigDecimal unitPriceVariancePercent, BigDecimal totalVariancePercent,
                             boolean paymentHold, List<String> blockers, List<String> variances) {}

    public enum Decision { MATCH, REVIEW, HOLD }
}
