/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;

import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.service.CashExposureService;
import cn.zhuatech.erp.service.MaterialPlanningService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/erp/insights")
public class FinancialInsightController {
    private final CashExposureService service;
    private final MaterialPlanningService materialPlanningService;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public FinancialInsightController(CashExposureService service, MaterialPlanningService materialPlanningService) {
        this.service = service;
        this.materialPlanningService = materialPlanningService;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/cash-exposure")
    public ApiResponse<CashExposureService.Result> analyze(@Valid @RequestBody CashExposureService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/material-shortage")
    public ApiResponse<MaterialPlanningService.Result> planMaterials(
        @Valid @RequestBody MaterialPlanningService.Request request) {
        return ApiResponse.ok(materialPlanningService.plan(request));
    }
}
