package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class CheckRetailSendBillDto {

    @ApiModelProperty(notes = "微商城订单号")
    private String orderSn;

    public CheckRetailSendBillDto(String orderSn) {
        this.orderSn = orderSn;
    }

}
