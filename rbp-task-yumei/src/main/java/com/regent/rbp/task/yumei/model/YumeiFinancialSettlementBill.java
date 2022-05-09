package com.regent.rbp.task.yumei.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@Data
@TableName(value = "yumei_financial_settlement_bill")
public class YumeiFinancialSettlementBill {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "资金号编码")
    private Long fundAccountId;

    @ApiModelProperty(notes = "单据状态 (0.未审核,1.已审核,2.反审核,3.已作废,4.已验货,5.已发货,6.已超时)")
    private Integer status;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;

    @ApiModelProperty(notes = "币种名称")
    @TableField(exist = false)
    private String currencyTypeName;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "线上单号")
    private String onlineOrderCode;

    @ApiModelProperty(notes = "货品总件数")
    private BigDecimal sumSkuQuantity;

    @ApiModelProperty(notes = "货品吊牌额")
    private BigDecimal sumTagAmount;

    @ApiModelProperty(notes = "货品结算额")
    private BigDecimal sumAmount;

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
