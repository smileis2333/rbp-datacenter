package com.regent.rbp.api.dto.send;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenchungui
 * @date 2021/12/16
 * @description
 */
@Data
public class SendBillGoodsDetailData extends BillGoodsDetailData {

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @ApiModelProperty(notes = "指令单号")
    private String noticeNo;

    @ApiModelProperty(notes = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(notes = "行ID", hidden = true)
    @JsonIgnore
    private Long columnId;

    @ApiModelProperty(notes = "指令单ID", hidden = true)
    @JsonIgnore
    private Long noticeId;

    @ApiModelProperty(notes = "销售计划ID", hidden = true)
    @JsonIgnore
    private Long salePlanId;

}
