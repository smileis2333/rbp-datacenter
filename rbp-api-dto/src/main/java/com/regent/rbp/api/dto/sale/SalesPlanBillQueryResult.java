package com.regent.rbp.api.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class SalesPlanBillQueryResult {
    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date billDate;

    @ApiModelProperty("业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废)")
    private Integer status;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdDate;

    @ApiModelProperty(notes = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updatedDate;

    @ApiModelProperty(notes = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date checkDate;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<SalesPlanBillGoodsResult> goodsDetailData;

}
