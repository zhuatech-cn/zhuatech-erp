# 库存估值关账

`POST /api/enterprise/erp/inventory-valuation-close` 将库存子账与总账勾稽结果纳入标准关账流程。

- 计算勾稽差异并与可配置容差比较。
- 阻断负库存、未计价入库、待过账成本调整和超容差关账。
- 要求锁定汇率、发布成本版本、冻结移动并归档证据。

返回 `CLOSE / REVIEW / BLOCKED`、差异金额、阻断原因和待办动作。
