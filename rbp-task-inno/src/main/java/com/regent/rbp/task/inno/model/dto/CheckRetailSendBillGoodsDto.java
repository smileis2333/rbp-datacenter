package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class CheckRetailSendBillGoodsDto {

    @ApiModelProperty(notes = "货号")
    private String goodsSn;

    @ApiModelProperty(notes = "条码")
    private String sku;

    @ApiModelProperty(notes = "是否能发货，1能发货，0不能发货")
    private Integer canDelivery;

}
