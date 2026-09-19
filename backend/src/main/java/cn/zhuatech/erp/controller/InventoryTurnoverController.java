/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;

import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.service.InventoryTurnoverService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/erp/insights")
public class InventoryTurnoverController {
    private final InventoryTurnoverService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public InventoryTurnoverController(InventoryTurnoverService service) { this.service = service; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/inventory-turnover")
    public ApiResponse<InventoryTurnoverService.Result> analyze(@Valid @RequestBody InventoryTurnoverService.Request request) {
        return ApiResponse.ok(service.analyze(request));
    }
}
