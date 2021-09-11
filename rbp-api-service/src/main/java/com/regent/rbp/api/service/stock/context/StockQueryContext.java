package com.regent.rbp.api.service.stock.context;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description 库存查询上下文对象
 */
@Data
public class StockQueryContext {

    @ApiModelProperty(notes = "库存类型，默认1。1：库存；2：可用库存；3：在途库存")
    private Integer stockType;

    @ApiModelProperty(notes = "渠道id")
    private List<Long> channelIdList;

    @ApiModelProperty(notes = "云仓id")
    private List<Long> warehouseIdList;

    @ApiModelProperty(notes = "云仓相同货品SKU库存是否合并，默认0。0：不合并；1：合并")
    private Integer merge;

    @ApiModelProperty(notes = "货号，不传默认全部。注：最多传50个货品。")
    private List<Long> goodsIdList;

    @ApiModelProperty(notes = "条形码。注：如果传值，则忽略货号条件；最多50个sku。")
    private List<String> barcode;

    private int pageNo;
    private int pageSize;
}
