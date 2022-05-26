package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class GoodsDetailData {
    @ApiModelProperty("条形码")
    private String barcode;
    @ApiModelProperty("货品")
    private String goodsCode;
    @JsonIgnore
    private String goodsName;
    @ApiModelProperty("颜色编码")
    private String colorCode;
    @ApiModelProperty("内长")
    private String longName;
    @ApiModelProperty("尺码")
    private String size;
}
