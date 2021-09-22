package com.regent.rbp.api.service.channel.context;

import com.regent.rbp.api.dto.channel.ChannelBarrio;
import com.regent.rbp.api.dto.channel.Channelorganization;
import com.regent.rbp.api.dto.channel.PhysicalRegion;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道查询上下文对象
 * @author: HaiFeng
 * @create: 2021-09-11 13:44
 */
@Data
public class ChannelQueryContext {

    private String[] channelCode;
    private String channelName;
    private String channelFullName;
    private String[] channelAddress;
    private long[] brand;
    private long[] branchCompany;
    private long[] grade;
    private long[] businessFormat;
    private long[] businessNature;
    private long[] balanceType;
    private long[] retailTagPriceType;
    private long[] saleTagPriceType;
    private long[] saleRange;
    private String[] linkMan;
    private String[] linkManMobile;
    private long[] fundAccount;
    private int[] status;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private String fields;

    private PhysicalRegion physicalRegion;
    private ChannelBarrio channelBarrio;
    private Channelorganization channelorganization;

    private Integer pageNo;
    private Integer pageSize;
}
