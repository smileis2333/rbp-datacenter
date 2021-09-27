package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillGoodsUploadParam {

    @ApiModelProperty(notes = "全渠道发货单号")
    private String billNo;

    @ApiModelProperty(notes = "条码")
    private String barcode;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

}
