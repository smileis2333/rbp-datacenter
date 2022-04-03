package com.regent.rbp.task.yumei.service;

import com.regent.rbp.task.yumei.model.YumeiOrder;
import com.regent.rbp.task.yumei.model.YumeiOrderItems;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
public interface SaleOrderService {

    /**
     * 订单确认收货接口
     * @param storeNo
     * @param orderSource
     * @param outOrderNo
     */
    void confirmReceive(String storeNo,String orderSource,String outOrderNo);

    /**
     * 订单推送接口
     * @param storeNo
     * @param orderSource
     */
    void pushOrder(String storeNo, String orderSource, List<YumeiOrder> orders);

    /**
     * 订单退款
     * @param storeNo 门店编号
     * @param outOrderNo 订单编号
     * @param notifyUrl 回调地址
     * @param data 订单只项
     */
    void orderRefund(String storeNo, String outOrderNo, String notifyUrl, List<YumeiOrderItems> data);

    /**
     * 确认收货
     * @param storeNo 门店编号
     * @param outOrderNo 订单编号
     */
    void orderReceipt(String storeNo, String outOrderNo);


}
