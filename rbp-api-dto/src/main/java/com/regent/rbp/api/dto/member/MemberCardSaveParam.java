package com.regent.rbp.api.dto.member;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 新增/修改
 * @author: HaiFeng
 * @create: 2021-09-15 16:31
 */
@Data
public class MemberCardSaveParam {

    @NotBlank
    private String code;

    private String password;

    private String name;

    @NotBlank
    private String memberType;

    private String areaCode;

    private String phone;

    private String sexName;

    @NotNull
    @DiscreteRange(ranges = {1, 2, 3}, message = "入参非法，合法输入1-线上，2-线下，3-后台")
    private Integer originType;

    @NotNull
    @DiscreteRange(ranges = {1, 2, 3, 4, 5}, message = "入参非法，合法输入1-pos，2-英朗，3-微盟，4-有赞，5-yike")
    private Integer origin;

    private String beginDate;

    private String endDate;

    @ChannelCodeCheck
    private String channelCode;

    private String userCode;

    private String referrerCardNo;

    @ChannelCodeCheck
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

    private String memberPolicyCode;

    private Integer updatedOrigin;

    private String unionId;

    private List<CustomizeDataDto> customizeData;

}
