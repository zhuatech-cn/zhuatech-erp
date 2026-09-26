/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 库存估值关账前完成子账总账勾稽、负库存、暂估和成本版本检查。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class InventoryValuationCloseService {
    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        BigDecimal variance = request.inventorySubledgerTotal().subtract(request.generalLedgerTotal()).abs();
        if (!request.periodOpen()) blockers.add("目标会计期间已关闭");
        if (!request.requesterAuthorized()) blockers.add("申请人无库存估值关账权限");
        if (request.negativeStockItems() > 0) blockers.add("存在负库存物料，必须先调整数量与成本层");
        if (request.uncostedReceipts() > 0) blockers.add("存在未计价入库或暂估单据");
        if (request.pendingCostAdjustments() > 0) blockers.add("存在待过账成本调整");
        if (variance.compareTo(request.reconciliationTolerance()) > 0) blockers.add("库存子账与总账差异超过关账容差");
        if (!request.exchangeRatesLocked()) actions.add("锁定多币种重估汇率");
        if (!request.standardCostVersionReleased()) actions.add("发布下一期标准成本版本");
        if (!request.inventoryMovementFrozen()) actions.add("执行关账时段库存移动冻结");
        if (!request.auditEvidenceAttached()) actions.add("归档成本运行日志、勾稽表和差异说明");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED
                : !actions.isEmpty() ? Decision.REVIEW : Decision.CLOSE;
        return new Assessment(request.closeNo(), request.period(), decision, variance,
                request.reconciliationTolerance(), List.copyOf(blockers), List.copyOf(actions));
    }

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public record Request(@NotBlank String closeNo, @NotBlank String period,
                          @NotNull @DecimalMin("0.00") BigDecimal inventorySubledgerTotal,
                          @NotNull @DecimalMin("0.00") BigDecimal generalLedgerTotal,
                          @NotNull @DecimalMin("0.00") BigDecimal reconciliationTolerance,
                          @Min(0) int negativeStockItems, @Min(0) int uncostedReceipts,
                          @Min(0) int pendingCostAdjustments, boolean periodOpen,
                          boolean requesterAuthorized, boolean exchangeRatesLocked,
                          boolean standardCostVersionReleased, boolean inventoryMovementFrozen,
                          boolean auditEvidenceAttached) {}

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public record Assessment(String closeNo, String period, Decision decision,
                             BigDecimal reconciliationVariance, BigDecimal tolerance,
                             List<String> blockers, List<String> actions) {}

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public enum Decision { CLOSE, REVIEW, BLOCKED }
}
