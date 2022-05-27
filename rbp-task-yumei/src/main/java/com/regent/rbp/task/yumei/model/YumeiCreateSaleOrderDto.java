package com.regent.rbp.task.yumei.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 玉美推送销售单返回
 * @author: HaiFeng
 * @create: 2022/5/27 13:17
 */
@Data
public class YumeiCreateSaleOrderDto {

    @ApiModelProperty(notes = "")
    private String code;

    @ApiModelProperty(notes = "")
    private String flag;

    @ApiModelProperty(notes = "")
    private String msg;

    @ApiModelProperty(notes = "")
    private String requestId;

    @ApiModelProperty(notes = "")
    private String subCode;

    @ApiModelProperty(notes = "")
    private String subMsg;

}
