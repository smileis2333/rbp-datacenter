package com.regent.rbp.api.core.member;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 会员政策
 * @author: HaiFeng
 * @create: 2021-09-15 13:45
 */
@Data
@ApiModel(description = "会员政策")
@TableName(value = "rbp_member_policy")
public class MemberPolicy {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "会员体系id")
    private Long memberSystemId;

    @ApiModelProperty(notes = "会员等级编码")
    private String gradeCode;

    @ApiModelProperty(notes = "会员等级名称")
    private String gradeName;

    @ApiModelProperty(notes = "会员类型编码")
    private Long memberTypeId;

    @ApiModelProperty(notes = "会员积分政策折扣")
    private BigDecimal integralPolicyDiscount;

    @ApiModelProperty(notes = "会员积分政策生意额")
    private BigDecimal integralPolicySaleValue;

    @ApiModelProperty(notes = "会员积分政策积分")
    private BigDecimal integralPolicyIntegralValue;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "是否默认等级")
    private Integer isDefault;

    @ApiModelProperty(notes = "是否充值")
    private Integer isRecharge;

    @ApiModelProperty(notes = "POS新增会员是否显示")
    private Integer isCreateShow;

    @ApiModelProperty(notes = "有效天数")
    private Integer validateDay;

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
