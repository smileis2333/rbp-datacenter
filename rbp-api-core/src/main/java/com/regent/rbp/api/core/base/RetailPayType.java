package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 零售付款方式
 * @author: HaiFeng
 * @create: 2021-11-09 16:48
 */
@Data
@ApiModel(description="零售付款方式 ")
@TableName(value = "rbp_retail_pay_type")
public class RetailPayType {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "付款方式编码")
    private String code;

    @ApiModelProperty(notes = "付款方式名称")
    private String name;

    @ApiModelProperty(notes = "是否计收入 0.计收入1.不计收入")
    private Integer notIncome;

    @ApiModelProperty(notes = "是否计积分 0.计积分1.不计积分")
    private Integer notInPoint;

    @ApiModelProperty(notes = "零售支付平台编码")
    private Long retailPayPlatformId;

    @ApiModelProperty(notes = "是否可用 1.可用")
    private Integer useable;

    @ApiModelProperty(notes = "是否显示 1.显示")
    private Integer visible;

    @ApiModelProperty(notes = "是否系统内置 1.系统内置,不可修改或删除")
    private Integer systemInline;

    @ApiModelProperty(notes = "排序 数字越小，排在越前")
    private Integer sort;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
