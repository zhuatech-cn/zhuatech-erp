/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity
@Table(name = "erp_stock_movement")
public class StockMovement extends BaseEntity {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Type { INBOUND, OUTBOUND, ADJUSTMENT }

    @Column(nullable = false, unique = true, length = 40)
    private String movementNo;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal quantity;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal beforeQuantity;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal afterQuantity;
    @Column(length = 40)
    private String referenceNo;
    @Column(length = 50)
    private String handledBy;
    @Column(nullable = false)
    private LocalDateTime occurredAt;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected StockMovement() {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public StockMovement(String movementNo, Product product, Type type, BigDecimal quantity,
                         BigDecimal beforeQuantity, BigDecimal afterQuantity, String referenceNo,
                         String handledBy, LocalDateTime occurredAt) {
        this.movementNo = movementNo;
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.beforeQuantity = beforeQuantity;
        this.afterQuantity = afterQuantity;
        this.referenceNo = referenceNo;
        this.handledBy = handledBy;
        this.occurredAt = occurredAt;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getMovementNo() { return movementNo; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Product getProduct() { return product; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Type getType() { return type; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getBeforeQuantity() { return beforeQuantity; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getAfterQuantity() { return afterQuantity; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getReferenceNo() { return referenceNo; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getHandledBy() { return handledBy; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
}

