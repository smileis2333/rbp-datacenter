package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 更新退货单收货状态
 * @author: HaiFeng
 * @create: 2021-10-15 14:29
 */
@Data
public class UpdateReturnOrderStatusDto {

    @ApiModelProperty(notes = "退款单号")
    private String return_sn;

    @ApiModelProperty(notes = "原始订单号")
    private String order_sn;

    @ApiModelProperty(notes = "外部退货单号")
    private String outer_return_sn;

    @ApiModelProperty(notes = "1：允许 0：不允许")
    private Integer is_succ;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "确认收货时间，时间格2020-07-13 09:30:30，不传则默认是当前时间")
    private String rec_time;
}
