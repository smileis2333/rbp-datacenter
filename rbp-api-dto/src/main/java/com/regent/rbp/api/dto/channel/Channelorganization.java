package com.regent.rbp.api.dto.channel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 组织架构
 * @author: HaiFeng
 * @create: 2021-09-11 11:54
 */
@Data
public class Channelorganization {

    @ApiModelProperty("一级")
    private String organization1;

    @ApiModelProperty("二级")
    private String organization2;

    @ApiModelProperty("三级")
    private String organization3;

    @ApiModelProperty("四级")
    private String organization4;

    @ApiModelProperty("五级")
    private String organization5;

}
