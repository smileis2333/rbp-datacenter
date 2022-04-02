package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-01
 * @Description
 */
@Data
public class SalesSendYuMeiSaveParam {

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "全渠道订单号")
    private String retailOrderBillNo;

    @ApiModelProperty(notes = "外部单号，唯一")
    private String manualId;

    @ApiModelProperty(notes = "发货渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty(notes = "货品明细")
    private List<SalesSendYuMeiGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "物流信息")
    private List<SalesSendYuMeiLogisticsInfoDto> logisticsInfo;
}
