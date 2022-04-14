package com.regent.rbp.api.core.coupon;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2022/4/14 14:40
 */
@Data
@ApiModel(description="券模版抵用券属性设置")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_coupon_model_cash_property")
public class CouponModelCashProperty extends Model<CouponModelCashProperty> {

    @ApiModelProperty(notes = "编码")
    private Long id;
    @ApiModelProperty(notes = "优惠券编号")
    private Long couponModelId;

    @ApiModelProperty(notes = "金额")
    private BigDecimal amount;
    @ApiModelProperty(notes = "生意额")
    private BigDecimal balancePriceLimit;
    @ApiModelProperty(notes = "支付方式;关联支付方式")
    private Long payId;

    @ApiModelProperty(notes = "是否对外叠加 0否1是")
    private Integer overlayNumLimit;
    @ApiModelProperty(notes = "是否自叠加 0代表不允许，其他代表允许多少张")
    private Integer selfOverlayNumLimit;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

}
