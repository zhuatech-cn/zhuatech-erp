/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import cn.zhuatech.erp.ai.OpenAiCompatibleGateway;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class AiInvoiceAnomalyService {
    private final OpenAiCompatibleGateway gateway;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public AiInvoiceAnomalyService(OpenAiCompatibleGateway gateway) { this.gateway = gateway; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result inspect(Request request) {
        int score = 0;
        List<String> findings = new ArrayList<>();
        BigDecimal ratio = request.vendorAverageAmount().signum() == 0 ? BigDecimal.ONE
            : request.amount().divide(request.vendorAverageAmount(), 2, RoundingMode.HALF_UP);
        if (ratio.compareTo(BigDecimal.valueOf(2)) > 0) { score += 25; findings.add("金额显著高于供应商历史均值"); }
        if (Boolean.TRUE.equals(request.duplicateReference())) { score += 40; findings.add("疑似重复发票或重复业务引用"); }
        if (Boolean.TRUE.equals(request.bankAccountChanged())) { score += 30; findings.add("收款账户近期发生变更"); }
        if (!Boolean.TRUE.equals(request.purchaseOrderMatched())) { score += 20; findings.add("未匹配采购订单"); }
        if (!Boolean.TRUE.equals(request.receiptMatched())) { score += 20; findings.add("未匹配收货或验收记录"); }
        if (request.taxRateDeviation().abs().compareTo(BigDecimal.valueOf(2)) > 0) { score += 15; findings.add("税率偏差超过 2 个百分点"); }
        if (request.vendorAgeDays() < 30) { score += 10; findings.add("供应商建档时间较短"); }
        score = Math.min(100, score);
        if (findings.isEmpty()) findings.add("三单匹配和历史金额检查未发现明显异常");

        String context = "发票=%s，金额=%s，历史均值=%s，风险分=%d，发现=%s"
            .formatted(request.invoiceNumber(), request.amount(), request.vendorAverageAmount(), score, findings);
        var enhanced = gateway.complete("你是企业应付审计助手，请解释发票异常并列出复核证据。", context);
        var metadata = gateway.metadata();
        return new Result(score, score >= 70 ? "BLOCK" : score >= 35 ? "REVIEW" : "PASS", ratio,
            enhanced.orElse("风险分 %d：%s".formatted(score, String.join("；", findings))), List.copyOf(findings),
            enhanced.isPresent() ? "EXTERNAL_MODEL" : "LOCAL_RULES", metadata.provider(), metadata.model());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String invoiceNumber, @DecimalMin("0.01") BigDecimal amount,
                          @DecimalMin("0") BigDecimal vendorAverageAmount, @NotNull Boolean duplicateReference,
                          @NotNull Boolean bankAccountChanged, @NotNull Boolean purchaseOrderMatched,
                          @NotNull Boolean receiptMatched, @NotNull BigDecimal taxRateDeviation,
                          @Min(0) int vendorAgeDays) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(int anomalyScore, String decision, BigDecimal amountRatio, String explanation,
                         List<String> findings, String aiMode, String provider, String model) {}
}
