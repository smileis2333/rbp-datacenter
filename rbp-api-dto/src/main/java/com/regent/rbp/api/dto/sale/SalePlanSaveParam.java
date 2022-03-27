package com.regent.rbp.api.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
public class SalePlanSaveParam {
    @NotBlank
    private String moduleId;

    @NotNull
    @ConflictManualIdCheck(targetTable = "rbp_sale_plan_bill")
    private String manualId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotNull
    @BusinessTypeCheck
    private String businessType;

    @NotNull
    @ChannelCodeCheck
    private String channelCode;

    @ApiModelProperty(notes = "价格类型名称")
    private String priceType;

    @CurrencyTypeCheck
    private String currencyType;

    private String notes;

    @NotNull
    @BillStatus
    private Integer status;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

    @Valid
    @NotEmpty
    @GoodsInfo
    private List<SalesPlanBillGoodsResult> goodsDetailData;

    // ---- 物流信息
    @ApiModelProperty(notes = "物流公司编号")
    private String logisticsCompanyCode;

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

    @ApiModelProperty(notes = "物流单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "说明")
    private String logisticsNotes;

}
