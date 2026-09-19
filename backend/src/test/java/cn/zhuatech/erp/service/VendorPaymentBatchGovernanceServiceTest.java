/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;
import org.junit.jupiter.api.Test; import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class VendorPaymentBatchGovernanceServiceTest {
    private final VendorPaymentBatchGovernanceService service = new VendorPaymentBatchGovernanceService();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private VendorPaymentBatchGovernanceService.Request request(BigDecimal amount, boolean evidence, boolean reconcile) {
        return new VendorPaymentBatchGovernanceService.Request("PAY-1", "maker", "checker", amount,
            true,true,true,true,true,true,true,true,true,evidence,reconcile,false,true);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void releasesControlledPaymentBatch() {
        var a=service.assess(request(new BigDecimal("100000"),true,true));
        assertThat(a.decision()).isEqualTo(VendorPaymentBatchGovernanceService.Decision.RELEASE);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void reviewsMissingEvidenceAndReconciliation() {
        var a=service.assess(request(new BigDecimal("100000"),false,false));
        assertThat(a.decision()).isEqualTo(VendorPaymentBatchGovernanceService.Decision.REVIEW);
        assertThat(a.actions()).hasSize(2);
    }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void blocksHighValueBatchWithoutTreasuryReview() {
        var r=new VendorPaymentBatchGovernanceService.Request("PAY-2","same","same",new BigDecimal("900000"),
            true,true,false,true,true,true,true,true,false,true,true,true,false);
        var a=service.assess(r);
        assertThat(a.decision()).isEqualTo(VendorPaymentBatchGovernanceService.Decision.BLOCKED);
        assertThat(a.riskLevel()).isEqualTo(VendorPaymentBatchGovernanceService.RiskLevel.HIGH);
        assertThat(a.blockers()).hasSizeGreaterThanOrEqualTo(4);
    }
}
