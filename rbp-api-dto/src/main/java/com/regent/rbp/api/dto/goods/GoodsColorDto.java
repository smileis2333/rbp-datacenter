package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class GoodsColorDto {

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long goodsId;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    private Long colorId;

    @ApiModelProperty(notes = "颜色名称")
    private String colorName;

    @ApiModelProperty(notes = "货品编码")
    private String goodsCode;
}
