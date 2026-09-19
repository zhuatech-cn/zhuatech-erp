/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;

import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.service.ManualJournalPostingGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/enterprise/erp")
public class ManualJournalPostingGovernanceController {
    private final ManualJournalPostingGovernanceService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ManualJournalPostingGovernanceController(ManualJournalPostingGovernanceService service) { this.service = service; }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/manual-journal-posting")
    public ApiResponse<ManualJournalPostingGovernanceService.Assessment> assess(
            @Valid @RequestBody ManualJournalPostingGovernanceService.Request request) {
        return ApiResponse.ok("手工总账凭证过账评估完成", service.assess(request));
    }
}
