/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class MaterialPlanningService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result plan(Request request) {
        List<ItemPlan> items = request.items().stream()
            .map(this::planItem)
            .sorted(Comparator.comparingInt((ItemPlan item) -> riskRank(item.riskLevel())).reversed()
                .thenComparing(ItemPlan::suggestedOrder, Comparator.reverseOrder()))
            .toList();
        BigDecimal capitalRequired = items.stream()
            .map(item -> item.suggestedOrder().multiply(item.unitCost()))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
        long shortageItems = items.stream().filter(item -> !"LOW".equals(item.riskLevel())).count();
        return new Result(items, shortageItems, capitalRequired,
            shortageItems == 0 ? "库存结构健康，按周复核需求预测" : "优先下单高风险物料，并同步校验供应商承诺交期");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ItemPlan planItem(ItemInput item) {
        BigDecimal projectedStock = item.onHand().add(item.inboundQty()).subtract(item.forecastDemand());
        BigDecimal suggestedOrder = item.safetyStock().add(item.forecastDemand())
            .subtract(item.onHand()).subtract(item.inboundQty()).max(BigDecimal.ZERO);
        String riskLevel = projectedStock.signum() < 0 || (item.leadDays() >= 14 && projectedStock.compareTo(item.safetyStock()) < 0)
            ? "HIGH" : projectedStock.compareTo(item.safetyStock()) < 0 ? "MEDIUM" : "LOW";
        String action = switch (riskLevel) {
            case "HIGH" -> "立即锁定供应商交期并创建补货单";
            case "MEDIUM" -> "纳入本周补货评审并跟踪在途数量";
            default -> "维持当前补货节奏";
        };
        return new ItemPlan(item.sku(), item.name(), projectedStock, suggestedOrder,
            item.leadDays(), item.unitCost(), riskLevel, action);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private int riskRank(String riskLevel) {
        return switch (riskLevel) { case "HIGH" -> 3; case "MEDIUM" -> 2; default -> 1; };
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ItemInput(@NotBlank String sku,
                            @NotBlank String name,
                            @NotNull @DecimalMin("0") BigDecimal onHand,
                            @NotNull @DecimalMin("0") BigDecimal safetyStock,
                            @NotNull @DecimalMin("0") BigDecimal forecastDemand,
                            @NotNull @DecimalMin("0") BigDecimal inboundQty,
                            @Min(0) int leadDays,
                            @NotNull @DecimalMin("0") BigDecimal unitCost) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotEmpty List<@Valid ItemInput> items) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ItemPlan(String sku, String name, BigDecimal projectedStock,
                           BigDecimal suggestedOrder, int leadDays, BigDecimal unitCost,
                           String riskLevel, String action) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(List<ItemPlan> items, long shortageItems, BigDecimal capitalRequired,
                         String recommendation) {}
}
