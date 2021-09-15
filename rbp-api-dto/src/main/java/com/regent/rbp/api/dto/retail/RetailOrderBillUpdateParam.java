package com.regent.rbp.api.dto.retail;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description 全渠道订单
 * @Author chenchungui
 * @Date 2021-09-14
 **/
@Data
public class RetailOrderBillUpdateParam {

    @ApiModelProperty(notes = "全渠道订单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "外部单号，唯一。")
    private String manualId;

    @ApiModelProperty(notes = "线上订单号")
    private String onlineOrderCode;

    @ApiModelProperty(notes = "平台类型(0-无;1-线下店铺;2-Inno微商城)")
    private Integer onlinePlatformTypeId;

    @ApiModelProperty(notes = "销售渠道编号")
    private String retailChannelNo;

    @ApiModelProperty(notes = "发货渠道编号")
    private String sendChannelNo;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废,4.已配货,5.已发货)")
    private Integer status;

    @ApiModelProperty(notes = "线上状态 0-等待买家付款、1-已付款+货到付款、2-货到付款等待发货、3-买家已付款、4-卖家部分发货、5-卖家已发货、6-买家已签收、7-买家拒签、8-交易成功、9-交易关闭")
    private Integer onlineStatus;

    @ApiModelProperty(notes = "营业员名称")
    private String employeeName;

    @ApiModelProperty(notes = "买家备注")
    private String buyerNotes;

    @ApiModelProperty(notes = "卖家备注")
    private String sellerNotes;

    @ApiModelProperty(notes = "打印备注")
    private String printNotes;

    @ApiModelProperty(notes = "客服备注")
    private String notes;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    /**************************** 物流信息 *********************************/

    @ApiModelProperty(notes = "会员卡号")
    private String memberCardCode;

    @ApiModelProperty(notes = "买家昵称")
    private String buyerNickname;

    @ApiModelProperty(notes = "买家账号")
    private String buyerAccount;

    @ApiModelProperty(notes = "买家邮箱")
    private String buyerEmail;

    @ApiModelProperty(notes = "物流公司名称")
    private String logisticsCompanyName;

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

    @ApiModelProperty(notes = "物流费用")
    private BigDecimal logisticsAmount;

    @ApiModelProperty(notes = "物流说明")
    private String note;

    /************************* 货品明细 ************************************/

    @ApiModelProperty(notes = "货品明细")
    private List<RetailOrderBillGoodsDetailData> goodsDetailData;
}
