package com.regent.rbp.api.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道资料查询返回
 * @author: HaiFeng
 * @create: 2021-09-11 11:32
 */
@Data
public class ChannelQueryResult {
    @JsonIgnore
    private Long channelId;
    private String channelCode;
    private String channelName;
    private String channelFullName;
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
    private Integer linkManMobile;
    private String fundAccount;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    private PhysicalRegion physicalRegion;
    private ChannelBarrio channelBarrio;
    private Channelorganization channelorganization;
    private List<AddressDataDto> addressData;
    private List<CustomizeDataDto> customizeData;
}
