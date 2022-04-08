package com.regent.rbp.task.yumei.service;

import com.regent.rbp.task.yumei.model.YumeiOrder;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryPageResp;
import com.regent.rbp.task.yumei.model.YumeiOrderQueryReq;
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
     *
     * @param storeNo
     * @param orderSource
     * @param outOrderNo
     */
    void confirmReceive(String storeNo, String orderSource, String outOrderNo);

    /**
     * 推送订单到玉美
     *
     * @param orderNoList 线上单号
     */
    public String pushOrderToYuMei(List<String> orderNoList);

    /**
     * 订单推送接口
     *
     * @param storeNo
     * @param orderSource
     */
    String pushOrder(String storeNo, String orderSource, List<YumeiOrder> orders);

    /**
     * 订单退款
     * @param storeNo 门店编号
     * @param outOrderNo 订单编号
     * @param notifyUrl 回调地址
     * @param data 订单只项
     */
    void orderRefund(String storeNo, Integer orderSource, String outOrderNo, String notifyUrl, List<YumeiOrderItems> data);

    /**
     * 确认收货
     * @param storeNo 门店编号
     * @param outOrderNo 订单编号
     */
    void orderReceipt(String storeNo, Integer orderSource, String outOrderNo);


    /**
     * 订单查询
     *
     * @param query
     * @return
     */
    YumeiOrderQueryPageResp orderQuery(YumeiOrderQueryReq query);

    /**
     * 订单取消
     * @param storeNo
     * @param orderSource
     * @param outOrderNo
     */
    void orderCancel(String storeNo, Integer orderSource, String outOrderNo);
}
