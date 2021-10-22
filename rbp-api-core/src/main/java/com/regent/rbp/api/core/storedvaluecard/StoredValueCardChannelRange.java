package com.regent.rbp.api.core.storedvaluecard;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2020/11/7 16:30
 */
@Data
@ApiModel(description="储值卡使用范围 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_stored_value_card_channel_range")
public class StoredValueCardChannelRange extends Model<StoredValueCardChannelRange> {

    @ApiModelProperty(notes = "类型 1-店铺 2-区域 3-组织架构")
    private Integer type;

    @ApiModelProperty(notes = "储值卡政策编码")
    private Long storedValueCardPolicyId;

    @ApiModelProperty(notes = "储值卡使用范围值")
    @TableField(exist = false)
    private List<StoredValueCardChannelRangeValue> storedValueCardChannelRangeValueList;

}
