package com.regent.rbp.api.core.coupon;

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
 * @date 2021/3/1 9:56 上午
 */
@Data
@ApiModel(description = "抵用券政策明细")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_cash_coupon_policy_detail")
public class CashCouponPolicyDetail extends Model<CashCouponPolicyDetail> {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "券政策id")
    private Long couponsPolicyId;
    @ApiModelProperty(notes = "生意额")
    private String saleAmount;
    @ApiModelProperty(notes = "可用券额")
    private String couponAmount;

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
