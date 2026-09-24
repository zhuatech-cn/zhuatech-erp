/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;

import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.service.BankReconciliationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/erp")
public class BankReconciliationController {
    private final BankReconciliationService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BankReconciliationController(BankReconciliationService service) { this.service = service; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/bank-reconciliation")
    public ApiResponse<BankReconciliationService.Result> reconcile(
            @Valid @RequestBody BankReconciliationService.Request request) {
        return ApiResponse.ok("银行对账完成", service.reconcile(request));
    }
}
