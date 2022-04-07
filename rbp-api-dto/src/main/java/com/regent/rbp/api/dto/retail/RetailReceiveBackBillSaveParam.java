package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-03
 * @Description
 */
@Data
public class RetailReceiveBackBillSaveParam {

    @ApiModelProperty(notes = "外部单号，唯一")
    private String manualId;

    @ApiModelProperty(notes = "全渠道退货通知单号")
    private String retailReturnNoticeBillNo;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "快递公司")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "快递单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailReceiveBackBillGoodsDetailData> goodsDetailData;
}