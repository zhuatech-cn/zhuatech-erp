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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 对银行流水和总账银行科目执行确定性一对一匹配并识别未达账项。
 *
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class BankReconciliationService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result reconcile(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> exceptions = new ArrayList<>();
        if (!uniqueIds(request.ledgerEntries().stream().map(LedgerEntry::entryId).toList())) {
            blockers.add("总账分录编号重复");
        }
        if (!uniqueIds(request.bankEntries().stream().map(BankEntry::entryId).toList())) {
            blockers.add("银行流水编号重复");
        }
        request.ledgerEntries().stream().filter(entry -> !entry.posted())
                .forEach(entry -> blockers.add(entry.entryId() + " 尚未过账"));
        request.bankEntries().stream().filter(BankEntry::duplicateFlag)
                .forEach(entry -> blockers.add(entry.entryId() + " 被标记为重复银行流水"));
        request.ledgerEntries().stream().filter(entry -> !request.currency().equals(entry.currency()))
                .forEach(entry -> blockers.add(entry.entryId() + " 总账币种不一致"));
        request.bankEntries().stream().filter(entry -> !request.currency().equals(entry.currency()))
                .forEach(entry -> blockers.add(entry.entryId() + " 银行流水币种不一致"));

        Set<String> usedBankIds = new HashSet<>();
        List<Match> matches = new ArrayList<>();
        List<LedgerEntry> ledgers = request.ledgerEntries().stream()
                .sorted(Comparator.comparing(LedgerEntry::businessDate).thenComparing(LedgerEntry::entryId)).toList();
        for (LedgerEntry ledger : ledgers) {
            if (!ledger.posted() || !request.currency().equals(ledger.currency())) continue;
            List<BankEntry> eligible = request.bankEntries().stream()
                    .filter(bank -> !usedBankIds.contains(bank.entryId()))
                    .filter(bank -> !bank.duplicateFlag() && request.currency().equals(bank.currency()))
                    .filter(bank -> ledger.amount().subtract(bank.amount()).abs().compareTo(request.amountTolerance()) <= 0)
                    .filter(bank -> Math.abs(ChronoUnit.DAYS.between(ledger.businessDate(), bank.businessDate()))
                            <= request.dateToleranceDays()).toList();
            List<BankEntry> referenceMatches = eligible.stream()
                    .filter(bank -> hasText(ledger.reference()) && ledger.reference().equals(bank.reference())).toList();
            List<BankEntry> candidates = referenceMatches.isEmpty() ? eligible : referenceMatches;
            if (candidates.size() == 1) {
                BankEntry bank = candidates.getFirst();
                usedBankIds.add(bank.entryId());
                MatchType type = referenceMatches.isEmpty() ? MatchType.AMOUNT_DATE : MatchType.REFERENCE;
                matches.add(new Match(ledger.entryId(), bank.entryId(), ledger.amount(), type,
                        Math.abs(ChronoUnit.DAYS.between(ledger.businessDate(), bank.businessDate()))));
            } else if (candidates.size() > 1) {
                exceptions.add(ledger.entryId() + " 存在多个候选银行流水，需人工确认");
            }
        }

        Set<String> matchedLedgerIds = matches.stream().map(Match::ledgerEntryId).collect(java.util.stream.Collectors.toSet());
        List<String> unmatchedLedgers = ledgers.stream().map(LedgerEntry::entryId)
                .filter(id -> !matchedLedgerIds.contains(id)).toList();
        List<String> unmatchedBanks = request.bankEntries().stream().map(BankEntry::entryId)
                .filter(id -> !usedBankIds.contains(id)).sorted().toList();
        BigDecimal balanceDifference = request.statementClosingBalance().subtract(request.ledgerClosingBalance());
        if (balanceDifference.abs().compareTo(request.amountTolerance()) > 0) {
            exceptions.add("银行对账单与总账期末余额不一致");
        }
        Decision decision = !blockers.isEmpty() ? Decision.BLOCKED
                : unmatchedLedgers.isEmpty() && unmatchedBanks.isEmpty() && exceptions.isEmpty()
                ? Decision.RECONCILED : Decision.REVIEW;
        return new Result(request.reconciliationNo(), decision, balanceDifference,
                List.copyOf(matches), List.copyOf(unmatchedLedgers), List.copyOf(unmatchedBanks),
                List.copyOf(blockers), List.copyOf(exceptions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private boolean uniqueIds(List<String> ids) { return new HashSet<>(ids).size() == ids.size(); }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private boolean hasText(String value) { return value != null && !value.isBlank(); }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String reconciliationNo, @NotBlank String accountNo,
                          @NotBlank String currency, @NotNull BigDecimal ledgerClosingBalance,
                          @NotNull BigDecimal statementClosingBalance,
                          @NotNull @DecimalMin("0.00") BigDecimal amountTolerance,
                          @Min(0) int dateToleranceDays,
                          @NotEmpty List<@Valid LedgerEntry> ledgerEntries,
                          @NotEmpty List<@Valid BankEntry> bankEntries) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record LedgerEntry(@NotBlank String entryId, @NotNull BigDecimal amount,
                              @NotBlank String currency, @NotNull LocalDate businessDate,
                              String reference, boolean posted) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record BankEntry(@NotBlank String entryId, @NotNull BigDecimal amount,
                            @NotBlank String currency, @NotNull LocalDate businessDate,
                            String reference, boolean duplicateFlag) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Match(String ledgerEntryId, String bankEntryId, BigDecimal amount,
                        MatchType matchType, long dateDifferenceDays) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(String reconciliationNo, Decision decision, BigDecimal balanceDifference,
                         List<Match> matches, List<String> unmatchedLedgerEntries,
                         List<String> unmatchedBankEntries, List<String> blockers,
                         List<String> exceptions) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum MatchType { REFERENCE, AMOUNT_DATE }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { RECONCILED, REVIEW, BLOCKED }
}
