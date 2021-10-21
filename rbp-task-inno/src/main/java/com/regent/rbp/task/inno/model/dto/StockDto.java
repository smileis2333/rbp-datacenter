package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 库存请求
 * @author: HaiFeng
 * @create: 2021-10-21 10:01
 */
@Data
public class StockDto {

    @ApiModelProperty(notes = "库存类型：默认1。1：库存；2：可⽤库存；3：在途 库存")
    private Integer stockType;

    @ApiModelProperty(notes = "类型。1：仓库；2：渠道")
    private Integer type;

    @ApiModelProperty(notes = "仓库编号或渠道编号")
    private List<String> unitNo;

    @ApiModelProperty(notes = "货号，不传默认全部。注：最多传50个货品。")
    private List<String> goodsNo;

    @ApiModelProperty(notes = "条形码。注：如果传值，则忽略货号条件；最多50个 sku。")
    private List<String> sku;

    @ApiModelProperty(notes = "库存⽇期：查询某天的库存，默认当前⽇期。注：库 存类型是“2:可⽤库存”时，此参数⽆⽤。")
    private String stockDate;

    @ApiModelProperty(notes = "页码")
    private Integer pageNo;

    @ApiModelProperty(notes = "每⻚条数：默认100条")
    private Integer pageSize;

    @ApiModelProperty(notes = "动态数据（注：F360需要 传\"SYSTEM_ModuleId\"）")
    private List<StockDynamicDto> dynamicData;

}
