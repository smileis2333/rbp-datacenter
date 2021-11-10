package com.regent.rbp.api.dto.sale;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 销售单付款方式
 * @author: HaiFeng
 * @create: 2021-11-09 16:24
 */
@Data
public class SalesOrderBillPaymentResult {

    @ApiModelProperty(notes = "付款方式")
    private String retailPayTypeCode;

    @ApiModelProperty(notes = "支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(notes = "找零金额")
    private BigDecimal returnMoney;
}
