package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单付款方式
 * @author: HaiFeng
 * @create: 2021-11-08 17:53
 */
@Data
@ApiModel(description="销售单尺码明细表")
@TableName(value = "rbp_sales_order_bill_payment")
public class SalesOrderBillPayment {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "销售单编码")
    private Long billId;

    @ApiModelProperty(notes = "支付方式编码")
    private Long retailPayTypeId;

    @ApiModelProperty(notes = "支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty(notes = "找零金额")
    private BigDecimal returnMoney;

    @ApiModelProperty(notes = "支付平台编码")
    private Long platformId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}
