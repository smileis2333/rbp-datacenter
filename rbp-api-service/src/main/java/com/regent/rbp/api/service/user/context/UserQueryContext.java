package com.regent.rbp.api.service.user.context;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description 用户档案 查询
 */
@Data
public class UserQueryContext {

    @ApiModelProperty(notes = "用户编号")
    private String code;

    @ApiModelProperty(notes = "用户名称")
    private String name;

    @ApiModelProperty(notes = "描述")
    private String notes;

    @ApiModelProperty(notes = "状态;(100-启用 101-禁用)")
    private Integer[] status;

    @ApiModelProperty(notes = "类型;(0-员工 1-管理员")
    private Integer[] type;

    @ApiModelProperty(notes = "性别;(0.男1.女)")
    private Integer[] sex;

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
    private Integer[] cashierFlag;

    @ApiModelProperty(notes = "创建日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateStart;

    @ApiModelProperty(notes = "创建日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDateEnd;

    @ApiModelProperty(notes = "修改日期(开始日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateStart;

    @ApiModelProperty(notes = "修改日期(截止日期)")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedDateEnd;

    @ApiModelProperty(notes = "需返回的字段列表")
    private String fields;

    @ApiModelProperty(notes = "页码：默认1")
    private Integer pageNo;

    @ApiModelProperty(notes = "每页条数：默认100条")
    private Integer pageSize;

}
