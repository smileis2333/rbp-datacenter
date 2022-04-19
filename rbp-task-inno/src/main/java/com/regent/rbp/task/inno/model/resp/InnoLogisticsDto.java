package com.regent.rbp.task.inno.model.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2022/4/19 16:45
 */
@Data
public class InnoLogisticsDto {

    @ApiModelProperty(notes = "App退换货SN")
    private String return_sn;

    @ApiModelProperty(notes = "物流单号")
    private String shipping_no;

    @ApiModelProperty(notes = "填写时间")
    private String modify_time;

    @ApiModelProperty(notes = "物流公司")
    private String Shipping_name;
}
