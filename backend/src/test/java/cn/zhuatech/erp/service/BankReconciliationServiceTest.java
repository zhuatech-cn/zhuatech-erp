/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class BankReconciliationServiceTest {
    private final BankReconciliationService service = new BankReconciliationService();
    private final LocalDate date = LocalDate.of(2026, 9, 24);

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void reconcilesByReferenceAndUniqueAmountDate() {
        var result = service.reconcile(new BankReconciliationService.Request("REC-1", "1002", "CNY",
                amount("1200"), amount("1200"), amount("0.01"), 2,
                List.of(ledger("L1", "1000", "PAY-1"), ledger("L2", "200", null)),
                List.of(bank("B1", "1000", "PAY-1", 1, false), bank("B2", "200", null, 0, false))));
        assertThat(result.decision()).isEqualTo(BankReconciliationService.Decision.RECONCILED);
        assertThat(result.matches()).extracting(BankReconciliationService.Match::matchType)
                .containsExactly(BankReconciliationService.MatchType.REFERENCE,
                        BankReconciliationService.MatchType.AMOUNT_DATE);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void sendsAmbiguousCandidatesToReview() {
        var result = service.reconcile(new BankReconciliationService.Request("REC-2", "1002", "CNY",
                amount("100"), amount("200"), amount("0.01"), 2,
                List.of(ledger("L1", "100", null)),
                List.of(bank("B1", "100", null, 0, false), bank("B2", "100", null, 1, false))));
        assertThat(result.decision()).isEqualTo(BankReconciliationService.Decision.REVIEW);
        assertThat(result.exceptions()).anyMatch(item -> item.contains("多个候选"));
        assertThat(result.unmatchedBankEntries()).containsExactly("B1", "B2");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void blocksDuplicateAndUnpostedInput() {
        var unposted = new BankReconciliationService.LedgerEntry("L1", amount("100"), "CNY", date, "R", false);
        var result = service.reconcile(new BankReconciliationService.Request("REC-3", "1002", "CNY",
                amount("100"), amount("100"), amount("0.01"), 1,
                List.of(unposted, unposted), List.of(bank("B1", "100", "R", 0, false))));
        assertThat(result.decision()).isEqualTo(BankReconciliationService.Decision.BLOCKED);
        assertThat(result.blockers()).anyMatch(item -> item.contains("编号重复"));
        assertThat(result.blockers()).anyMatch(item -> item.contains("尚未过账"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BankReconciliationService.LedgerEntry ledger(String id, String value, String reference) {
        return new BankReconciliationService.LedgerEntry(id, amount(value), "CNY", date, reference, true);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BankReconciliationService.BankEntry bank(String id, String value, String reference,
                                                       int dayOffset, boolean duplicate) {
        return new BankReconciliationService.BankEntry(id, amount(value), "CNY",
                date.plusDays(dayOffset), reference, duplicate);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BigDecimal amount(String value) { return new BigDecimal(value); }
}
