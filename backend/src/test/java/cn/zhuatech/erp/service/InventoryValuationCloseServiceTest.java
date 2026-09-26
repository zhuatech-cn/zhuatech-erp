/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class InventoryValuationCloseServiceTest {
    private final InventoryValuationCloseService service = new InventoryValuationCloseService();

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    @Test
    void closesReconciledPreparedInventoryPeriod() {
        var result = service.assess(request("1000000", "999999.50", 0, 0, 0, true));
        assertThat(result.decision()).isEqualTo(InventoryValuationCloseService.Decision.CLOSE);
        assertThat(result.reconciliationVariance()).isEqualByComparingTo("0.50");
    }

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    @Test
    void blocksCloseWithNegativeStockAndLedgerVariance() {
        var result = service.assess(request("1000000", "990000", 3, 0, 0, true));
        assertThat(result.decision()).isEqualTo(InventoryValuationCloseService.Decision.BLOCKED);
        assertThat(result.blockers()).anyMatch(item -> item.contains("负库存"));
        assertThat(result.blockers()).anyMatch(item -> item.contains("容差"));
    }

    /** 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    @Test
    void reviewsCloseMissingOperationalEvidence() {
        var result = service.assess(request("1000000", "1000000", 0, 0, 0, false));
        assertThat(result.decision()).isEqualTo(InventoryValuationCloseService.Decision.REVIEW);
        assertThat(result.actions()).hasSize(4);
    }

    private InventoryValuationCloseService.Request request(String subledger, String ledger,
                                                            int negatives, int uncosted,
                                                            int adjustments, boolean prepared) {
        return new InventoryValuationCloseService.Request("IVC-100", "2026-09",
                new BigDecimal(subledger), new BigDecimal(ledger), new BigDecimal("1.00"),
                negatives, uncosted, adjustments, true, true, prepared, prepared, prepared, prepared);
    }
}
