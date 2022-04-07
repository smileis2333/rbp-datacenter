package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 分销信息
 * @author: HaiFeng
 * @create: 2022/4/7 17:08
 */
@Data
public class RetailOrderBillDstbInfo {

    @ApiModelProperty(notes = "分销等级")
    private Integer level;

    @ApiModelProperty(notes = "分销员编号")
    private String dstbCode;

    @ApiModelProperty(notes = "分销员手机")
    private String phone;

    @ApiModelProperty(notes = "分销员关联会员卡号")
    private String memberCode;

    @ApiModelProperty(notes = "提成类型")
    private Integer commType;

}
