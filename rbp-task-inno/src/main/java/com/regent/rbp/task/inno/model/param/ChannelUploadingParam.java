package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;

import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道/仓库 上传请求
 * @author: HaiFeng
 * @create: 2021-09-22 11:49
 */
@Data
public class ChannelUploadingParam {

    @ApiModelProperty(notes = "平台编号")
    private String onlinePlatformCode;

}
