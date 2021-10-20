package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-10-19 17:39
 */
@Data
public class MemberPageDataRespDto {

    @ApiModelProperty(notes = "1：正常，其他：异常编码")
    private String code;

    @ApiModelProperty(notes = "提示")
    private String msg;

    private MemberPageRespDto data;
}
