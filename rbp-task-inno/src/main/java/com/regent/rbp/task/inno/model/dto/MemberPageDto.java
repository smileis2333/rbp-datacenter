package com.regent.rbp.task.inno.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 会员明细
 * @author: HaiFeng
 * @create: 2021-10-19 14:25
 */
@Data
public class MemberPageDto {

    @ApiModelProperty(notes = "用户ID")
    private String id;

    @ApiModelProperty(notes = "用户卡号")
    private String card_no;

    @ApiModelProperty(notes = "用户简称")
    private String nick_name;

    @ApiModelProperty(notes = "邮编")
    private String email;

    @ApiModelProperty(notes = "性别（男，女）")
    private String sex;

    @ApiModelProperty(notes = "生日（时间标准化：2015-01-01 00:00:01）")
    private String birthday;

    @ApiModelProperty(notes = "手机号码")
    private String mobile_no;

    @ApiModelProperty(notes = "用户创建时间（时间标准化：2015-01-01 00:00:01）")
    private String create_date;

    @ApiModelProperty(notes = "用户修改时间（时间标准化：2015-01-01 00:00:01）")
    private String modify_date;

    @ApiModelProperty(notes = "店铺编码")
    private String store_code;

    @ApiModelProperty(notes = "会员号")
    private String user_name;

    @ApiModelProperty(notes = "店铺员工编号")
    private String staff_code;

    @ApiModelProperty(notes = "等级名称")
    private String rank_name;

    @ApiModelProperty(notes = "等级代码")
    private String rank_code;

    @ApiModelProperty(notes = "微信的openid")
    private String wap_openid;

    @ApiModelProperty(notes = "状态是否可用，1：可用，0：不可用")
    private Integer is_enabled;

    @ApiModelProperty(notes = "用户转移之前的卡号")
    private String related_card_num;

    @ApiModelProperty(notes = "是否关注微信，1：已关注，0：未关注")
    private Integer is_subscribe_wechat;

    @ApiModelProperty(notes = "微信的unionid")
    private String Unionid;

    @ApiModelProperty(notes = "支付密码")
    private String pay_password;

    @ApiModelProperty(notes = "是否完善资料，0未完善，1已完善")
    private Integer profile_modify;

    @ApiModelProperty(notes = "小程序openid")
    private String applet_openid;

    @ApiModelProperty(notes = "会员合并时间，只有 related_card_num不为空的时候才有")
    private String transferTime;

    @ApiModelProperty(notes = "会员头像")
    private String portrait_path;

    @ApiModelProperty(notes = "所属省份")
    private String province_name;

    @ApiModelProperty(notes = "所属城市")
    private String city_name;

    @ApiModelProperty(notes = "所属地区")
    private String district_name;

    @ApiModelProperty(notes = "详细地址")
    private String address;

}
