package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 库存查询动态数据
 * @author: HaiFeng
 * @create: 2021-10-21 10:03
 */
@Data
public class StockDynamicDto {

    @ApiModelProperty(notes = "ID")
    private String id;

    @ApiModelProperty(notes = "值")
    private String value;
}
