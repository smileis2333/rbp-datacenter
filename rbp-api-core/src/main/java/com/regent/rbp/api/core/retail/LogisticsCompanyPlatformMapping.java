package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-04-12
 * @Description
 */
@Data
@TableName(value = "rbp_logistics_company_platform_mapping")
public class LogisticsCompanyPlatformMapping {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "物流公司编码")
    private Long logisticsCompanyId;

    @ApiModelProperty(notes = "平台类型 关联电商平台类型表rbp_online_platform_type.id")
    private Integer onlinePlatformTypeId;

    @ApiModelProperty(notes = "平台物流编号")
    private String onlinePlatformLogisticsCode;

    @ApiModelProperty(notes = "平台物流名称")
    private String onlinePlatformLogisticsName;

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
