package com.regent.rbp.api.dto.member;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(notes = "卡号")
    private String code;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "姓名")
    private String name;

    @ApiModelProperty(notes = "会员类型")
    private String memberType;

    @ApiModelProperty(notes = "区号")
    private String areaCode;

    @ApiModelProperty(notes = "手机")
    private String phone;

    @ApiModelProperty(notes = "性别名称")
    private String sexName;

    @ApiModelProperty(notes = "来源类别。(1-线上，2-线下，3-后台)")
    private String originType;

    @ApiModelProperty(notes = "来源。（1-pos，2-英朗，3-微盟，4-有赞，5-yike）")
    private String origin;

    @ApiModelProperty(notes = "生效日期。相当于开卡日期")
    private String beginDate;

    @ApiModelProperty(notes = "失效日期")
    private String endDate;

    @ApiModelProperty(notes = "发卡渠道编号")
    private String channelCode;

    @ApiModelProperty(notes = "发卡人编号")
    private String userCode;

    @ApiModelProperty(notes = "推荐人卡号")
    private String referrerCardNo;

    @ApiModelProperty(notes = "维护渠道编号")
    private String repairChannelCode;

    @ApiModelProperty(notes = "维护人编号")
    private String maintainerCode;

    @ApiModelProperty(notes = "拓展人编号")
    private String developerCode;

    @ApiModelProperty(notes = "国家")
    private String nation;

    @ApiModelProperty(notes = "省")
    private String province;

    @ApiModelProperty(notes = "市")
    private String city;

    @ApiModelProperty(notes = "区")
    private String area;

    @ApiModelProperty(notes = "地址")
    private String address;

    @ApiModelProperty(notes = "生日")
    private String birthday;

    @ApiModelProperty(notes = "生日-年")
    private Integer birthdayYear;

    @ApiModelProperty(notes = "生日-月")
    private Integer birthdayMouth;

    @ApiModelProperty(notes = "生日-日")
    private Integer birthdayDay;

    @ApiModelProperty(notes = "会员状态")
    private String memberStatus;

    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "微信")
    private String weixin;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "会员政策编号")
    private String memberPolicyCode;

    @ApiModelProperty(notes = "更新来源 0.RBP,1.INNO")
    private Integer updatedOrigin;

    @ApiModelProperty(notes = "微信unionId")
    private String unionId;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> customizeData;

}
