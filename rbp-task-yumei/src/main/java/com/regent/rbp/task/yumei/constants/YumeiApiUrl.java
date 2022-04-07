package com.regent.rbp.task.yumei.constants;


public class YumeiApiUrl {

    public static final String ACCESS_TOKEN = "/auth/accessToken";

    /**
     * 销售订单_推送
     */
    public static final String SALE_ORDER_PUSH = "/api/trade/tradeCreate";

    /**
     * 销售订单_退款
     */
    public static final String SALE_ORDER_REFUND = "api/trade/orderRefund";

    /**
     * 销售订单_确认收货
     */
    public static final String SALE_ORDER_CONFIRM_RECEIPT = "api/trade/orderReceipt";

    /**
     * 订单查询接口
     */
    public static final String SALE_ORDER_QUERY = "api/trade/orderQuery";
}
