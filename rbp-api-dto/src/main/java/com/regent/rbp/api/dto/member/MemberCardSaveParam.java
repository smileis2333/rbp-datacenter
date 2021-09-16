package com.regent.rbp.api.dto.member;

import com.regent.rbp.api.dto.base.CustomizeData;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 新增/修改
 * @author: HaiFeng
 * @create: 2021-09-15 16:31
 */
@Data
public class MemberCardSaveParam {

    private String code;
    private String password;
    private String name;
    private String memberType;
    private String areaCode;
    private String phone;
    private String sexName;
    private String originType;
    private String origin;
    private String beginDate;
    private String endDate;
    private String channelCode;
    private String userCode;
    private String referrerCardNo;
    private String repairChannelCode;
    private String maintainerCode;
    private String developerCode;
    private String nation;
    private String province;
    private String city;
    private String area;
    private String address;
    private String birthday;
    private Integer birthdayYear;
    private Integer birthdayMouth;
    private Integer birthdayDay;
    private String memberStatus;
    private String email;
    private String weixin;
    private String notes;

    private List<CustomizeData> customizeData;

}
