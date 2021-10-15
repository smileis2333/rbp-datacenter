package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: inno 接口数据返回
 * @author: HaiFeng
 * @create: 2021-10-15 15:15
 */
@Data
public class InnoDataRespDto {

    @ApiModelProperty(notes = "返回单号")
    private String RowUniqueKey;

    @ApiModelProperty(notes = "1：正常，其他：异常编码")
    private String code;

    @ApiModelProperty(notes = "提示")
    private String msg;
}
