package com.regent.rbp.api.service.member.context;

import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 会员 查询上下文对象
 * @author: HaiFeng
 * @create: 2021-09-14 14:52
 */
@Data
public class MemberCardQueryContext {

    private String[] code;
    private String name;
    private String areaCode;
    private String phone;
    private long[] sexCode;
    private int[] originType;
    private int[] origin;
    private int[] status;
    private Date beginDateStart;
    private Date beginDateEnd;
    private Date endDateStart;
    private Date endDateEnd;
    private long[] channelCode;
    private Long userCode;
    private String referrerCardNo;
    private long[] repairChannelCode;
    private Long maintainerCode;
    private Long developerCode;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private String fields;

    private Integer pageNo;
    private Integer pageSize;

}
