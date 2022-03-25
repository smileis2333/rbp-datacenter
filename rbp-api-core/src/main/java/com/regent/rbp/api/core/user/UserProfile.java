package com.regent.rbp.api.core.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description 用户档案 新增
 */
@Data
@ApiModel(description = "用户档案 ")
@TableName(value = "rbp_user")
public class UserProfile {

    @TableId("id")
    @ApiModelProperty(notes = "ID")
    private Long id;

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
    private Integer cashierFlag;

    @ApiModelProperty(value = "最低折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updatedTime;

    public static UserProfile build() {
        UserProfile user = new UserProfile();
        user.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        user.setCreatedBy(ThreadLocalGroup.getUserId());
        user.setUpdatedBy(ThreadLocalGroup.getUserId());

        return user;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }

}
