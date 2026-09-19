/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import cn.zhuatech.erp.common.BusinessException;
import cn.zhuatech.erp.dto.ErpDto.*;
import cn.zhuatech.erp.model.*;
import cn.zhuatech.erp.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class ErpService {
    private final ProductRepository products;
    private final PartnerRepository partners;
    private final SalesOrderRepository salesOrders;
    private final PurchaseOrderRepository purchaseOrders;
    private final StockMovementRepository stockMovements;
    private final FinanceRecordRepository financeRecords;
    private final CurrentUserService currentUser;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ErpService(ProductRepository products, PartnerRepository partners,
                      SalesOrderRepository salesOrders, PurchaseOrderRepository purchaseOrders,
                      StockMovementRepository stockMovements, FinanceRecordRepository financeRecords,
                      CurrentUserService currentUser) {
        this.products = products;
        this.partners = partners;
        this.salesOrders = salesOrders;
        this.purchaseOrders = purchaseOrders;
        this.stockMovements = stockMovements;
        this.financeRecords = financeRecords;
        this.currentUser = currentUser;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        if (products.existsBySku(request.sku())) throw new BusinessException("商品 SKU 已存在");
        return products.save(new Product(request.sku(), request.name(), request.category(), request.unit(),
            request.costPrice(), request.salePrice(), request.stockOnHand(), request.safetyStock()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public BusinessPartner createPartner(PartnerCreateRequest request) {
        if (partners.existsByCode(request.code())) throw new BusinessException("往来单位编码已存在");
        return partners.save(new BusinessPartner(request.code(), request.name(),
            BusinessPartner.Type.valueOf(request.type()), request.contactName(), request.phone(),
            request.address(), request.creditLimit()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public SalesOrder createSalesOrder(SalesOrderCreateRequest request) {
        if (salesOrders.existsByOrderNo(request.orderNo())) throw new BusinessException("销售订单号已存在");
        BusinessPartner customer = partners.findById(request.customerId())
            .orElseThrow(() -> new BusinessException("客户不存在"));
        if (customer.getType() != BusinessPartner.Type.CUSTOMER) throw new BusinessException("请选择客户类型的往来单位");
        if (request.paidAmount().compareTo(request.totalAmount()) > 0) throw new BusinessException("已收金额不能超过订单金额");
        return salesOrders.save(new SalesOrder(request.orderNo(), customer, request.orderDate(),
            request.itemSummary(), request.itemCount(), request.totalAmount(), request.paidAmount(),
            SalesOrder.Status.DRAFT, currentUser.get().getFullName()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public SalesOrder changeSalesStatus(Long id, String status) {
        SalesOrder order = salesOrders.findById(id).orElseThrow(() -> new BusinessException("销售订单不存在"));
        try {
            order.changeStatus(SalesOrder.Status.valueOf(status));
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("销售订单状态不正确");
        }
        return order;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public PurchaseOrder createPurchaseOrder(PurchaseOrderCreateRequest request) {
        if (purchaseOrders.existsByOrderNo(request.orderNo())) throw new BusinessException("采购订单号已存在");
        BusinessPartner supplier = partners.findById(request.supplierId())
            .orElseThrow(() -> new BusinessException("供应商不存在"));
        if (supplier.getType() != BusinessPartner.Type.SUPPLIER) throw new BusinessException("请选择供应商类型的往来单位");
        return purchaseOrders.save(new PurchaseOrder(request.orderNo(), supplier, request.orderDate(),
            request.expectedDate(), request.itemSummary(), request.itemCount(), request.totalAmount(),
            PurchaseOrder.Status.DRAFT, currentUser.get().getFullName()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public PurchaseOrder changePurchaseStatus(Long id, String status) {
        PurchaseOrder order = purchaseOrders.findById(id).orElseThrow(() -> new BusinessException("采购订单不存在"));
        try {
            order.changeStatus(PurchaseOrder.Status.valueOf(status));
        } catch (IllegalArgumentException exception) {
            throw new BusinessException("采购订单状态不正确");
        }
        return order;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public StockMovement createStockMovement(StockMovementCreateRequest request) {
        Product product = products.findById(request.productId()).orElseThrow(() -> new BusinessException("商品不存在"));
        StockMovement.Type type = StockMovement.Type.valueOf(request.type());
        BigDecimal before = product.getStockOnHand();
        BigDecimal delta = type == StockMovement.Type.OUTBOUND ? request.quantity().negate() : request.quantity();
        product.changeStock(delta);
        String movementNo = "MV" + System.currentTimeMillis();
        return stockMovements.save(new StockMovement(movementNo, product, type, request.quantity(), before,
            product.getStockOnHand(), request.referenceNo(), currentUser.get().getFullName(), LocalDateTime.now()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public FinanceRecord createFinanceRecord(FinanceCreateRequest request) {
        if (financeRecords.existsByRecordNo(request.recordNo())) throw new BusinessException("财务单据号已存在");
        return financeRecords.save(new FinanceRecord(request.recordNo(), FinanceRecord.Type.valueOf(request.type()),
            request.partnerName(), request.amount(), BigDecimal.ZERO, request.dueDate(), FinanceRecord.Status.PENDING,
            request.referenceNo(), request.remark()));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Transactional
    public FinanceRecord settleFinanceRecord(Long id, BigDecimal amount) {
        FinanceRecord record = financeRecords.findById(id).orElseThrow(() -> new BusinessException("财务单据不存在"));
        record.settle(amount);
        return record;
    }
}

