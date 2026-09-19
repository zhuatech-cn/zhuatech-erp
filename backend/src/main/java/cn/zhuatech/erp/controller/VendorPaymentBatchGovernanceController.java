/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;
import cn.zhuatech.erp.common.ApiResponse; import cn.zhuatech.erp.service.VendorPaymentBatchGovernanceService;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/enterprise/erp")
public class VendorPaymentBatchGovernanceController {
    private final VendorPaymentBatchGovernanceService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public VendorPaymentBatchGovernanceController(VendorPaymentBatchGovernanceService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/vendor-payment-batch")
    public ApiResponse<VendorPaymentBatchGovernanceService.Assessment> assess(@Valid @RequestBody VendorPaymentBatchGovernanceService.Request request) {
        return ApiResponse.ok("供应商付款批次评估完成", service.assess(request));
    }
}
