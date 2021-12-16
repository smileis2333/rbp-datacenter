package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道组织架构
 * @author: HaiFeng
 * @create: 2021-09-11 17:53
 */
@Data
@ApiModel(description = "渠道组织架构")
@TableName(value = "rbp_channel_organization")
public class ChannelOrganization {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "父级id,一级则为0")
    private Long parentId;

    @ApiModelProperty(notes = "深度")
    private Integer depth;

    @ApiModelProperty(notes = "名称")
    private String name;

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

    @TableField(exist = false)
    private List<ChannelOrganization> childrenData;

    public void addChild(ChannelOrganization co){
        if (childrenData == null) {
            childrenData = new ArrayList<>();
        }
        childrenData.add(co);
    }
}
