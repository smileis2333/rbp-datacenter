package com.regent.rbp.task.yumei.service;

import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
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
     * 确认收货状态推送到玉美
     *
     * @param orderNoList 线上单号
     */
    String pushOrderReceiveStatusToYuMei(List<String> orderNoList);

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
     * @param orderSource 订单来源（1：美人计会员商城、2：酒会员商城、3：丽晶）
     * @param outOrderNo 订单编号
     * @param notifyUrl 回调地址
     * @param data 订单只项
     */
    Boolean orderRefund(String storeNo, Integer orderSource, String outOrderNo, String notifyUrl, List<YumeiOrderItems> data);

    /**
     * 确认收货
     * @param storeNo 门店编号
     * @param outOrderNo 订单编号
     * @param confirmTime 收货时间
     */
    String orderReceipt(String storeNo, Integer orderSource, String outOrderNo, String confirmTime);


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
    Boolean orderCancel(String storeNo, Integer orderSource, String outOrderNo);

    /**
     * 根据全渠道订单的分销员和会员获取所属门店编号
     *
     * @param retailOrderBillId 全渠道订单id
     * @return
     */
    OrderBusinessPersonDto getOrderBusinessPersonDto(Long retailOrderBillId);
}
