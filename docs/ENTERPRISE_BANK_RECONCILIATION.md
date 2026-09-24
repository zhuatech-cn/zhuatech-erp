# 企业级银行自动对账

`POST /api/enterprise/erp/bank-reconciliation` 对银行流水和已过账总账分录执行一对一匹配。

匹配引擎优先使用业务参考号，在参考号缺失时使用金额和日期容差；同一分录出现多个候选时不会自动猜测，而是进入人工复核。系统同时检查重复流水、未过账凭证、币种、期末余额差异和未达账项，返回 `RECONCILED / REVIEW / BLOCKED`、匹配类型及异常清单。

商业授权或定制开发请微信添加微信号 `zhuatech` 或 `zhuatech2` 进行咨询。官网：[知华科技](https://www.zhuatech.cn/)。
