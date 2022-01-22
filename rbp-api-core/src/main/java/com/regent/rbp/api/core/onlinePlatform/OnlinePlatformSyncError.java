package com.regent.rbp.api.core.onlinePlatform;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 线上平台同步数据失败列表
 * @author: HaiFeng
 * @create: 2022/1/20 17:46
 */
@Data
@ApiModel(description = "线上平台同步数据失败列表 ")
@TableName(value = "rbp_online_platform_sync_error")
public class OnlinePlatformSyncError {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "平台编码")
    private Long onlinePlatformId;

    @ApiModelProperty(notes = "同步key")
    private String syncKey;

    @ApiModelProperty(notes = "值")
    private String data;

    @ApiModelProperty(notes = "重试次数")
    private Integer retryTimes;

    @ApiModelProperty(notes = "成功标记")
    private Boolean successFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;


}
