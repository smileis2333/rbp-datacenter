package com.regent.rbp.task.yumei.service;


import com.regent.rbp.task.yumei.model.AuditData;

public interface OffshopService {
    /**
     * 根据单号创建退仓订单
     *
     * @param billNo
     */
    void createReturnOrder(String billNo, AuditData auditData);

    /**
     * 根据单号取消退仓订单
     *
     * @param billNo
     */
    void cancelReturnOrder(String billNo);

    /**
     * 渠道调出接口
     * @param billNo
     */
    void checkChannelTuneOut(String billNo);

    /**
     * 渠道调入接口
     * @param billNo
     */
    void checkChannelTuneIn(String billNo);

    /**
     * 审核库存调整单
     * @param billNo
     */
    void checkStockAdjust(String billNo);
}
