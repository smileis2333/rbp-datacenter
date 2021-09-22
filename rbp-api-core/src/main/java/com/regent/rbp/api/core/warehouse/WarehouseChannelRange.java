package com.regent.rbp.api.core.warehouse;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
@Data
@ApiModel(description = "云仓渠道范围")
@TableName(value = "rbp_warehourse_channel_range")
public class WarehouseChannelRange extends Model<WarehouseChannelRange> {

    @TableId("id")
    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "云仓编码 对应表rbp_warehouse")
    private Long warehouseId;

    @ApiModelProperty(notes = "渠道编码 对应表rbp_channel")
    private Long channelId;

    @ApiModelProperty(notes = "云仓可用库存比例")
    private BigDecimal useRate;

}
