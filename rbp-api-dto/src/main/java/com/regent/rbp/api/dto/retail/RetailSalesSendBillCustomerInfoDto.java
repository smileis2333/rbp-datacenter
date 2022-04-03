package com.regent.rbp.api.dto.retail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
public class RetailSalesSendBillCustomerInfoDto {

    @ApiModelProperty(notes = "会员卡号")
    private String memberCardCode;

    @ApiModelProperty(notes = "买家昵称")
    private String buyerNickname;

    @ApiModelProperty(notes = "买家账号")
    private String buyerAccount;

    @ApiModelProperty(notes = "买家邮箱")
    private String buyerEmail;

    @ApiModelProperty(notes = "收货人。若未传全渠道配单单据编号，则必传")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机。若未传全渠道配单单据编号，则必传")
    private String mobile;

    @ApiModelProperty(notes = "国家。若未传全渠道配单单据编号，则必传")
    private String nation;

    @ApiModelProperty(notes = "省份。若未传全渠道配单单据编号，则必传")
    private String province;

    @ApiModelProperty(notes = "城市。若未传全渠道配单单据编号，则必传")
    private String city;

    @ApiModelProperty(notes = "地区。若未传全渠道配单单据编号，则必传")
    private String county;

    @ApiModelProperty(notes = "详细地址。若未传全渠道配单单据编号，则必传")
    private String address;

    @ApiModelProperty(notes = "邮政编码")
    private String postCode;

    @ApiModelProperty(notes = "物流公司编号")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "物流费用")
    private BigDecimal logisticsAmount;

    @ApiModelProperty(notes = "物流说明")
    private String notes;
}
