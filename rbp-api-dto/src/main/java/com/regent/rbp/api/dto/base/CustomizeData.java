package com.regent.rbp.api.dto.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 自定义字段
 * @author: HaiFeng
 * @create: 2021-09-11 13:24
 */
@Data
public class CustomizeData {

    @ApiModelProperty(notes = "字段编码")
    private String code;

    @ApiModelProperty(notes = "字段名称")
    private String name;

    @ApiModelProperty(notes = "字段值")
    private String value;
}
