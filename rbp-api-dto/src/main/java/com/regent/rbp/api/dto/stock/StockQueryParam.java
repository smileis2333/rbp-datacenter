package com.regent.rbp.api.dto.stock;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
@Data
public class StockQueryParam {

    @ApiModelProperty(notes = "库存类型，默认1。1：库存；2：可用库存；3：在途库存")
    private Integer stockType;

    @ApiModelProperty(notes = "渠道编号")
    private String[] channelCodeList;

    @ApiModelProperty(notes = "云仓编号")
    private String[] warehouseCodeList;

    @ApiModelProperty(notes = "相同货品SKU库存是否合并，默认0。0：不合并；1：合并")
    private Integer merge;

    @ApiModelProperty(notes = "货号，不传默认全部。注：最多传50个货品。")
    private String[] goodsCodeList;

    @ApiModelProperty(notes = "条形码。注：如果传值，则忽略货号条件；最多50个sku。")
    private String[] barcodeList;

    private int pageNo;
    private int pageSize;
}
