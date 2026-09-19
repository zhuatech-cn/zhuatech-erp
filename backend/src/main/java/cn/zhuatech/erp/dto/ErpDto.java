/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.dto;

import cn.zhuatech.erp.model.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public final class ErpDto {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private ErpDto() {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record DashboardView(
        BigDecimal salesAmount,
        long activeSalesOrders,
        long pendingPurchaseOrders,
        long lowStockProducts,
        BigDecimal outstandingReceivable,
        BigDecimal outstandingPayable,
        long todayStockMovements) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ProductCreateRequest(
        @NotBlank @Size(max = 40) String sku,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 60) String category,
        @NotBlank @Size(max = 20) String unit,
        @NotNull @DecimalMin("0") BigDecimal costPrice,
        @NotNull @DecimalMin("0") BigDecimal salePrice,
        @NotNull @DecimalMin("0") BigDecimal stockOnHand,
        @NotNull @DecimalMin("0") BigDecimal safetyStock) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record ProductView(Long id, String sku, String name, String category, String unit,
                              BigDecimal costPrice, BigDecimal salePrice, BigDecimal stockOnHand,
                              BigDecimal safetyStock, boolean lowStock, boolean enabled) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static ProductView from(Product product) {
            return new ProductView(product.getId(), product.getSku(), product.getName(), product.getCategory(),
                product.getUnit(), product.getCostPrice(), product.getSalePrice(), product.getStockOnHand(),
                product.getSafetyStock(), product.getStockOnHand().compareTo(product.getSafetyStock()) <= 0,
                product.isEnabled());
        }
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PartnerCreateRequest(
        @NotBlank @Size(max = 40) String code,
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "CUSTOMER|SUPPLIER") String type,
        @Size(max = 50) String contactName,
        @Size(max = 30) String phone,
        @Size(max = 255) String address,
        @NotNull @DecimalMin("0") BigDecimal creditLimit) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PartnerView(Long id, String code, String name, String type, String contactName,
                              String phone, String address, BigDecimal creditLimit, String status) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static PartnerView from(BusinessPartner partner) {
            return new PartnerView(partner.getId(), partner.getCode(), partner.getName(),
                partner.getType().name(), partner.getContactName(), partner.getPhone(), partner.getAddress(),
                partner.getCreditLimit(), partner.getStatus().name());
        }
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record SalesOrderCreateRequest(
        @NotBlank @Size(max = 40) String orderNo,
        @NotNull Long customerId,
        @NotNull LocalDate orderDate,
        @NotBlank @Size(max = 500) String itemSummary,
        @Min(1) int itemCount,
        @NotNull @DecimalMin("0.01") BigDecimal totalAmount,
        @NotNull @DecimalMin("0") BigDecimal paidAmount) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record SalesOrderView(Long id, String orderNo, Long customerId, String customerName,
                                 LocalDate orderDate, String itemSummary, int itemCount,
                                 BigDecimal totalAmount, BigDecimal paidAmount, BigDecimal unpaidAmount,
                                 String status, String ownerName) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static SalesOrderView from(SalesOrder order) {
            return new SalesOrderView(order.getId(), order.getOrderNo(), order.getCustomer().getId(),
                order.getCustomer().getName(), order.getOrderDate(), order.getItemSummary(), order.getItemCount(),
                order.getTotalAmount(), order.getPaidAmount(), order.getTotalAmount().subtract(order.getPaidAmount()),
                order.getStatus().name(), order.getOwnerName());
        }
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PurchaseOrderCreateRequest(
        @NotBlank @Size(max = 40) String orderNo,
        @NotNull Long supplierId,
        @NotNull LocalDate orderDate,
        LocalDate expectedDate,
        @NotBlank @Size(max = 500) String itemSummary,
        @Min(1) int itemCount,
        @NotNull @DecimalMin("0.01") BigDecimal totalAmount) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record PurchaseOrderView(Long id, String orderNo, Long supplierId, String supplierName,
                                    LocalDate orderDate, LocalDate expectedDate, String itemSummary,
                                    int itemCount, BigDecimal totalAmount, String status, String buyerName) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static PurchaseOrderView from(PurchaseOrder order) {
            return new PurchaseOrderView(order.getId(), order.getOrderNo(), order.getSupplier().getId(),
                order.getSupplier().getName(), order.getOrderDate(), order.getExpectedDate(), order.getItemSummary(),
                order.getItemCount(), order.getTotalAmount(), order.getStatus().name(), order.getBuyerName());
        }
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record OrderStatusRequest(@NotBlank String status) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record StockMovementCreateRequest(
        @NotNull Long productId,
        @NotBlank @Pattern(regexp = "INBOUND|OUTBOUND|ADJUSTMENT") String type,
        @NotNull @DecimalMin("0.001") BigDecimal quantity,
        @Size(max = 40) String referenceNo) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record StockMovementView(Long id, String movementNo, Long productId, String sku,
                                    String productName, String type, BigDecimal quantity,
                                    BigDecimal beforeQuantity, BigDecimal afterQuantity,
                                    String referenceNo, String handledBy, LocalDateTime occurredAt) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static StockMovementView from(StockMovement movement) {
            return new StockMovementView(movement.getId(), movement.getMovementNo(),
                movement.getProduct().getId(), movement.getProduct().getSku(), movement.getProduct().getName(),
                movement.getType().name(), movement.getQuantity(), movement.getBeforeQuantity(),
                movement.getAfterQuantity(), movement.getReferenceNo(), movement.getHandledBy(),
                movement.getOccurredAt());
        }
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record FinanceCreateRequest(
        @NotBlank @Size(max = 40) String recordNo,
        @NotBlank @Pattern(regexp = "RECEIVABLE|PAYABLE|EXPENSE|RECEIPT") String type,
        @Size(max = 120) String partnerName,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        LocalDate dueDate,
        @Size(max = 40) String referenceNo,
        @Size(max = 500) String remark) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record FinanceSettleRequest(@NotNull @DecimalMin("0") BigDecimal settledAmount) {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record FinanceView(Long id, String recordNo, String type, String partnerName,
                              BigDecimal amount, BigDecimal settledAmount, BigDecimal outstandingAmount,
                              LocalDate dueDate, String status, String referenceNo, String remark) {
        /**
         * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
         */
        public static FinanceView from(FinanceRecord record) {
            return new FinanceView(record.getId(), record.getRecordNo(), record.getType().name(),
                record.getPartnerName(), record.getAmount(), record.getSettledAmount(),
                record.getAmount().subtract(record.getSettledAmount()), record.getDueDate(),
                record.getStatus().name(), record.getReferenceNo(), record.getRemark());
        }
    }
}

