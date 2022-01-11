package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

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

    private String channelCode;
    private String channelName;
    private String channelFullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date channelBuildDate;
    private String channelAddress;
    private String[] brand;
    private String branchCompany;
    private String grade;
    private String businessFormat;
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
    private List<AddressData> addressData;
    private List<CustomizeDataDto> customizeData;
    private Integer status = 0;

}
