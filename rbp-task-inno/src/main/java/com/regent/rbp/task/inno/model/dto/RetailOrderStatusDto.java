package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-15
 * @Description
 */
@Data
public class RetailOrderStatusDto {

    @ApiModelProperty(notes = "订单编号")
    private String orderSn;

    @ApiModelProperty(notes = "订单状态，0未确认，1已确认，2已取消，5，取消中，8申请取消，9拒绝取消，10拆单，11已关闭")
    private String orderStatus;

    @ApiModelProperty(notes = "支付状态，0未支付，2已支付，3待退款，4已退款，5退款中")
    private String payStatus;

    @ApiModelProperty(notes = "配送状态，0未配货，1已发货，2已收货，3配货中，4部分发货，5发货中")
    private String shippingStatus;

}
