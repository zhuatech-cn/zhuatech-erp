/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp;

import cn.zhuatech.erp.service.InventoryTurnoverService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class InventoryTurnoverServiceTests {
    private final InventoryTurnoverService service = new InventoryTurnoverService();

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void detectsExcessInventory() {
        var result = service.analyze(new InventoryTurnoverService.Request("SKU-1001",
            new BigDecimal("100000"), new BigDecimal("80000"), new BigDecimal("70000"), new BigDecimal("10000")));
        assertThat(result.status()).isEqualTo("EXCESS");
        assertThat(result.turnoverTimes()).isEqualByComparingTo("1.25");
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void prioritizesShortageRisk() {
        var result = service.analyze(new InventoryTurnoverService.Request("SKU-1002",
            new BigDecimal("500000"), new BigDecimal("60000"), new BigDecimal("8000"), new BigDecimal("12000")));
        assertThat(result.status()).isEqualTo("SHORTAGE_RISK");
        assertThat(result.actions()).anyMatch(action -> action.contains("安全库存"));
    }
}
