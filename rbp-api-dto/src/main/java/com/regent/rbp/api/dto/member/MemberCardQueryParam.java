package com.regent.rbp.api.dto.member;

import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 查询请求
 * @author: HaiFeng
 * @create: 2021-09-14 14:18
 */
@Data
public class MemberCardQueryParam {

    private String[] code;
    private String name;
    private String areaCode;
    private String phone;
    private Integer sex;
    private int[] originType;
    private int[] origin;
    private int[] status;
    private String beginDateStart;
    private String beginDateEnd;
    private String endDateStart;
    private String endDateEnd;
    private String[] channelCode;
    private String userCode;
    private String referrerCardNo;
    private String[] repairChannelCode;
    private String maintainerCode;
    private String developerCode;
    private String createdDateStart;
    private String createdDateEnd;
    private String checkDateStart;
    private String checkDateEnd;
    private String updatedDateStart;
    private String updatedDateEnd;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;
}
