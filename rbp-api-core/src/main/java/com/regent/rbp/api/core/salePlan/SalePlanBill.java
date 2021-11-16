package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售计划
 * @author: HaiFeng
 * @create: 2021-11-15 15:02
 */
@Data
@TableName(value = "rbp_sale_plan_bill")
public class SalePlanBill {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "价格类型")
    private Long priceTypeId;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态")
    private Integer status;

    @ApiModelProperty(notes = "处理状态")
    private Integer processStatus;

    @ApiModelProperty(notes = "完结状态")
    private Integer finishFlag;

    @ApiModelProperty(notes = "审批状态")
    private Integer flowStatus;

    @ApiModelProperty(notes = "流程类型")
    private Integer flowType;

    @ApiModelProperty(notes = "流程实例编码")
    private String flowId;

    @ApiModelProperty(notes = "科目编号")
    private Integer subjectId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;
}
