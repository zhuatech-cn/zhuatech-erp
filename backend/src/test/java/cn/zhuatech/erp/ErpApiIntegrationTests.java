/* Copyright 2026 Shanghai Rujing Zhihua Information Technology Co., Ltd. · https://www.zhuatech.cn/ */
package cn.zhuatech.erp;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@SpringBootTest
@AutoConfigureMockMvc
class ErpApiIntegrationTests {
    @Autowired
    MockMvc mvc;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void managerCanReadBusinessDashboardAndModules() throws Exception {
        String token = login("demo", "Demo@2026", "MANAGER");
        mvc.perform(get("/api/erp/dashboard").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.salesAmount").value(160080.00))
            .andExpect(jsonPath("$.data.lowStockProducts").value(1));
        mvc.perform(get("/api/erp/sales-orders").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].orderNo").value("SO20260726003"));
        mvc.perform(get("/api/erp/finance-records").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void managerCanCreateInboundMovementAndStockIsUpdated() throws Exception {
        String token = login("demo", "Demo@2026", "MANAGER");
        mvc.perform(post("/api/erp/stock-movements").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\":2,\"type\":\"INBOUND\",\"quantity\":50,\"referenceNo\":\"PO-TEST-001\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.beforeQuantity").value(12.000))
            .andExpect(jsonPath("$.data.afterQuantity").value(62.000));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void salesRoleCannotCreateProductMasterData() throws Exception {
        String token = login("sales", "Demo@2026", "SALES");
        mvc.perform(post("/api/erp/products").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"sku\":\"TEST-001\",\"name\":\"测试商品\",\"category\":\"测试\",\"unit\":\"件\",\"costPrice\":1,\"salePrice\":2,\"stockOnHand\":0,\"safetyStock\":0}"))
            .andExpect(status().isForbidden());
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void unauthenticatedBusinessRequestIsRejected() throws Exception {
        mvc.perform(get("/api/erp/dashboard"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("请先登录或重新登录"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void authenticationAndRequestErrorsUseStructuredStatusCodes() throws Exception {
        mvc.perform(get("/api/erp/dashboard").header("Authorization", "Bearer invalid-token"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("请先登录或重新登录"));
        mvc.perform(get("/api/auth/login"))
            .andExpect(status().isMethodNotAllowed())
            .andExpect(jsonPath("$.message").value("请求方法不支持"));
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{bad-json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("请求体格式不正确"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void managerCanAnalyzeCashExposure() throws Exception {
        String token = login("demo", "Demo@2026", "MANAGER");
        mvc.perform(post("/api/erp/insights/cash-exposure").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"receivables\":1000000,\"overdueReceivables\":400000,\"payables\":700000,\"cashBalance\":300000,\"monthlyFixedCost\":200000}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.data.netWorkingCapital").value(600000))
            .andExpect(jsonPath("$.data.overdueRatio").value(0.4))
            .andExpect(jsonPath("$.data.runwayMonths").value(1.5))
            .andExpect(jsonPath("$.data.riskLevel").value("HIGH"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test
    void managerCanGenerateMaterialShortagePlan() throws Exception {
        String token = login("demo", "Demo@2026", "MANAGER");
        mvc.perform(post("/api/erp/insights/material-shortage").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"items":[
                      {"sku":"ZH-SN-016","name":"温湿度传感器","onHand":12,"safetyStock":30,"forecastDemand":60,"inboundQty":20,"leadDays":18,"unitCost":96},
                      {"sku":"ZH-GW-001","name":"工业边缘网关","onHand":86,"safetyStock":20,"forecastDemand":30,"inboundQty":0,"leadDays":7,"unitCost":1680}
                    ]}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.shortageItems").value(1))
            .andExpect(jsonPath("$.data.capitalRequired").value(5568.00))
            .andExpect(jsonPath("$.data.items[0].sku").value("ZH-SN-016"))
            .andExpect(jsonPath("$.data.items[0].suggestedOrder").value(58))
            .andExpect(jsonPath("$.data.items[0].riskLevel").value("HIGH"));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    private String login(String username, String password, String expectedRole) throws Exception {
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.user.role").value(expectedRole))
            .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.data.token");
    }
}
