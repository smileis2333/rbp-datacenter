package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillGoodsCheckRespDto {

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "条码")
    private String barcode;

    @ApiModelProperty(notes = "是否能发货，1能发货，0不能发货")
    private Integer canDelivery;

    public RetailSendBillGoodsCheckRespDto() {
    }

    public RetailSendBillGoodsCheckRespDto(String goodsCode, String barcode, Integer canDelivery) {
        this.goodsCode = goodsCode;
        this.barcode = barcode;
        this.canDelivery = canDelivery;
    }

}
