package com.regent.rbp.api.dto.send;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 发货单 查询
 * @author: chenchungui
 * @create: 2021-12-16
 */
@Data
public class SendBillQueryParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "外部单号")
    private String manualId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型名称")
    private String[] businessType;

    @ApiModelProperty(notes = "发货渠道编号")
    private String[] channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String[] toChannelCode;

    @ApiModelProperty(notes = "币种名称")
    private String[] currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废)")
    private Integer[] status;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;

    @ApiModelProperty(notes = "审核日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateStart;

    @ApiModelProperty(notes = "审核日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;

    @ApiModelProperty(notes = "需返回的字段列表")
    private String fields;

    @ApiModelProperty(notes = "页码：默认1")
    private Integer pageNo;

    @ApiModelProperty(notes = "每页条数：默认100条")
    private Integer pageSize;

}
