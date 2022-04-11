package com.regent.rbp.task.yumei.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;
import java.util.Date;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    /**
     * 分销员编号
     */
    private String guideNo;
    private List<OrderItem> orderItems;

    @Data
    public class OrderItem {
        private String goodsName;
        private String skuCode;
        private BigDecimal skuQty;
        private BigDecimal unitPrice;
        private String buyerRemark;
    }
}