package com.regent.rbp.task.inno.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/20 13:29
 */
@Data
public class IntegralResultDto {
    @ApiModelProperty(notes = "单据类型名称")
    private String billTypeName;

    @ApiModelProperty(notes = "核销单号")
    private String checkID;

    @ApiModelProperty(notes = "核销日期")
    private Date checkDate;

    @ApiModelProperty(notes = "手工单号")
    private String manual_ID;

    @ApiModelProperty(notes = "店铺编号")
    private String customer_ID;

    @ApiModelProperty(notes = "会员卡号")
    private String vip;

    @ApiModelProperty(notes = "会员姓名")
    private String vipName;

    @ApiModelProperty(notes = "积分值，正数增加，负数减少")
    private BigDecimal integralAmount;

    @ApiModelProperty(notes = "备注")
    private String remark;
}
