package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuzhicheng
 * @createTime 2022-04-19
 * @Description
 */
@Data
public class RetailOrderReceivedDto {

    @ApiModelProperty(notes = "订单编号")
    private String orderSn;

    @ApiModelProperty(notes = "收货时间")
    private String receivedTime;
}
