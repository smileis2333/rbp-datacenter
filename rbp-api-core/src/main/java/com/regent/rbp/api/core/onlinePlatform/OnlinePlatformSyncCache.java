package com.regent.rbp.api.core.onlinePlatform;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 线上平台同步数据的缓存
 * 场景：
 * 1.拉取平台数据，记录接口的最大拉取时间。
 *
 * @author chenchungui
 * @date 2021-09-22
 */
@Data
@ApiModel(description = "线上平台同步数据的缓存 ")
@TableName(value = "rbp_online_platform_sync_cache")
public class OnlinePlatformSyncCache {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "平台编码")
    private Long onlinePlatformId;

    @ApiModelProperty(notes = "同步key")
    private String syncKey;

    @ApiModelProperty(notes = "值")
    private String data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库默认时间
     */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    /**
     * 数据库默认时间
     */
    private Date updatedTime;

    public static OnlinePlatformSyncCache build() {
        return build(null, StrUtil.EMPTY, StrUtil.EMPTY);
    }

    public static OnlinePlatformSyncCache build(Long onlinePlatformId, String syncKey, String data) {
        OnlinePlatformSyncCache cache = new OnlinePlatformSyncCache();
        cache.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        cache.setOnlinePlatformId(onlinePlatformId);
        cache.setSyncKey(syncKey);
        cache.setData(data);

        return cache;
    }


}
