package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: inno page 数据返回
 * @author: HaiFeng
 * @create: 2021-10-19 14:36
 */
@Data
public class InnoPageRespDto<T> {

    @ApiModelProperty(notes = "1：正常，其他：异常编码")
    private String code;

    @ApiModelProperty(notes = "提示")
    private String msg;

    @ApiModelProperty(notes = "返回明细")
    private T data;
}
