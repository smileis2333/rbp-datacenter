package com.regent.rbp.api.core.member;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 会员档案
 * @author: HaiFeng
 * @create: 2021-09-14 14:31
 */
@Data
@ApiModel(description = "会员档案")
@TableName(value = "rbp_member_card")
public class MemberCard {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "卡号")
    private String code;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "姓名")
    private String name;

    @ApiModelProperty(notes = "会员类型编码")
    private Long memberTypeId;

    @ApiModelProperty(notes = "政策")
    private Long memberPolicyId;

    @ApiModelProperty(notes = "区号")
    private String areaCode;

    @ApiModelProperty(notes = "手机")
    private String phone;

    @ApiModelProperty(notes = "性别")
    private Long sexId;

    @ApiModelProperty(notes = "来源类别")
    private Integer originType;

    @ApiModelProperty(notes = "来源")
    private Integer origin;

    @ApiModelProperty(notes = "生效日期")
    private Date beginDate;

    @ApiModelProperty(notes = "失效日期")
    private Date endDate;

    @ApiModelProperty(notes = "发卡渠道")
    private Long channelId;

    @ApiModelProperty(notes = "发卡人")
    private Long userId;

    @ApiModelProperty(notes = "推荐人卡号")
    private String referrerCardNo;

    @ApiModelProperty(notes = "维护渠道")
    private Long repairChannelId;

    @ApiModelProperty(notes = "维护人")
    private Long maintainerId;

    @ApiModelProperty(notes = "拓展人")
    private Long developerId;

    @ApiModelProperty(notes = "国家")
    @TableField(exist = false)
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
    private Date birthdayDate;

    @ApiModelProperty(notes = "生日-年")
    private Integer birthdayYear;

    @ApiModelProperty(notes = "生日-月")
    private Integer birthdayMouth;

    @ApiModelProperty(notes = "生日-日")
    private Integer birthdayDay;

    @ApiModelProperty(notes = "审核状态")
    private Integer status;

    @ApiModelProperty(notes = "会员状态编码")
    private Long memberStatusId;

    @ApiModelProperty(notes = "邮箱")
    private String email;

    @ApiModelProperty(notes = "微信")
    private String weixin;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "更新来源 0.RBP,1.INNO")
    private Integer updatedOrigin;

}
