package com.regent.rbp.api.dto.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 查询返回
 * @author: HaiFeng
 * @create: 2021-09-14 13:37
 */
@Data
public class MemberCardQueryResult {

    @JsonIgnore
    private Long memberCardId;
    private String code;
    private String password;
    private String name;
    private Long memberTypeId;
    private String memberTypeName;
    private Long memberPolicyId;
    private String memberPolicyName;
    private String areaCode;
    private String phone;
    private Integer sexId;
    private String sexName;
    private Integer originType;
    private String originTypeName;
    private Integer origin;
    private String originName;
    private Date beginDate;
    private Date endDate;
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
    private Integer status;
    private String statusName;
    private String memberStatus;
    private String email;
    private String weixin;
    private String notes;
    private String createdBy;
    private Date createdTime;
    private String updatedBy;
    private Date updatedTime;
    private String checkBy;
    private Date checkTime;
    private String cancelBy;
    private Date cancelTime;
    private String uncheckBy;
    private Date uncheckTime;

    private List<CustomizeDataDto> customizeData;

    public void setOriginType(Integer originType) {
        this.originType = originType;
        String originTypeName = "";
        switch (originType) {
            case 1:
                originTypeName = "线上";
                break;
            case 2:
                originTypeName = "线下";
                break;
            case 3:
                originTypeName = "后台";
                break;
        }
        this.originTypeName = originTypeName;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
        String originName = "";
        switch (origin) {
            case 1:
                originName = "pos";
                break;
            case 2:
                originName = "英朗";
                break;
            case 3:
                originName = "微盟";
                break;
            case 4:
                originName = "有赞";
                break;
            case 5:
                originName = "yike";
                break;
        }
        this.originName = originName;
    }

    public void setStatus(Integer status) {
        this.status = status;
        String name = "";
        switch (status) {
            case 0:
                name = "";
                break;
            case 1:
                name = "已审核";
                break;
            case 2:
                name = "反审核";
                break;
            case 3:
                name = "已作废";
                break;
        }
        this.statusName = name;
    }


}
