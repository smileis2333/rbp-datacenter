package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class RetailSendBillUploadParam {

    @ApiModelProperty(notes = "全渠道发货单号")
    private String billNo;

    @ApiModelProperty(notes = "全渠道订单号")
    private String retailOrderBillNo;

    @ApiModelProperty(notes = "线上订单号")
    private String onlineOrderNo;

    @ApiModelProperty(notes = "发货时间")
    private String billDate;

    @ApiModelProperty(notes = "渠道编码")
    private String channelCode;

    @ApiModelProperty(notes = "物流公司名称")
    private String logisticsCompanyName;

    @ApiModelProperty(notes = "物流公司编码")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "收货人")
    private String contactsPerson;

    @ApiModelProperty(notes = "地址")
    private String address;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省份")
    private String province;

    @ApiModelProperty(notes = "城市")
    private String city;

    @ApiModelProperty(notes = "地区")
    private String county;

    @ApiModelProperty(notes = "邮件")
    private String email;

    @ApiModelProperty(notes = "邮编")
    private String postCode;

    @ApiModelProperty(notes = "电话")
    private String tel;

    @ApiModelProperty(notes = "手机")
    private String mobile;

    @ApiModelProperty(notes = "保险费")
    private BigDecimal insureFee;

    @ApiModelProperty(notes = "快递费用")
    private BigDecimal logisticsAmount;

    @ApiModelProperty(notes = "物流单号")
    private String logisticsNo;

    @ApiModelProperty(notes = "发货单位代码")
    private String deliveryStoreCode;

    @ApiModelProperty(notes = "发货类型，0门店，1仓库")
    private Integer deliveryType;

    @ApiModelProperty(notes = "货品明细")
    private List<RetailSendBillGoodsUploadParam> billGoodsList;

}
