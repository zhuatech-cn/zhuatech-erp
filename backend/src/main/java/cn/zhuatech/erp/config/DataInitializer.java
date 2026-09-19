/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.config;

import cn.zhuatech.erp.model.*;
import cn.zhuatech.erp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository users;
    private final ProductRepository products;
    private final PartnerRepository partners;
    private final SalesOrderRepository salesOrders;
    private final PurchaseOrderRepository purchaseOrders;
    private final StockMovementRepository stockMovements;
    private final FinanceRecordRepository financeRecords;
    private final PasswordEncoder encoder;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public DataInitializer(UserRepository users, ProductRepository products, PartnerRepository partners,
                           SalesOrderRepository salesOrders, PurchaseOrderRepository purchaseOrders,
                           StockMovementRepository stockMovements, FinanceRecordRepository financeRecords,
                           PasswordEncoder encoder) {
        this.users = users;
        this.products = products;
        this.partners = partners;
        this.salesOrders = salesOrders;
        this.purchaseOrders = purchaseOrders;
        this.stockMovements = stockMovements;
        this.financeRecords = financeRecords;
        this.encoder = encoder;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Override
    @Transactional
    public void run(String... args) {
        if (users.count() > 0) return;

        users.save(new UserAccount("admin", encoder.encode("ZhuaTech@2026"), "系统管理员",
            UserAccount.Role.ADMIN, "数字化中心"));
        users.save(new UserAccount("demo", encoder.encode("Demo@2026"), "王经理",
            UserAccount.Role.MANAGER, "经营管理部"));
        users.save(new UserAccount("sales", encoder.encode("Demo@2026"), "陈销售",
            UserAccount.Role.SALES, "销售中心"));

        Product gateway = products.save(new Product("ZH-GW-001", "工业边缘网关 Pro", "智能硬件", "台",
            money("1680"), money("2680"), quantity("86"), quantity("20")));
        Product sensor = products.save(new Product("ZH-SN-016", "温湿度传感器", "智能硬件", "只",
            money("96"), money("168"), quantity("12"), quantity("30")));
        Product license = products.save(new Product("ZH-SW-ERP", "企业协同软件授权", "软件服务", "套",
            money("4500"), money("8800"), quantity("48"), quantity("10")));
        products.save(new Product("ZH-SV-001", "数字化实施服务包", "专业服务", "项",
            money("12000"), money("26000"), quantity("9"), quantity("3")));

        BusinessPartner ocean = partners.save(new BusinessPartner("C-1001", "海岳智能制造有限公司",
            BusinessPartner.Type.CUSTOMER, "刘经理", "13800001001", "上海市浦东新区", money("500000")));
        BusinessPartner nova = partners.save(new BusinessPartner("C-1002", "星云数科（上海）有限公司",
            BusinessPartner.Type.CUSTOMER, "周女士", "13800001002", "上海市闵行区", money("300000")));
        BusinessPartner source = partners.save(new BusinessPartner("S-2001", "华东电子供应链有限公司",
            BusinessPartner.Type.SUPPLIER, "赵主管", "13900002001", "江苏省苏州市", money("0")));
        BusinessPartner cloud = partners.save(new BusinessPartner("S-2002", "云桥软件技术有限公司",
            BusinessPartner.Type.SUPPLIER, "孙经理", "13900002002", "浙江省杭州市", money("0")));

        salesOrders.save(new SalesOrder("SO20260726001", ocean, LocalDate.now().minusDays(3),
            "工业边缘网关 Pro × 20；数字化实施服务包 × 1", 2, money("79600"), money("30000"),
            SalesOrder.Status.CONFIRMED, "陈销售"));
        salesOrders.save(new SalesOrder("SO20260726002", nova, LocalDate.now().minusDays(1),
            "企业协同软件授权 × 8", 1, money("70400"), money("70400"),
            SalesOrder.Status.SHIPPED, "陈销售"));
        salesOrders.save(new SalesOrder("SO20260726003", ocean, LocalDate.now(),
            "温湿度传感器 × 60", 1, money("10080"), money("0"),
            SalesOrder.Status.DRAFT, "王经理"));

        purchaseOrders.save(new PurchaseOrder("PO20260726001", source, LocalDate.now().minusDays(2),
            LocalDate.now().plusDays(5), "温湿度传感器 × 200", 1, money("19200"),
            PurchaseOrder.Status.APPROVED, "王经理"));
        purchaseOrders.save(new PurchaseOrder("PO20260726002", cloud, LocalDate.now(),
            LocalDate.now().plusDays(12), "软件组件年度技术支持", 1, money("38000"),
            PurchaseOrder.Status.DRAFT, "王经理"));

        stockMovements.save(new StockMovement("MV20260726001", gateway, StockMovement.Type.OUTBOUND,
            quantity("20"), quantity("106"), quantity("86"), "SO20260726001", "仓库管理员",
            LocalDateTime.now().minusHours(3)));
        stockMovements.save(new StockMovement("MV20260726002", license, StockMovement.Type.OUTBOUND,
            quantity("8"), quantity("56"), quantity("48"), "SO20260726002", "仓库管理员",
            LocalDateTime.now().minusHours(2)));
        stockMovements.save(new StockMovement("MV20260726003", sensor, StockMovement.Type.ADJUSTMENT,
            quantity("2"), quantity("10"), quantity("12"), "STOCKTAKE-0726", "王经理",
            LocalDateTime.now().minusMinutes(35)));

        financeRecords.save(new FinanceRecord("AR20260726001", FinanceRecord.Type.RECEIVABLE, ocean.getName(),
            money("79600"), money("30000"), LocalDate.now().plusDays(20), FinanceRecord.Status.PARTIAL,
            "SO20260726001", "项目首付款已到账"));
        financeRecords.save(new FinanceRecord("AR20260726002", FinanceRecord.Type.RECEIVABLE, nova.getName(),
            money("70400"), money("70400"), LocalDate.now().plusDays(5), FinanceRecord.Status.SETTLED,
            "SO20260726002", "已全额回款"));
        financeRecords.save(new FinanceRecord("AP20260726001", FinanceRecord.Type.PAYABLE, source.getName(),
            money("19200"), money("0"), LocalDate.now().plusDays(12), FinanceRecord.Status.PENDING,
            "PO20260726001", "到货验收后付款"));
        financeRecords.save(new FinanceRecord("EX20260726001", FinanceRecord.Type.EXPENSE, "知华科技",
            money("6800"), money("0"), LocalDate.now().plusDays(3), FinanceRecord.Status.PENDING,
            "EXP-0726", "项目差旅与现场实施费用"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BigDecimal money(String value) { return new BigDecimal(value).setScale(2); }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private BigDecimal quantity(String value) { return new BigDecimal(value).setScale(3); }
}

