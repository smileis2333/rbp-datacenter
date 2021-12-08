package com.regent.rbp.api.dto.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 指令单 新增
 * @author: chenchungui
 * @create: 2021-12-07
 */
@Data
public class NoticeBillSaveParam {

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "计划单号")
    private String salePlanNo;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @ApiModelProperty(notes = "发货渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    private String toChannelCode;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

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

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;
}
