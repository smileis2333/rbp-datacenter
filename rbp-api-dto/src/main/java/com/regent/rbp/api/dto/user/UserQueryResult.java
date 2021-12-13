package com.regent.rbp.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description 用户档案 查询
 */
@Data
public class UserQueryResult {

    @ApiModelProperty(notes = "用户编号")
    private String code;

    @ApiModelProperty(notes = "用户名称")
    private String name;

    @ApiModelProperty(notes = "描述")
    private String notes;

    @ApiModelProperty(notes = "状态;(100-启用 101-禁用)")
    private Integer status;

    @ApiModelProperty(notes = "类型;(0-员工 1-管理员")
    private Integer type;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "性别;(0.男1.女)")
    private Integer sex;

    @ApiModelProperty(notes = "部门")
    private String department;

    @ApiModelProperty(notes = "职位")
    private String position;

    @ApiModelProperty(notes = "手机")
    private String mobile;

    @ApiModelProperty(notes = "生日日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdayDate;

    @ApiModelProperty(notes = "入职日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date workDate;

    @ApiModelProperty(notes = "邮件")
    private String email;

    @ApiModelProperty(notes = "身份证号")
    private String idCard;

    @ApiModelProperty(notes = "微信号")
    private String weixin;

    @ApiModelProperty(notes = "企业微信号")
    private String qyweixin;

    @ApiModelProperty(value = "是否收银员0-否1-是")
    private Integer cashierTag;

    @ApiModelProperty(value = "最低折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "收银员渠道范围")
    private List<UserCashierChannelDto> cashierChannels;

    @ApiModelProperty(value = "收银员最低折扣")
    private List<UserCashierDiscountDto> cashierDiscount;

}
