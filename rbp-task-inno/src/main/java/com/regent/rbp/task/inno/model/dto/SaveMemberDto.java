package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 新增会员请求 Dto
 * @author: HaiFeng
 * @create: 2021-10-19 13:55
 */
@Data
public class SaveMemberDto {

    @ApiModelProperty(notes = "开始时间")
    private String beginTime;

    @ApiModelProperty(notes = "结束时间")
    private String endTime;

    @ApiModelProperty(notes = "手机号码")
    private String mobileList;

    @ApiModelProperty(notes = "会员卡号")
    private String cardnumList;

    @ApiModelProperty(notes = "用户来源")
    private String user_src;

    @ApiModelProperty(notes = "页码")
    private String pageIndex;
}
