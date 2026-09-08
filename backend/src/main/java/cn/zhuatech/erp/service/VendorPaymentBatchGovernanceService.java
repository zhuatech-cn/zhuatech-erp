/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
@Service
public class VendorPaymentBatchGovernanceService {
    private static final BigDecimal HIGH_VALUE = new BigDecimal("500000");
    public Assessment assess(Request r) {
        List<String> blockers = new ArrayList<>(); List<String> actions = new ArrayList<>();
        if (!r.threeWayMatchPassed()) blockers.add("发票、采购订单与收货记录三单匹配未通过");
        if (!r.duplicateInvoiceChecked()) blockers.add("尚未完成重复发票检查");
        if (!r.vendorBankVerified()) blockers.add("供应商收款账户未完成独立验证");
        if (!r.sanctionsScreeningPassed()) blockers.add("供应商合规筛查未通过");
        if (!r.accountingPeriodOpen()) blockers.add("付款所属会计期间已关闭");
        if (!r.cashPlanReserved()) blockers.add("资金计划尚未预留额度");
        if (!r.approvalMatrixPassed()) blockers.add("付款批次未满足金额分级审批矩阵");
        if (r.preparerId().equals(r.approverId())) blockers.add("制单人与审批人必须职责分离");
        if (!r.paymentFileSigned()) blockers.add("支付文件未完成签名或防篡改校验");
        if (!r.idempotencyKeyRegistered()) blockers.add("付款批次缺少幂等键，存在重复支付风险");
        if (!r.auditEvidenceAttached()) actions.add("补充付款依据与审批证据包");
        if (!r.remittanceReconciliationPlanned()) actions.add("配置支付回执与总账自动对账任务");
        RiskLevel risk = r.totalAmount().compareTo(HIGH_VALUE) >= 0 || r.urgentPayment() ? RiskLevel.HIGH : RiskLevel.NORMAL;
        if (risk == RiskLevel.HIGH && !r.treasuryReviewed()) blockers.add("大额或紧急付款必须经资金负责人复核");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED : !actions.isEmpty() ? Decision.REVIEW : Decision.RELEASE;
        String route = risk == RiskLevel.HIGH ? "应付会计→财务经理→资金负责人" : "应付会计→财务经理";
        return new Assessment(r.batchNo(), decision, risk, route, List.copyOf(blockers), List.copyOf(actions));
    }
    public record Request(@NotBlank String batchNo, @NotBlank String preparerId, @NotBlank String approverId,
                          @NotNull @DecimalMin("0.01") BigDecimal totalAmount, boolean threeWayMatchPassed,
                          boolean duplicateInvoiceChecked, boolean vendorBankVerified, boolean sanctionsScreeningPassed,
                          boolean accountingPeriodOpen, boolean cashPlanReserved, boolean approvalMatrixPassed,
                          boolean paymentFileSigned, boolean idempotencyKeyRegistered, boolean auditEvidenceAttached,
                          boolean remittanceReconciliationPlanned, boolean urgentPayment, boolean treasuryReviewed) {}
    public record Assessment(String batchNo, Decision decision, RiskLevel riskLevel, String approvalRoute,
                             List<String> blockers, List<String> actions) {}
    public enum Decision { RELEASE, REVIEW, BLOCKED } public enum RiskLevel { NORMAL, HIGH }
}
