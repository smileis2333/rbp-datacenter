package com.regent.rbp.api.core.channel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author huangjie
 * @date : 2022/04/07
 * @description
 */
@Data
@ApiModel(description="渠道选项值")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_channel_setting_value")
public class ChannelSettingValue {
    @ApiModelProperty(notes = "编码")
    private Long id;


    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;


    @ApiModelProperty(notes = "配置键")
    private String keyCode;


    @ApiModelProperty(notes = "配置值")
    private String value;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;


    /**
     * 插入之前执行方法，子类实现
     */

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }
}
