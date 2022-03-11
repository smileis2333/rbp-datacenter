package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.Dictionary;
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

    @NotBlank
    private String channelCode;
    @NotBlank
    private String channelName;
    @NotBlank
    private String channelFullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date channelBuildDate;
    private String channelAddress;
    private List<String> brand;
    private String branchCompany;
    private String grade;
    @NotBlank
    @Dictionary(targetTable = "rbp_channel_business_format",targetField = "name")
    private String businessFormat;
    @NotBlank
    @Dictionary(targetTable = "rbp_channel_business_nature",targetField = "name")
    private String businessNature;
    private String balanceType;
    private String retailTagPriceType;
    private String saleTagPriceType;
    private String saleRange;
    private String linkMan;
    private String linkManMobile;
    private String fundAccount;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private PhysicalRegion physicalRegion;
    private ChannelBarrio channelBarrio;
    private Channelorganization channelorganization;
    @Valid
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;
    @NotNull
    @BillStatus
    private Integer status;

}
