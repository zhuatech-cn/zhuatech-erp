/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class ManualJournalPostingGovernanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (!request.periodOpen()) blockers.add("总账期间已关闭或未开放");
        if (!request.debitCreditBalanced()) blockers.add("凭证借贷金额不平衡");
        if (!request.accountsActive()) blockers.add("凭证包含停用或不允许手工记账的科目");
        if (!request.dimensionsComplete()) blockers.add("成本中心、项目或组织核算维度不完整");
        if (!request.sourceEvidenceReady()) blockers.add("手工凭证来源及附件证据不完整");
        if (!request.duplicateCheckClear()) blockers.add("凭证摘要、金额或来源单号存在重复风险");
        if (!request.intercompanyBalanced()) blockers.add("内部往来双方未配平");
        if (!request.taxReviewed()) blockers.add("税码与纳税影响未完成复核");
        if (!request.highValueApprovalReady()) blockers.add("大额或非常规凭证缺少分级审批");
        if (!request.financeApproved()) blockers.add("财务负责人尚未批准过账");
        if (!request.preparerApproverSeparated()) blockers.add("制证人与审批人未职责分离");
        if (!request.currencyRatesLocked()) blockers.add("外币凭证汇率版本未锁定");
        if (!request.auditReady()) blockers.add("编制、复核、审批和附件证据链不完整");
        if (!request.reversalScheduleReady()) actions.add("为暂估或预提凭证配置自动冲回计划");
        if (!request.postingNoticeReady()) actions.add("准备关联系统及责任人过账通知");
        if (!request.archivePackageReady()) actions.add("生成凭证归档包和检索索引");
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED : !actions.isEmpty() ? Decision.REVIEW : Decision.POST;
        return new Assessment(request.journalNo(), request.batchNo(), request.lineCount(),
                decision, List.copyOf(blockers), List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String journalNo, @NotBlank String batchNo, @Min(2) int lineCount,
                          boolean periodOpen, boolean debitCreditBalanced, boolean accountsActive,
                          boolean dimensionsComplete, boolean sourceEvidenceReady, boolean duplicateCheckClear,
                          boolean intercompanyBalanced, boolean taxReviewed, boolean highValueApprovalReady,
                          boolean financeApproved, boolean preparerApproverSeparated, boolean currencyRatesLocked,
                          boolean auditReady, boolean reversalScheduleReady, boolean postingNoticeReady,
                          boolean archivePackageReady) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Assessment(String journalNo, String batchNo, int lineCount, Decision decision,
                             List<String> blockers, List<String> actions) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { POST, REVIEW, BLOCKED }
}
