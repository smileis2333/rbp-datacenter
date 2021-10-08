package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单操作日志
 * @author: HaiFeng
 * @create: 2021-09-27 13:40
 */
@Data
@TableName(value = "rbp_retail_return_notice_bill_operator_log")
public class RetailReturnNoticeBillOperatorLog {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "模块编码")
    private String moduleId;

    @ApiModelProperty(notes = "操作状态 (0.失败;1.成功;)")
    private Integer status;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "单据编号")
    private String billNo;

    @ApiModelProperty(notes = "上级单据编码")
    private Long preBillId;

    @ApiModelProperty(notes = "上级单据编号")
    private String preBillNo;

    @ApiModelProperty(notes = "操作类型")
    private String actionCode;

    @ApiModelProperty(notes = "操作名称")
    private String actionName;

    @ApiModelProperty(notes = "操作人")
    private Long operateBy;

    @ApiModelProperty(notes = "操作人名称")
    private String operateByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "操作时间")
    private Date operateTime;

    @ApiModelProperty(notes = "线上订单号")
    private String onlineOrderCode;

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

}
