package com.regent.rbp.api.dto.notice;

import com.regent.rbp.api.dto.base.CustomizeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 指令单 查询返回
 * @author: chenchungui
 * @create: 2021-12-07
 */
@Data
public class NoticeBillQueryResult {

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "计划单号")
    private String salePlanNo;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "发货渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String toChannelCode;

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeData> customizeData;

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

    @ApiModelProperty(notes = "货品明细")
    private List<NoticeBillBillGoodsDetailData> goodsDetailData;

}
