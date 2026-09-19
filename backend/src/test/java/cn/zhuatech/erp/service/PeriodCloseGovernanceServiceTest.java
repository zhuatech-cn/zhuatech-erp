/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class PeriodCloseGovernanceServiceTest {
    private final PeriodCloseGovernanceService service = new PeriodCloseGovernanceService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void closesFullyReconciledPeriod() {
        var result = service.assess(new PeriodCloseGovernanceService.Request(
                "2026-08", 6, 6, 0, true, true, true, true));
        assertThat(result.decision()).isEqualTo(PeriodCloseGovernanceService.Decision.CLOSE);
        assertThat(result.reconciliationCoverage()).isEqualTo(100);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksIncompletePeriod() {
        var result = service.assess(new PeriodCloseGovernanceService.Request(
                "2026-09", 5, 3, 4, false, false, false, false));
        assertThat(result.decision()).isEqualTo(PeriodCloseGovernanceService.Decision.BLOCKED);
        assertThat(result.blockers()).hasSize(4);
        assertThat(result.actions()).hasSize(2);
    }
}
