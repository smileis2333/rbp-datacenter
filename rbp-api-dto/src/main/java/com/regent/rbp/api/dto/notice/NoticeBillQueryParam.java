package com.regent.rbp.api.dto.notice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 指令单 查询
 * @author: chenchungui
 * @create: 2021-12-07
 */
@Data
public class NoticeBillQueryParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "计划单号")
    private String salePlanNo;

    @ApiModelProperty(notes = "外部单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private String billDate;

    @ApiModelProperty(notes = "业务类型名称")
    private String[] businessType;

    @ApiModelProperty(notes = "发货渠道编号")
    private String[] channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String[] toChannelCode;

    @ApiModelProperty(notes = "价格类型名称")
    private String[] priceType;

    @ApiModelProperty(notes = "币种名称")
    private String[] currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废)")
    private Integer[] status;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    private String createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    private String createdDateEnd;

    @ApiModelProperty(notes = "审核日期(开始日期)")
    private String checkDateStart;

    @ApiModelProperty(notes = "审核日期(截止日期)")
    private String checkDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    private String updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    private String updatedDateEnd;

    @ApiModelProperty(notes = "需返回的字段列表")
    private String fields;

    @ApiModelProperty(notes = "页码：默认1")
    private Integer pageNo;

    @ApiModelProperty(notes = "每页条数：默认100条")
    private Integer pageSize;

}
