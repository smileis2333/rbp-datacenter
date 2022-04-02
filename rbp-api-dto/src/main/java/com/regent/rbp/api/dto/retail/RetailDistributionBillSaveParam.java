package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-03-31
 * @Description
 */
@Data
public class RetailDistributionBillSaveParam {

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "外部单号，唯一")
    private String manualId;

    @ApiModelProperty(notes = "发货渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "接单状态。(0-未接单、1-已接单)")
    private Integer acceptOrderStatus;

    @ApiModelProperty(notes = "打印状态。未传默认0，0-未打印、1-已打印")
    private Integer printStatus;

    @ApiModelProperty(notes = "打印次数")
    private Integer printCount;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailDistributionBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "物流信息")
    private List<RetailDistributionBillLogisticsInfoDto> logisticsInfo;
}
