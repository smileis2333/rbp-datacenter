package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 物流公司档案 对象 rbp_logistics_company
 * 
 * @author czw
 * @date 2020-04-20
 */
@Data
@ApiModel(description="物流公司档案 ")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_logistics_company")
public class LogisticsCompany {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "物流公司编号")
    private String code;


    @ApiModelProperty(notes = "物流公司名称")
    private String name;


    @ApiModelProperty(notes = "联系人")
    private String contactsPerson;


    @ApiModelProperty(notes = "联系电话")
    private String mobile;


    @ApiModelProperty(notes = "传真")
    private String fax;


    @ApiModelProperty(notes = "材料费用")
    private BigDecimal materialCost;


    @ApiModelProperty(notes = "默认打印机")
    private String defaultPrinter;


    @ApiModelProperty(notes = "单号长度")
    private Integer billLength;


    @ApiModelProperty(notes = "网址")
    private String webSite;


    @ApiModelProperty(notes = "备注")
    private String notes;
    
    @ApiModelProperty(notes = "状态,100:启用 101:禁用")
    private Integer status;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建人名称")
    @TableField(exist = false)
    private String createdByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 数据库默认时间
     */
    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新人名称")
    @TableField(exist = false)
    private String updatedByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "更新时间")
    /**
     * 数据库默认时间
     */
    private Date updatedTime;

    /**
     * 插入之前执行方法，子类实现
     */
    public void preInsert() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setCreatedBy(ThreadLocalGroup.getUserId());
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setCreatedTime(date);
        setUpdatedTime(date);
    }

    public void preUpdate() {
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }
}
