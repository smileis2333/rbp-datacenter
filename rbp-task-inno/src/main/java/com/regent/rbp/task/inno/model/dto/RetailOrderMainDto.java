package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单
 *
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
public class RetailOrderMainDto {

    @ApiModelProperty(notes = "订单总体信息")
    private RetailOrderItemDto orderinfo;

    @ApiModelProperty(notes = "订单明细信息")
    private List<RetailOrderGoodsDto> ordergoods;

}
