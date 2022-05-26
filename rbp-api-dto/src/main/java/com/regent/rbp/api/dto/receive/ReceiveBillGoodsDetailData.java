package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillGoodsDetailData extends BillGoodsDetailData {
    @ApiModelProperty("价格类型名称")
    @NotBlank
    private String priceType;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long goodsId;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long columnId;

    @JsonIgnore
    private BigDecimal planQuantity;

}
