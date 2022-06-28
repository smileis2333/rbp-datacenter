package com.regent.rbp.api.dto.send;

import com.regent.rbp.api.dto.purchase.AbstractBusinessBillSaveParam;
import com.regent.rbp.api.dto.validate.BusinessBill;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 发货单 新增
 * @author: chenchungui
 * @create: 2021-12-16
 */
@Data
@BusinessBill(baseModuleId = "700005",baseTable = "rbp_send_bill")
public class SendBillSaveParam extends AbstractBusinessBillSaveParam {

    @ApiModelProperty(notes = "发货渠道编号")
    @ChannelCodeCheck
    @NotNull
    private String channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    @ChannelCodeCheck
    @NotNull
    private String toChannelCode;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    /**************************** 物流信息 *********************************/

    @ApiModelProperty(notes = "物流公司编号")
    private String logisticsCompanyCode;

    @ApiModelProperty(notes = "物流单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省份")
    private String province;

    @ApiModelProperty(notes = "城市")
    private String city;

    @ApiModelProperty(notes = "区/县")
    private String county;

    @ApiModelProperty(notes = "详细地址")
    private String address;

    @ApiModelProperty(notes = "收货人")
    private String contactsPerson;

    @ApiModelProperty(notes = "手机号码")
    private String mobile;

    @ApiModelProperty(notes = "邮编")
    private String postCode;

    @ApiModelProperty(notes = "物流说明")
    private String logisticsNotes;

    @Valid
    @NotEmpty
    @GoodsInfo
    @ApiModelProperty(notes = "货品明细")
    private List<SendBillGoodsDetailData> goodsDetailData;

}
