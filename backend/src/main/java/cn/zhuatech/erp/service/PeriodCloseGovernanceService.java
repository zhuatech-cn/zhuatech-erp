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
public class PeriodCloseGovernanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        int coverage = request.subledgerCount() == 0 ? 0
                : Math.min(100, request.reconciledSubledgers() * 100 / request.subledgerCount());
        if (request.subledgerCount() == 0) blockers.add("未登记需要关账的子账簿");
        else if (request.reconciledSubledgers() < request.subledgerCount()) blockers.add("子账簿尚未全部对账");
        if (request.unpostedJournalCount() > 0) blockers.add("存在未过账凭证");
        if (!request.intercompanyMatched()) blockers.add("内部往来未完成抵销匹配");
        if (!request.bankReconciled()) blockers.add("银行账户未完成对账");
        if (!request.taxReviewed()) actions.add("完成税务口径复核");
        if (!request.closeOwnerSignedOff()) actions.add("取得关账负责人签署");

        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED
                : !actions.isEmpty() ? Decision.REVIEW : Decision.CLOSE;
        return new Assessment(request.period(), coverage, decision,
                List.copyOf(blockers), List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String period, @Min(0) int subledgerCount,
                          @Min(0) int reconciledSubledgers, @Min(0) int unpostedJournalCount,
                          boolean intercompanyMatched, boolean bankReconciled,
                          boolean taxReviewed, boolean closeOwnerSignedOff) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Assessment(String period, int reconciliationCoverage, Decision decision,
                             List<String> blockers, List<String> actions) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { CLOSE, REVIEW, BLOCKED }
}
