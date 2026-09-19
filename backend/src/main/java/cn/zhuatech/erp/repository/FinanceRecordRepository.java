/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.repository;

import cn.zhuatech.erp.model.FinanceRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    List<FinanceRecord> findAllByOrderByDueDateAscIdDesc();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    boolean existsByRecordNo(String recordNo);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Query("select coalesce(sum(f.amount - f.settledAmount), 0) from FinanceRecord f where f.type = :type and f.status <> cn.zhuatech.erp.model.FinanceRecord.Status.SETTLED")
    BigDecimal outstanding(@Param("type") FinanceRecord.Type type);
}

