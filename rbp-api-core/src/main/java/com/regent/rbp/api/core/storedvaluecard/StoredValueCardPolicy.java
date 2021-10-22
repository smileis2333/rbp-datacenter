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
 * @Date 2020/11/7 15:18
 */
@Data
@ApiModel(description="储值卡政策 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_stored_value_card_policy")
public class StoredValueCardPolicy extends Model<StoredValueCardPolicy> {
    @ApiModelProperty(notes = "类型编号")
    private String code;

    @ApiModelProperty(notes = "类型名称")
    private String name;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "储值卡使用范围")
    @TableField(exist = false)
    private List<StoredValueCardChannelRange> rangeList;
}
