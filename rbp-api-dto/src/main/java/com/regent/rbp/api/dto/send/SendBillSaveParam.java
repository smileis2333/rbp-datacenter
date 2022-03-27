package com.regent.rbp.api.dto.send;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.ConflictManualIdCheck;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 发货单 新增
 * @author: chenchungui
 * @create: 2021-12-16
 */
@Data
public class SendBillSaveParam {

    @ApiModelProperty(notes = "模块编号")
    @NotBlank
    private String moduleId;

    @ApiModelProperty(notes = "外部单号，唯一。对应Nebual手工单号")
    @ConflictManualIdCheck(targetTable = "rbp_send_bill")
    @NotBlank
    private String manualId;

    @ApiModelProperty(notes = "单据日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date billDate;

    @ApiModelProperty(notes = "发货渠道编号")
    @ChannelCodeCheck
    @NotNull
    private String channelCode;

    @ApiModelProperty(notes = "收货渠道编号")
    @ChannelCodeCheck
    @NotNull
    private String toChannelCode;

    @ApiModelProperty(notes = "业务类型名称")
    @NotBlank
    private String businessType;

    @ApiModelProperty(notes = "币种名称")
    private String currencyType;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态(0.未审核,1.已审核,2.反审核,3.已作废)")
    @BillStatus
    @NotNull
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

    @Valid
    @NotEmpty
    @GoodsInfo
    @ApiModelProperty(notes = "货品明细")
    private List<SendBillGoodsDetailData> goodsDetailData;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;
}
