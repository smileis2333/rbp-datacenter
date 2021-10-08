package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单
 * @author: HaiFeng
 * @create: 2021-09-26 16:47
 */
@Data
@TableName(value = "rbp_retail_return_notice_bill")
public class RetailReturnNoticeBill {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "销售渠道编码")
    private Long saleChannelId;

    @ApiModelProperty(notes = "收货渠道编码")
    private Long receiveChannelId;

    @ApiModelProperty(notes = "快递公司")
    private Long logisticsCompanyId;

    @ApiModelProperty(notes = "快递单号")
    private String logisticsBillCode;

    @ApiModelProperty(notes = "全渠道订单号")
    private Long retailOrderBillId;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废,4.已配货,5.已发货)")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "完结状态")
    private Integer finishFlag;

    @ApiModelProperty(notes = "审批状态;(0.待审批;1.审批中;2.已驳回;3.已通过;)")
    private Integer flowStatus;

    @ApiModelProperty(notes = "流程类型;(1.审核流程;2.反审核流程;3.作废流程;)")
    private Integer flowType;

    @ApiModelProperty(notes = "流程实例编码")
    private String flowId;

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

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    public static RetailReturnNoticeBill build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailReturnNoticeBill item = new RetailReturnNoticeBill();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }

}
