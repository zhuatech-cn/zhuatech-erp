/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.controller;

import cn.zhuatech.erp.common.ApiResponse;
import cn.zhuatech.erp.dto.ErpDto.*;
import cn.zhuatech.erp.model.*;
import cn.zhuatech.erp.repository.*;
import cn.zhuatech.erp.service.ErpService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/erp")
public class ErpController {
    private final ProductRepository products;
    private final PartnerRepository partners;
    private final SalesOrderRepository salesOrders;
    private final PurchaseOrderRepository purchaseOrders;
    private final StockMovementRepository stockMovements;
    private final FinanceRecordRepository financeRecords;
    private final ErpService service;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ErpController(ProductRepository products, PartnerRepository partners,
                         SalesOrderRepository salesOrders, PurchaseOrderRepository purchaseOrders,
                         StockMovementRepository stockMovements, FinanceRecordRepository financeRecords,
                         ErpService service) {
        this.products = products;
        this.partners = partners;
        this.salesOrders = salesOrders;
        this.purchaseOrders = purchaseOrders;
        this.stockMovements = stockMovements;
        this.financeRecords = financeRecords;
        this.service = service;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/dashboard")
    public ApiResponse<DashboardView> dashboard() {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        return ApiResponse.ok(new DashboardView(salesOrders.sumActiveSales(),
            salesOrders.countByStatusIn(List.of(SalesOrder.Status.DRAFT, SalesOrder.Status.CONFIRMED, SalesOrder.Status.SHIPPED)),
            purchaseOrders.countByStatusIn(List.of(PurchaseOrder.Status.DRAFT, PurchaseOrder.Status.APPROVED)),
            products.countLowStock(), financeRecords.outstanding(FinanceRecord.Type.RECEIVABLE),
            financeRecords.outstanding(FinanceRecord.Type.PAYABLE),
            stockMovements.countByOccurredAtBetween(start, start.plusDays(1))));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/products")
    public ApiResponse<List<ProductView>> products() {
        return ApiResponse.ok(products.findAllByOrderByIdDesc().stream().map(ProductView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/products")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ApiResponse<ProductView> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.ok("商品创建成功", ProductView.from(service.createProduct(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/partners")
    public ApiResponse<List<PartnerView>> partners(@RequestParam(required = false) String type) {
        var data = type == null ? partners.findAllByOrderByNameAsc()
            : partners.findByTypeOrderByNameAsc(BusinessPartner.Type.valueOf(type));
        return ApiResponse.ok(data.stream().map(PartnerView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/partners")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ApiResponse<PartnerView> createPartner(@Valid @RequestBody PartnerCreateRequest request) {
        return ApiResponse.ok("往来单位创建成功", PartnerView.from(service.createPartner(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/sales-orders")
    public ApiResponse<List<SalesOrderView>> salesOrders() {
        return ApiResponse.ok(salesOrders.findAllByOrderByOrderDateDescIdDesc().stream().map(SalesOrderView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/sales-orders")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES')")
    public ApiResponse<SalesOrderView> createSalesOrder(@Valid @RequestBody SalesOrderCreateRequest request) {
        return ApiResponse.ok("销售订单创建成功", SalesOrderView.from(service.createSalesOrder(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PatchMapping("/sales-orders/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','SALES','WAREHOUSE')")
    public ApiResponse<SalesOrderView> changeSalesStatus(@PathVariable Long id,
                                                         @Valid @RequestBody OrderStatusRequest request) {
        return ApiResponse.ok(SalesOrderView.from(service.changeSalesStatus(id, request.status())));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/purchase-orders")
    public ApiResponse<List<PurchaseOrderView>> purchaseOrders() {
        return ApiResponse.ok(purchaseOrders.findAllByOrderByOrderDateDescIdDesc().stream().map(PurchaseOrderView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/purchase-orders")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','PURCHASING')")
    public ApiResponse<PurchaseOrderView> createPurchaseOrder(@Valid @RequestBody PurchaseOrderCreateRequest request) {
        return ApiResponse.ok("采购订单创建成功", PurchaseOrderView.from(service.createPurchaseOrder(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PatchMapping("/purchase-orders/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','PURCHASING','WAREHOUSE')")
    public ApiResponse<PurchaseOrderView> changePurchaseStatus(@PathVariable Long id,
                                                               @Valid @RequestBody OrderStatusRequest request) {
        return ApiResponse.ok(PurchaseOrderView.from(service.changePurchaseStatus(id, request.status())));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/stock-movements")
    public ApiResponse<List<StockMovementView>> stockMovements() {
        return ApiResponse.ok(stockMovements.findTop30ByOrderByOccurredAtDesc().stream().map(StockMovementView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/stock-movements")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','WAREHOUSE')")
    public ApiResponse<StockMovementView> createStockMovement(@Valid @RequestBody StockMovementCreateRequest request) {
        return ApiResponse.ok("库存流水创建成功", StockMovementView.from(service.createStockMovement(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/finance-records")
    public ApiResponse<List<FinanceView>> financeRecords() {
        return ApiResponse.ok(financeRecords.findAllByOrderByDueDateAscIdDesc().stream().map(FinanceView::from).toList());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/finance-records")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','FINANCE')")
    public ApiResponse<FinanceView> createFinanceRecord(@Valid @RequestBody FinanceCreateRequest request) {
        return ApiResponse.ok("财务单据创建成功", FinanceView.from(service.createFinanceRecord(request)));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PatchMapping("/finance-records/{id}/settle")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','FINANCE')")
    public ApiResponse<FinanceView> settleFinanceRecord(@PathVariable Long id,
                                                        @Valid @RequestBody FinanceSettleRequest request) {
        return ApiResponse.ok(FinanceView.from(service.settleFinanceRecord(id, request.settledAmount())));
    }
}

