/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.model;

import cn.zhuatech.erp.common.BusinessException;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity
@Table(name = "erp_product")
public class Product extends BaseEntity {
    @Column(nullable = false, unique = true, length = 40)
    private String sku;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 60)
    private String category;
    @Column(nullable = false, length = 20)
    private String unit;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal costPrice;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal salePrice;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal stockOnHand;
    @Column(nullable = false, precision = 14, scale = 3)
    private BigDecimal safetyStock;
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected Product() {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Product(String sku, String name, String category, String unit, BigDecimal costPrice,
                   BigDecimal salePrice, BigDecimal stockOnHand, BigDecimal safetyStock) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.stockOnHand = stockOnHand;
        this.safetyStock = safetyStock;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void changeStock(BigDecimal delta) {
        BigDecimal next = stockOnHand.add(delta);
        if (next.signum() < 0) throw new BusinessException("库存不足，无法完成出库");
        stockOnHand = next;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getSku() { return sku; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getName() { return name; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getCategory() { return category; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getUnit() { return unit; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getCostPrice() { return costPrice; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getSalePrice() { return salePrice; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getStockOnHand() { return stockOnHand; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getSafetyStock() { return safetyStock; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public boolean isEnabled() { return enabled; }
}

