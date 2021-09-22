package com.regent.rbp.api.dto.channel;

import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道资料 查询请求
 * @author: HaiFeng
 * @create: 2021-09-11 13:26
 */
@Data
public class ChannelQueryParam {

    private String[] channelCode;
    private String channelName;
    private String channelFullName;
    private String[] channelAddress;
    private String[] brand;
    private String[] branchCompany;
    private String[] grade;
    private String[] businessFormat;
    private String[] businessNature;
    private String[] balanceType;
    private String[] retailTagPriceType;
    private String[] saleTagPriceType;
    private String[] saleRange;
    private String[] linkMan;
    private String[] linkManMobile;
    private String[] fundAccount;
    private int[] status;
    private String createdDateStart;
    private String createdDateEnd;
    private String checkDateStart;
    private String checkDateEnd;
    private String updatedDateStart;
    private String updatedDateEnd;
    private String fields;

    private PhysicalRegion physicalRegion;
    private ChannelBarrio channelBarrio;
    private Channelorganization channelorganization;

    private Integer pageNo;
    private Integer pageSize;

}
