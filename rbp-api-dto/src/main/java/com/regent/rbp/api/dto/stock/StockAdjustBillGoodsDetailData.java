package com.regent.rbp.api.dto.stock;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.base.GoodsDetailData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class StockAdjustBillGoodsDetailData extends GoodsDetailData {
    @NotNull
    @ApiModelProperty("数量")
    private BigDecimal quantity;

    @ApiModelProperty("备注")
    private String remark;

    @Valid
    @ApiModelProperty("货品自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

}
