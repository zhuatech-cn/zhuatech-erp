/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;
import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.service.AiInvoiceAnomalyService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/erp/ai")
public class AiInvoiceAnomalyController {
    private final AiInvoiceAnomalyService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public AiInvoiceAnomalyController(AiInvoiceAnomalyService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/invoice-anomaly")
    public ApiResponse<AiInvoiceAnomalyService.Result> inspect(@Valid @RequestBody AiInvoiceAnomalyService.Request request) {
        return ApiResponse.ok(service.inspect(request));
    }
}
