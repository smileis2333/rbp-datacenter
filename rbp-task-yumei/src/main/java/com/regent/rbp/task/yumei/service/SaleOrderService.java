package com.regent.rbp.task.yumei.service;

import com.regent.rbp.task.yumei.model.YumeiOrder;

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

}
