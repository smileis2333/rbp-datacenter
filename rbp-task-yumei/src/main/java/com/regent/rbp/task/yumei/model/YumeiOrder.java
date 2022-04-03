package com.regent.rbp.task.yumei.model;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Data
public class YumeiOrder {
    private String outTradeNo;
    private String buyerNick;
    private String receiver;
    private String provinceName;
    private String cityName;
    private String areaName;
    private String addrDetail;
    private String mobile;
    private String postCode;
    private String userRemark;
    private BigDecimal freightAmount;
    private BigDecimal actualTotalAmount;
    private BigDecimal goodsQty;
    private List<OrderItem> orderItems;

    @Data
    class OrderItem {
        private String goodsName;
        private String skuCode;
        private BigDecimal skuQty;
        private BigDecimal unitPrice;
        private String buyerRemark;
    }
}