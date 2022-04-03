package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
@ApiModel(description = "发货销售单")
@TableName(value = "rbp_sales_send_bill")
public class RetailSalesSendBill {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "全渠道配货单ID")
    private Long retailDistributionBillId;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "状态 (0.未审核,1.已审核,2.反审核,3.已作废)")
    private Integer status;
    @ApiModelProperty(notes = "状态 (0.未审核,1.已审核,2.反审核,3.已作废)")
    @TableField(exist = false)
    private String statusName;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;
    @ApiModelProperty(notes = "渠道名称")
    @TableField(exist = false)
    private String channelName;

    @ApiModelProperty(notes = "渠道名称")
    @TableField(exist = false)
    private String channelCode;
    /**
     * 班次编码
     */
    @ApiModelProperty(notes = "班次编码")
    private Long shiftId;

    /**
     * 会员编码
     */
    @ApiModelProperty(notes = "会员编码")
    private Long memberId;

    /**
     * 收货标识
     */
    @ApiModelProperty(notes = "收货标识")
    private Boolean receiveGoodsFlag;

    /**
     * 会员卡号
     */
    @ApiModelProperty(notes = "会员卡号")
    @TableField(exist = false)
    private String memberCardNo;

    /**
     * 货品总件数
     */
    @ApiModelProperty(notes = "货品总件数")
    private BigDecimal sumSkuQuantity;

    /**
     * 货品总款数
     */
    @ApiModelProperty(notes = "货品总款数")
    private BigDecimal sumItemQuantity;

    /**
     * 货品零售额
     */
    @ApiModelProperty(notes = "货品零售额")
    private BigDecimal sumRetailAmount;

    /**
     * 货品原始实卖额
     */
    @ApiModelProperty(notes = "货品原始实卖额（改为生意额）")
    private BigDecimal sumOriginalAmount;

    /**
     * 货品吊牌额
     */
    @ApiModelProperty(notes = "货品吊牌额")
    private BigDecimal sumTagAmount;

    /**
     * 货品生意额
     */
    @ApiModelProperty(notes = "货品生意额（改为计收额）")
    private BigDecimal sumSalesAmount;

    /**
     * 找零金额
     */
    @ApiModelProperty(notes = "找零金额")
    private BigDecimal returnMoney;

    /**
     * 获得积分
     */
    @ApiModelProperty(notes = "获得积分")
    private BigDecimal gotPoint;

    /**
     * 机器唯一码
     */
    @ApiModelProperty(notes = "机器唯一码")
    private String deviceId;

    /**
     * 机器类型 (1.WEB,2.ANDROID3.IOS
     */
    @ApiModelProperty(notes = "机器类型 (1.WEB,2.ANDROID3.IOS")
    private Integer deviceType;

    @ApiModelProperty(notes = "机器类型名称 (1.WEB,2.ANDROID3.IOS")
    @TableField(exist = false)
    private String deviceTypeName;

    /**
     * 机器编码
     */
    @ApiModelProperty(notes = "机器编码")
    private Long machineNum;

    /**
     * 来源
     */
    @ApiModelProperty(notes = "来源 (0.Pos;1.后台;2.第三方平台)")
    private Integer origin;


    @ApiModelProperty(notes = "来源名称")
    @TableField(exist = false)
    private String originName;

    /**
     * 单据时间
     */
    @ApiModelProperty(notes = "单据时间")
    private Date billTime;

    /**
     * 原单编码
     */
    @ApiModelProperty(notes = "原单编码")
    private Long originBillId;

    /**
     * 原单号
     */
    @ApiModelProperty(notes = "原单号")
    private String originBillNo;

    /**
     * 销售渠道编码
     */
    @ApiModelProperty(notes = "销售渠道编码")
    private Long saleChannelId;

    /**
     * 统计口径 0.销售
     */
    @ApiModelProperty(notes = "统计口径 0.销售")
    private Integer statType;

    @ApiModelProperty(notes = "统计口径名称 0.销售")
    @TableField(exist = false)
    private String statTypeName;

    /**
     * 业务类型
     */
    @ApiModelProperty(notes = "业务类型")
    private Long businessType;

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
     * 失效人
     */
    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效人名称")
    @TableField(exist = false)
    private String cancelByName;

    /**
     * 失效时间
     */
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    /**
     * 反审核人
     */
    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @ApiModelProperty(notes = "反审核人名称")
    @TableField(exist = false)
    private String uncheckByName;

    /**
     * 反审核时间
     */
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

    /**
     * 货品
     */
    @ApiModelProperty(notes = "销售单货品")
    @TableField(exist = false)
    private List<SalesSendBillGoods> salesSendBillGoodsList;
}
