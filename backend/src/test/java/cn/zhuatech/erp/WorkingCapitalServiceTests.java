/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp;

import cn.zhuatech.erp.service.WorkingCapitalService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class WorkingCapitalServiceTests {
    private final WorkingCapitalService service = new WorkingCapitalService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void flagsLongCashConversionCycle() {
        var result = service.analyze(new WorkingCapitalService.Request("SH01", new BigDecimal("3650000"), new BigDecimal("2190000"),
            new BigDecimal("800000"), new BigDecimal("120000"), new BigDecimal("700000")));
        assertThat(result.riskLevel()).isEqualTo("HIGH");
        assertThat(result.daysSalesOutstanding()).isEqualByComparingTo("80.0");
        assertThat(result.actions()).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void recognizesHealthyWorkingCapital() {
        var result = service.analyze(new WorkingCapitalService.Request("SH02", new BigDecimal("3650000"), new BigDecimal("3650000"),
            new BigDecimal("300000"), new BigDecimal("400000"), new BigDecimal("200000")));
        assertThat(result.riskLevel()).isEqualTo("LOW");
    }
}
