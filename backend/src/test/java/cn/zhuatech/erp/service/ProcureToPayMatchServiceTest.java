/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProcureToPayMatchServiceTest {
    private final ProcureToPayMatchService service = new ProcureToPayMatchService();

    @Test
    void matchesInvoiceInsideTolerance() {
        var result = service.assess(request("10", "9.90", "99.00", false, true));
        assertThat(result.decision()).isEqualTo(ProcureToPayMatchService.Decision.MATCH);
        assertThat(result.paymentHold()).isFalse();
    }

    @Test
    void reviewsCommercialVarianceOutsideTolerance() {
        var result = service.assess(request("10", "11.00", "110.00", false, true));
        assertThat(result.decision()).isEqualTo(ProcureToPayMatchService.Decision.REVIEW);
        assertThat(result.variances()).hasSize(2);
        assertThat(result.paymentHold()).isTrue();
    }

    @Test
    void holdsOverInvoicingAndDuplicateInvoice() {
        var result = service.assess(request("12", "10.00", "120.00", true, true));
        assertThat(result.decision()).isEqualTo(ProcureToPayMatchService.Decision.HOLD);
        assertThat(result.blockers()).contains("检测到重复发票号码或重复金额组合", "发票数量超过已收货数量");
    }

    @Test
    void holdsInvoiceWithoutPostedReceipt() {
        var result = service.assess(request("10", "10.00", "100.00", false, false));
        assertThat(result.decision()).isEqualTo(ProcureToPayMatchService.Decision.HOLD);
        assertThat(result.blockers()).contains("收货记录尚未过账");
    }

    private ProcureToPayMatchService.Request request(String invoiceQty, String invoicePrice,
                                                      String total, boolean duplicate, boolean received) {
        return new ProcureToPayMatchService.Request("INV-1", "PO-1", "GR-1",
                new BigDecimal("15"), new BigDecimal("10"), new BigDecimal(invoiceQty),
                new BigDecimal("10"), new BigDecimal(invoicePrice), new BigDecimal(total),
                new BigDecimal("2"), true, true, true, duplicate, received);
    }
}
