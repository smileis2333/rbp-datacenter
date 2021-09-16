package com.regent.rbp.api.core.base;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 用户管理
 * @author: HaiFeng
 * @create: 2021-09-15 10:57
 */
@Data
@ApiModel(description = "用户管理")
@TableName(value = "rbp_user")
public class User {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "用户编号")
    private String code;

    @ApiModelProperty(notes = "用户名称")
    private String name;

    @ApiModelProperty(notes = "描述")
    private String notes;

    @ApiModelProperty(notes = "状态")
    private Integer status;

    @ApiModelProperty(notes = "类型")
    private Integer type;

    @ApiModelProperty(notes = "密码")
    private String password;

    @ApiModelProperty(notes = "生效日期")
    private Date beginDate;

    @ApiModelProperty(notes = "失效日期")
    private Date endDate;

    @ApiModelProperty(notes = "性别")
    private Integer sex;

    @ApiModelProperty(notes = "部门")
    private String department;

    @ApiModelProperty(notes = "职位")
    private String position;

    @ApiModelProperty(notes = "手机")
    private String mobile;

    @ApiModelProperty(notes = "生日日期")
    private Date birthdayDate;

    @ApiModelProperty(notes = "入职日期")
    private Date workDate;

    @ApiModelProperty(notes = "邮件")
    private String email;

    @ApiModelProperty(notes = "身份证号")
    private String idCard;

    @ApiModelProperty(notes = "微信号")
    private String weixin;

    @ApiModelProperty(notes = "企业微信号")
    private String qyweixin;

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
}
