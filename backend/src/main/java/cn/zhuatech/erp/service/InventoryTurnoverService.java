/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class InventoryTurnoverService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result analyze(Request request) {
        BigDecimal turnover = request.annualCostOfSales().divide(request.averageInventoryValue(), 2, RoundingMode.HALF_UP);
        BigDecimal daysOnHand = turnover.signum() == 0 ? new BigDecimal("999.00")
            : new BigDecimal("365").divide(turnover, 2, RoundingMode.HALF_UP);
        BigDecimal dailyCost = request.annualCostOfSales().divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP);
        BigDecimal stockCoverageDays = dailyCost.signum() == 0 ? new BigDecimal("999.00")
            : request.currentInventoryValue().divide(dailyCost, 2, RoundingMode.HALF_UP);

        String status;
        if (request.currentInventoryValue().compareTo(request.safetyInventoryValue()) < 0) status = "SHORTAGE_RISK";
        else if (stockCoverageDays.compareTo(new BigDecimal("120")) > 0 || turnover.compareTo(new BigDecimal("2")) < 0) status = "EXCESS";
        else if (turnover.compareTo(new BigDecimal("4")) < 0) status = "SLOW";
        else status = "HEALTHY";

        List<String> actions = new ArrayList<>();
        if ("SHORTAGE_RISK".equals(status)) actions.add("校验在途采购并优先补足安全库存");
        if ("EXCESS".equals(status)) actions.add("暂停非刚性采购并制定去库存计划");
        if ("SLOW".equals(status)) actions.add("复核需求预测和最小采购批量");
        if ("HEALTHY".equals(status)) actions.add("维持补货参数并持续监控周转趋势");
        return new Result(request.skuCode(), turnover, daysOnHand, stockCoverageDays, status, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String skuCode,
                          @DecimalMin("0.01") BigDecimal annualCostOfSales,
                          @DecimalMin("0.01") BigDecimal averageInventoryValue,
                          @DecimalMin("0") BigDecimal currentInventoryValue,
                          @DecimalMin("0") BigDecimal safetyInventoryValue) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String skuCode, BigDecimal turnoverTimes, BigDecimal daysOnHand,
                         BigDecimal stockCoverageDays, String status, List<String> actions) {}
}
