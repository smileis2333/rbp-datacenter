package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.Dictionary;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道档案 新增/修改
 * @author: HaiFeng
 * @create: 2021-09-13 13:23
 */
@Data
public class ChannelSaveParam {

    @ApiModelProperty("渠道编号")
    @NotBlank
    private String channelCode;
    @ApiModelProperty("渠道简称")
    @NotBlank
    private String channelName;
    @ApiModelProperty("渠道名称")
    @NotBlank
    private String channelFullName;

    @ApiModelProperty("建档日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date channelBuildDate = new Date();
    @ApiModelProperty("详细地址")
    private String channelAddress;
    @ApiModelProperty("品牌")
    private List<String> brand;
    @ApiModelProperty("分公司")
    private String branchCompany;
    @ApiModelProperty("渠道等级")
    private String grade;
    @ApiModelProperty("渠道业态")
    @NotBlank
    @Dictionary(targetTable = "rbp_channel_business_format", targetField = "name")
    private String businessFormat;

    @ApiModelProperty("经营性质")
    @NotBlank
    @Dictionary(targetTable = "rbp_channel_business_nature", targetField = "name")
    private String businessNature;

    @ApiModelProperty("结算方式")
    private String balanceType;

    @ApiModelProperty("零售吊牌价类型")
    private String retailTagPriceType;

    @ApiModelProperty("分销吊牌价类型")
    private String saleTagPriceType;

    @ApiModelProperty("销售范围")
    private String saleRange;

    @ApiModelProperty("联系人")
    private String linkMan;

    @ApiModelProperty("联系人电话")
    private String linkManMobile;

    @ApiModelProperty("资金号")
    private String fundAccount;

    @ApiModelProperty("最低价格")
    private BigDecimal minPrice;

    @ApiModelProperty("最高价格")
    private BigDecimal maxPrice;

    @ApiModelProperty("物理区域")
    private PhysicalRegion physicalRegion;

    @ApiModelProperty("行政区域")
    private ChannelBarrio channelBarrio;

    @ApiModelProperty("组织架构")
    private Channelorganization channelorganization;

    @ApiModelProperty("渠道收货信息")
    @Valid
    private List<AddressData> addressData;

    @ApiModelProperty("自定义字段")
    private List<CustomizeDataDto> customizeData;

    @ApiModelProperty("单据状态(0.未审核,1.已审核)")
    @NotNull
    @DiscreteRange(ranges = {0, 1},message = "单据状态(0.未审核,1.已审核)")
    private Integer status;

}
