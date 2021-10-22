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

/**
 * @author LuZijian
 * @date 2021/3/5 5:25 下午
 */
@Data
@ApiModel(description = "券规则使用货品范围值")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_coupon_rule_goods_range_value")
public class CouponRuleGoodsRangeValue extends Model<CouponRuleGoodsRangeValue> {
    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "券使用组id")
    private Long couponRuleId;
    @ApiModelProperty(notes = "范围编码")
    private Long couponRuleGoodsRangeId;
    @ApiModelProperty(notes = "属性值")
    private Long valueId;
    @ApiModelProperty(notes = "属性值编号")
    @TableField(exist = false)
    private String valueCode;
    @ApiModelProperty(notes = "属性值名称")
    @TableField(exist = false)
    private String valueName;
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
