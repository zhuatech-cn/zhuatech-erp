/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.repository;

import cn.zhuatech.erp.model.SalesOrder;
import org.springframework.data.jpa.repository.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @EntityGraph(attributePaths = "customer")
    List<SalesOrder> findAllByOrderByOrderDateDescIdDesc();
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Override
    @EntityGraph(attributePaths = "customer")
    Optional<SalesOrder> findById(Long id);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    boolean existsByOrderNo(String orderNo);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    long countByStatusIn(Collection<SalesOrder.Status> statuses);
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Query("select coalesce(sum(s.totalAmount), 0) from SalesOrder s where s.status <> cn.zhuatech.erp.model.SalesOrder.Status.CANCELLED")
    BigDecimal sumActiveSales();
}
