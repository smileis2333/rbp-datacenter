package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 订单分销
 * @author: HaiFeng
 * @create: 2022/4/7 16:33
 */
@Data
public class RetailDstbInfoDto {

    @ApiModelProperty(notes = "分销等级，1表示1级分销员，2表示2级分销员")
    private Integer commLevel;

    @ApiModelProperty(notes = "分销员代码")
    private String dstbStaffCode;

    @ApiModelProperty(notes = "分销员手机号")
    private String dstbStaffPhone;

    @ApiModelProperty(notes = "分销员关联会员卡号")
    private String dstbCardNum;

    @ApiModelProperty(notes = "提成类型，0未知，1一次提成，2二次直接提成，3二次间接提成")
    private Integer commType;

}
