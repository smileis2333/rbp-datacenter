package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class CheckRetailSendBillMainDto {

    @ApiModelProperty(notes = "订单号")
    private String orderSn;

    @ApiModelProperty(notes = "是否能发货，1能发货，0不能发货")
    private Integer canDelivery;

    @ApiModelProperty(notes = "不能发货原因")
    private String reason;

    @ApiModelProperty(notes = "货品明细")
    private List<CheckRetailSendBillGoodsDto> orderGoods;

}
