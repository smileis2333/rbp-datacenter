package com.regent.rbp.api.core.coupon;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author LuZijian
 * @date 2022/1/21 5:14 下午
 */
@Data
@ApiModel(description = "券模版折扣券属性设置")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_coupon_model_discount_property")
public class CouponModelDiscountProperty extends Model<CouponModelDiscountProperty> {

    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "优惠券编号")
    private Long couponModelId;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;
    @ApiModelProperty(notes = "还原吊牌价 0否1是")
    private Integer restoreTagPrice;
    @ApiModelProperty(notes = "货品件数")
    private Integer numberOfGoods;
    @ApiModelProperty(notes = "消费金额上限")
    private BigDecimal maxSalesAmount;
    @ApiModelProperty(notes = "整单折扣下限")
    private BigDecimal lowerLimitDiscount;

    @ApiModelProperty(notes = "折扣区间")
    @TableField(exist = false)
    private List<DiscountCouponPolicyDetail> discountPropertyDetail;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
