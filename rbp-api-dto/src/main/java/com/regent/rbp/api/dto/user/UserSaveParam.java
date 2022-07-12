package com.regent.rbp.api.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description 用户档案新增
 */
@Data
public class UserSaveParam {

    @JsonIgnore
    private Long id;

    @NotBlank
    @ApiModelProperty(notes = "用户编号")
    private String code;

    @NotBlank
    @ApiModelProperty(notes = "用户名称")
    private String name;

    @ApiModelProperty(notes = "描述")
    private String notes;

    @NotNull
    @DiscreteRange(ranges = {100, 101}, message = "入参非法，合法输入100-启用 101-禁用")
    @ApiModelProperty(notes = "状态;(100-启用 101-禁用)")
    private Integer status;

    @DiscreteRange(ranges = {0, 1, 2, 3}, message = "入参非法，合法输入0-普通用户;1-收银员; 2-管理员;3-下级管理员")
    private Integer type;

    @NotBlank
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

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer workStatus;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long channelId;

    @Email
    @ApiModelProperty(notes = "邮件")
    private String email;

    @ApiModelProperty(notes = "身份证号")
    private String idCard;

    @ApiModelProperty(notes = "微信号")
    private String weixin;

    @ApiModelProperty(notes = "企业微信号")
    private String qyweixin;

    @DiscreteRange(ranges = {0, 1}, message = "入参非法，合法输入0-否1-是")
    @ApiModelProperty(value = "是否收银员0-否1-是")
    private Integer cashierFlag;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Boolean businessManFlag;

    @ApiModelProperty(value = "最低折扣")
    private BigDecimal discount;

    @ApiModelProperty(value = "收银员渠道范围")
    private List<UserCashierChannelDto> cashierChannels;

    @ApiModelProperty(value = "收银员最低折扣")
    private List<UserCashierDiscountDto> cashierDiscount;

    @AssertTrue(message = "收银员标识为“是”时必填")
    private boolean isDiscount() {
        if (cashierFlag != null && cashierFlag == 1) {
            return discount != null;
        }
        return true;
    }
}
