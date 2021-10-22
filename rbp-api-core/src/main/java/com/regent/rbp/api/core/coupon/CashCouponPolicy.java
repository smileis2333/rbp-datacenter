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
 * @date 2021/2/24 5:25 下午
 */
@Data
@ApiModel(description = "抵用券政策")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_cash_coupon_policy")
public class CashCouponPolicy extends Model<CashCouponPolicy> {
    @ApiModelProperty(notes = "id")
    private Long id;
    @ApiModelProperty(notes = "券类型编码（政策编码）")
    private String code;
    @ApiModelProperty(notes = "券类型名称（政策名称）")
    private String name;
    @ApiModelProperty(notes = "最大使用张数")
    private Integer numberOfCoupon;
    @ApiModelProperty(notes = "组")
    private Integer policyGroup;
    @ApiModelProperty(notes = "备注")
    private String remark;

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
