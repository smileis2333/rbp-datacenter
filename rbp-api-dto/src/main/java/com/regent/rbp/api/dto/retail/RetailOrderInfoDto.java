package com.regent.rbp.api.dto.retail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-07
 * @Description
 */
@Data
public class RetailOrderInfoDto {

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
    private Date payTime;
    /**
     * 分销员编号
     */
    private String guideNo;

    private List<RetalOrderGoodsInfoDto> orderItems;
}
