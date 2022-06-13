package com.regent.rbp.api.dto.send;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 发货单 查询返回
 * @author: chenchungui
 * @create: 2021-12-16
 */
@Data
public class SendBillQueryResult {

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long billId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "发货渠道编号")
    private String channelCode;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String channelName;

    @ApiModelProperty(notes = "收货渠道编号")
    private String toChannelCode;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String toChannelName;

    @ApiModelProperty(notes = "业务类型名称")
    private String businessType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @ApiModelProperty(notes = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date checkTime;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String checkByName;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String createdByName;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    /**************************** 物流信息 *********************************/

    @ApiModelProperty(notes = "物流公司编号")
    private String logisticsCompanyCode;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String logisticsCompanyName;

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
    private List<SendBillGoodsDetailData> goodsDetailData;

}
