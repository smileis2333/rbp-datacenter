package com.regent.rbp.api.dto.purchaseReturn;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知 查询
 * @author: HaiFeng
 * @create: 2021/12/30 14:03
 */
@Data
public class PurchaseReturnNoticeBillQueryParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单据编号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private String billDate;

    @ApiModelProperty(notes = "业务类型名称")
    private String[] businessType;

    @ApiModelProperty(notes = "供应商编号")
    private String[] supplierCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String[] toChannelCode;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "币种名称")
    private String[] currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
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
