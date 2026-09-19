/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.model;

import cn.zhuatech.erp.common.BusinessException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Entity
@Table(name = "erp_finance_record")
public class FinanceRecord extends BaseEntity {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Type { RECEIVABLE, PAYABLE, EXPENSE, RECEIPT }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Status { PENDING, PARTIAL, SETTLED }

    @Column(nullable = false, unique = true, length = 40)
    private String recordNo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;
    @Column(length = 120)
    private String partnerName;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal settledAmount;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
    @Column(length = 40)
    private String referenceNo;
    @Column(length = 500)
    private String remark;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    protected FinanceRecord() {}

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public FinanceRecord(String recordNo, Type type, String partnerName, BigDecimal amount,
                         BigDecimal settledAmount, LocalDate dueDate, Status status,
                         String referenceNo, String remark) {
        this.recordNo = recordNo;
        this.type = type;
        this.partnerName = partnerName;
        this.amount = amount;
        this.settledAmount = settledAmount;
        this.dueDate = dueDate;
        this.status = status;
        this.referenceNo = referenceNo;
        this.remark = remark;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public void settle(BigDecimal value) {
        if (value.signum() < 0 || value.compareTo(amount) > 0) throw new BusinessException("核销金额不正确");
        settledAmount = value;
        status = value.signum() == 0 ? Status.PENDING : value.compareTo(amount) >= 0 ? Status.SETTLED : Status.PARTIAL;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getRecordNo() { return recordNo; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Type getType() { return type; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getPartnerName() { return partnerName; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getAmount() { return amount; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public BigDecimal getSettledAmount() { return settledAmount; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public LocalDate getDueDate() { return dueDate; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Status getStatus() { return status; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getReferenceNo() { return referenceNo; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public String getRemark() { return remark; }
}

