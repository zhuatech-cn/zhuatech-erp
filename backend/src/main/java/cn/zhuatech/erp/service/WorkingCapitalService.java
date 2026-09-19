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
public class WorkingCapitalService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result analyze(Request request) {
        BigDecimal days = BigDecimal.valueOf(365);
        BigDecimal dso = request.receivables().multiply(days).divide(request.annualRevenue(), 1, RoundingMode.HALF_UP);
        BigDecimal dpo = request.payables().multiply(days).divide(request.annualCostOfSales(), 1, RoundingMode.HALF_UP);
        BigDecimal inventoryDays = request.inventory().multiply(days).divide(request.annualCostOfSales(), 1, RoundingMode.HALF_UP);
        BigDecimal cashConversionCycle = dso.add(inventoryDays).subtract(dpo);
        String riskLevel = cashConversionCycle.compareTo(new BigDecimal("90")) > 0 ? "HIGH"
            : cashConversionCycle.compareTo(new BigDecimal("45")) > 0 ? "MEDIUM" : "LOW";

        List<String> actions = new ArrayList<>();
        if (dso.compareTo(new BigDecimal("60")) > 0) actions.add("按账龄分层催收并缩短高风险客户账期");
        if (inventoryDays.compareTo(new BigDecimal("75")) > 0) actions.add("处置慢动库存并校准补货参数");
        if (dpo.compareTo(new BigDecimal("30")) < 0) actions.add("在不影响供应关系的前提下优化付款节奏");
        if (actions.isEmpty()) actions.add("维持当前营运资金策略并按月滚动复盘");
        return new Result(request.entityCode(), dso, dpo, inventoryDays, cashConversionCycle, riskLevel, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String entityCode,
                          @DecimalMin(value = "0.01") BigDecimal annualRevenue,
                          @DecimalMin(value = "0.01") BigDecimal annualCostOfSales,
                          @DecimalMin("0") BigDecimal receivables,
                          @DecimalMin("0") BigDecimal payables,
                          @DecimalMin("0") BigDecimal inventory) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String entityCode, BigDecimal daysSalesOutstanding, BigDecimal daysPayablesOutstanding,
                         BigDecimal inventoryDays, BigDecimal cashConversionCycleDays,
                         String riskLevel, List<String> actions) {}
}
