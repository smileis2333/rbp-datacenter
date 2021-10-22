package com.regent.rbp.api.core.coupon;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author LuZijian
 * @date 2021/3/5 5:24 下午
 */
@Data
@ApiModel(description = "券使用组使用会员范围")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_coupon_rule_member_range")
public class CouponRuleMemberRange extends Model<CouponRuleMemberRange> {
    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "券使用组id")
    private Long couponRuleId;
    @ApiModelProperty(notes = "类别 表名-比如rbp_member_organization表")
    private String memberCategory;
    @ApiModelProperty(notes = "会员属性 表字段名")
    private String memberAttributeColumn;
    @ApiModelProperty(notes = "券使用组使用会员范围值")
    @TableField(exist = false)
    private List<CouponRuleMemberRangeValue> couponRuleMemberRangeValueList;
    @ApiModelProperty(notes = "是否反选")
    private Integer reverseSelect;
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
