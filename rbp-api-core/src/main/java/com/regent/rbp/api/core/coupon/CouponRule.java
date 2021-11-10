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
 * @date 2021/2/24 5:25 下午
 */
@Data
@ApiModel(description = "券使用组")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_coupon_rule")
public class CouponRule extends Model<CouponRule> {
    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "券使用组编码")
    private String code;
    @ApiModelProperty(notes = "券使用组名称")
    private String name;
    @ApiModelProperty(notes = "券使用组规则描述")
    private String notes;

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

    @ApiModelProperty(notes = "使用店铺")
    @TableField(exist = false)
    private List<CouponRuleChannelRange> couponRuleChannelRangeList;

    @ApiModelProperty(notes = "使用货品")
    @TableField(exist = false)
    private List<CouponRuleGoodsRange> couponRuleGoodsRangeList;

    @ApiModelProperty(notes = "使用会员")
    @TableField(exist = false)
    private List<CouponRuleMemberRange> couponRuleMemberRangeList;

    @ApiModelProperty(notes = "券类型")
    @TableField(exist = false)
    private String bonusType;

    @ApiModelProperty(notes = "支付方式编号")
    @TableField(exist = false)
    private String paymentCode;

}
