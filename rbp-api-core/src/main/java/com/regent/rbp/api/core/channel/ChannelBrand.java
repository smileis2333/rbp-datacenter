package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道品牌关系
 * @author: HaiFeng
 * @create: 2021-09-11 16:05
 */
@Data
@ApiModel(description = "渠道品牌关系")
@TableName(value = "rbp_channel_brand")
public class ChannelBrand {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "品牌编码")
    private Long brandId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;
}
