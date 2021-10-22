package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 13:24
 */
@Data
public class StoredValueCardRecordDto {
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
    @ApiModelProperty(notes = "储值，正数增加，负数减少")
    private BigDecimal amount;
}
