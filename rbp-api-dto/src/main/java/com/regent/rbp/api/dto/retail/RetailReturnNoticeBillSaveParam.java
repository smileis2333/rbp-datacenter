package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单 创建/更新 Param
 * @author: HaiFeng
 * @create: 2021-09-27 15:38
 */
@Data
public class RetailReturnNoticeBillSaveParam {

    @ApiModelProperty(notes = "外部单号（RBP手工单号），唯一。")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "销售渠道编号")
    private String saleChannelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String receiveChannelCode;

    @ApiModelProperty(notes = "快递公司")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "快递单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "全渠道订单号")
    private String retailOrdereBillNo;

    @ApiModelProperty(notes = "单据状态(0-未审核，1-已审核，2-反审核，3-已作废，4-已验货，5-已发货，6-已超时)")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "线上退货单号")
    private String onlineReturnNoticeCode;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailReturnNoticeBillGoodsDetailData> goodsDetailData;
}
