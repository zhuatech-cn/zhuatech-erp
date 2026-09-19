/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.erp.service;

import jakarta.validation.constraints.DecimalMin;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class CashExposureService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Result analyze(Request request) {
        BigDecimal netWorkingCapital = request.cashBalance().add(request.receivables()).subtract(request.payables());
        BigDecimal overdueRatio = request.receivables().signum() == 0 ? BigDecimal.ZERO
            : request.overdueReceivables().divide(request.receivables(), 4, RoundingMode.HALF_UP);
        BigDecimal runwayMonths = request.monthlyFixedCost().signum() == 0 ? BigDecimal.valueOf(999)
            : request.cashBalance().divide(request.monthlyFixedCost(), 2, RoundingMode.HALF_UP);
        String riskLevel = overdueRatio.compareTo(new BigDecimal("0.30")) >= 0 || runwayMonths.compareTo(new BigDecimal("2")) < 0
            ? "HIGH" : overdueRatio.compareTo(new BigDecimal("0.15")) >= 0 || runwayMonths.compareTo(new BigDecimal("4")) < 0 ? "MEDIUM" : "LOW";
        List<String> actions = new ArrayList<>();
        if (overdueRatio.compareTo(new BigDecimal("0.15")) >= 0) actions.add("对逾期应收按金额和账龄分层催收");
        if (runwayMonths.compareTo(new BigDecimal("4")) < 0) actions.add("滚动更新十三周现金流并控制非必要支出");
        if (netWorkingCapital.signum() < 0) actions.add("协商供应商账期并调整付款节奏");
        if (actions.isEmpty()) actions.add("保持月度营运资金复盘");
        return new Result(netWorkingCapital, overdueRatio, runwayMonths, riskLevel, actions);
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@DecimalMin("0") BigDecimal receivables,
                          @DecimalMin("0") BigDecimal overdueReceivables,
                          @DecimalMin("0") BigDecimal payables,
                          @DecimalMin("0") BigDecimal cashBalance,
                          @DecimalMin("0") BigDecimal monthlyFixedCost) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Result(BigDecimal netWorkingCapital, BigDecimal overdueRatio,
                         BigDecimal runwayMonths, String riskLevel, List<String> actions) {}
}
