package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.core.base.Category;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道区域
 * @author: HaiFeng
 * @create: 2021-09-11 17:41
 */
@Data
@ApiModel(description = "渠道区域")
@TableName(value = "rbp_channel_area")
public class ChannelArea {

    public ChannelArea(){}

    public ChannelArea(Long parentId, Integer depth, String name, String columnName, String orderNumber) {
        this.id = SnowFlakeUtil.getDefaultSnowFlakeId();
        this.parentId = parentId;
        this.depth = depth;
        this.name = name;
        this.columnName = columnName;
        this.orderNumber = orderNumber;
        long userId = ThreadLocalGroup.getUserId();
        this.setCreatedBy(userId);
        this.setCreatedTime(new Date());
        this.setUpdatedBy(userId);
        this.setUpdatedTime(new Date());
    }

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "父级id,一级则为0")
    private Long parentId;

    @ApiModelProperty(notes = "深度")
    private Integer depth;

    @ApiModelProperty(notes = "名称")
    private String name;

    @ApiModelProperty(notes = "字段名")
    private String columnName;

    @ApiModelProperty(notes = "排序")
    private String orderNumber;

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
