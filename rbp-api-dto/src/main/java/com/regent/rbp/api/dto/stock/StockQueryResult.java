package com.regent.rbp.api.dto.stock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
@Data
public class StockQueryResult {

    @ApiModelProperty(notes = "渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "云仓编号")
    private String warehouseCode;

    @ApiModelProperty(notes = "条形码")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;
}
