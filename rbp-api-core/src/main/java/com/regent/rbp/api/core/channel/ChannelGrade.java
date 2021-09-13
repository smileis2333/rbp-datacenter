package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 渠道等级
 * @author: HaiFeng
 * @create: 2021-09-11 14:34
 */
@Data
@ApiModel(description = "渠道等级")
@TableName(value = "rbp_channel_grade")
public class ChannelGrade {

    public ChannelGrade() {}

    public ChannelGrade(String code, String name) {
        this.id = SnowFlakeUtil.getDefaultSnowFlakeId();
        this.code = code;
        this.name = name;
        long userId = ThreadLocalGroup.getUserId();
        this.setCreatedBy(userId);
        this.setCreatedTime(new Date());
        this.setUpdatedBy(userId);
        this.setUpdatedTime(new Date());
    }

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "编号")
    private String code;

    @ApiModelProperty(notes = "名称")
    private String name;

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
