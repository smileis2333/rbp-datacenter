package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.infrastructure.util.MD5Util;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenchungui
 * @date 2021-10-11
 */
@Data
public class RetailSendBillGoodsCheckReqDto {

    @ApiModelProperty(notes = "货品行号")
    private String rowUniqueKey;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "条码")
    private String barcode;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "唯一标记")
    @JsonIgnore
    public String getSingleCode() {
        return MD5Util.shortenKeyString(this.goodsCode, this.barcode);
    }

}
