package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 下载会员请求
 * @author: HaiFeng
 * @create: 2021-10-19 11:47
 */
@Data
public class DownloadMemberParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;

    @ApiModelProperty(notes = "手机号")
    private List<String> mobileList;

    @ApiModelProperty(notes = "会员卡")
    private List<String> cardnumList;

}
