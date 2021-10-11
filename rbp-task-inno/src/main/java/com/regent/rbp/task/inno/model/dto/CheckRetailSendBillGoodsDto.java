package com.regent.rbp.task.inno.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.infrastructure.util.MD5Util;
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

    @ApiModelProperty(notes = "唯一标记")
    @JsonIgnore
    public String getSingleCode() {
        return MD5Util.shortenKeyString(this.goodsSn, this.sku);
    }

}
