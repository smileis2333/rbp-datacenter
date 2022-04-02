package com.regent.rbp.api.dto.channel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 行政区域
 * @author: HaiFeng
 * @create: 2021-09-11 11:51
 */
@Data
public class ChannelBarrio {

    @ApiModelProperty("一级")
    private String barrio1;

    @ApiModelProperty("二级")
    private String barrio2;

    @ApiModelProperty("三级")
    private String barrio3;

    @ApiModelProperty("四级")
    private String barrio4;

    @ApiModelProperty("五级")
    private String barrio5;
}
