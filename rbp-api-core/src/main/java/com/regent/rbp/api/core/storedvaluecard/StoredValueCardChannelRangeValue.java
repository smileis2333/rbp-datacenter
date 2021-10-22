package com.regent.rbp.api.core.storedvaluecard;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2020/11/9 15:42
 */
@Data
@ApiModel(description="储值卡使用范围值 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_stored_value_card_channel_range_value")
public class StoredValueCardChannelRangeValue {
    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "值编码")
    private Long valueId;

    @ApiModelProperty(notes = "储值卡使用范围编码")
    private Long storedValueCardChannelRangeId;

    @ApiModelProperty(notes = "值")
    @TableField(exist = false)
    private String valueName;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建人名称")
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;
}
