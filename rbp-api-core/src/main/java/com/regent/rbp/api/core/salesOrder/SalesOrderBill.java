package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单
 * @author: HaiFeng
 * @create: 2021-11-08 15:34
 */
@Data
@ApiModel(description="销售单主表")
@TableName(value = "rbp_sales_order_bill")
public class SalesOrderBill {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "班次编码")
    private Long shiftId;

    @ApiModelProperty(notes = "会员编码")
    private Long memberId;

    @ApiModelProperty(notes = "货品总件数")
    private BigDecimal sumSkuQuantity;

    @ApiModelProperty(notes = "货品总款数")
    private BigDecimal sumItemQuantity;

    @ApiModelProperty(notes = "货品零售额")
    private BigDecimal sumRetailAmount;

    @ApiModelProperty(notes = "货品原始实卖额")
    private BigDecimal sumOriginalAmount;

    @ApiModelProperty(notes = "货品吊牌额")
    private BigDecimal sumTagAmount;

    @ApiModelProperty(notes = "货品生意额")
    private BigDecimal sumSalesAmount;

    @ApiModelProperty(notes = "找零金额")
    private BigDecimal returnMoney;

    @ApiModelProperty(notes = "获得积分")
    private BigDecimal gotPoint;

    @ApiModelProperty(notes = "单据状态")
    private Integer status;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "机器唯一码")
    private String deviceId;

    @ApiModelProperty(notes = "机器类型")
    private Integer deviceType;

    @ApiModelProperty(notes = "机器编码")
    private Integer machineNum;

    @ApiModelProperty(notes = "来源")
    private Integer origin;

    @ApiModelProperty(notes = "单据时间")
    private Date billTime;

    @ApiModelProperty(notes = "原单编码")
    private Long originBillId;

    @ApiModelProperty(notes = "原单号")
    private String originBillNo;

    @ApiModelProperty(notes = "单据类型")
    private Integer billType;

    @ApiModelProperty(notes = "销售渠道编码")
    private Long saleChannelId;

    @ApiModelProperty(notes = "统计口径")
    private Integer statType;

    @ApiModelProperty(notes = "全渠道配单编码")
    private Long retailDistributionBillId;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "审核人")
    private Long checkBy;

    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "收货标记")
    private Integer receiveGoodsFlag;

}
