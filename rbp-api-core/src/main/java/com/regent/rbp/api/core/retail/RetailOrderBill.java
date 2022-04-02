package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * @Description 全渠道订单
 * @Author chenchungui
 * @Date 2021-09-14
 **/
@Data
@TableName(value = "rbp_retail_order_bill")
public class RetailOrderBill {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "模块编号")
    private String moduleId;

    @ApiModelProperty(notes = "提货码")
    private String acceptGoodsCode;

    @ApiModelProperty(notes = "单号")
    private String billNo;

    @ApiModelProperty(notes = "线上订单号")
    private String onlineOrderCode;

    @ApiModelProperty(notes = "销售单编码")
    private Long salesOrderBillId;

    @ApiModelProperty(notes = "单据日期")
    private Date billDate;

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "渠道编码")
    private Long channelId;

    @ApiModelProperty(notes = "营业员编码")
    private Long employeeId;

    @ApiModelProperty(notes = "线上状态 0-等待买家付款、1-已付款+货到付款、2-货到付款等待发货、3-买家已付款、4-卖家部分发货、5-卖家已发货、6-买家已签收、7-买家拒签、8-交易成功、9-交易关闭")
    private Integer onlineStatus;

    @ApiModelProperty(notes = "平台类型 关联online_platform_type.id")
    private Integer onlinePlatformTypeId;

    @ApiModelProperty(notes = "电商平台 关联rbp_online_platform.id")
    private Long onlinePlatformId;

    @ApiModelProperty(notes = "手工单号")
    private String manualId;

    @ApiModelProperty(notes = "买家备注")
    private String buyerNotes;

    @ApiModelProperty(notes = "卖家备注")
    private String sellerNotes;

    @ApiModelProperty(notes = "打印备注")
    private String printNotes;

    @ApiModelProperty(notes = "客服备注")
    private String notes;

    @ApiModelProperty(notes = "单据状态;(0.未审核,1.已审核,2.反审核,3.已作废,4.已配货,5.已发货)")
    private Integer status;

    @ApiModelProperty(notes = "配货状态 (0.未配货;1.部分配货;2.已配货)")
    private Integer distributionStatus;

    @ApiModelProperty(notes = "发货状态 (0.未发货;1.部分发货;2.已发货)")
    private Integer sendStatus;

    @ApiModelProperty(notes = "支付状态 (0.未支付;1.已支付)")
    private Integer payStatus;

    @ApiModelProperty(notes = "售后处理状态 0-无、1-待处理、2-同意退款、3-同意退货、4-拒绝退款、5-拒绝退货、6-仓库已收货、7-仓库已拒收、8-换货中、9-换货已发货、10-换货部分发货、11-作废")
    private Integer afterSaleProcessStatus;

    @ApiModelProperty(notes = "退款状态 (0.无;1.待同意;2.待退货;3.拒绝退款;4.退款成功;5.退款关闭)")
    private Integer refundStatus;

    @ApiModelProperty(notes = "失效人")
    private Long cancelBy;

    @ApiModelProperty(notes = "失效人名称")
    @TableField(exist = false)
    private String cancelByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "失效时间")
    private Date cancelTime;

    @ApiModelProperty(notes = "反审核人")
    private Long uncheckBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "反审核时间")
    private Date uncheckTime;

    @ApiModelProperty(notes = "反审核人名称")
    @TableField(exist = false)
    private String uncheckByName;

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

    @ApiModelProperty(notes = "审核人名称")
    @TableField(exist = false)
    private String checkByName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "审核时间")
    private Date checkTime;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

    public static RetailOrderBill build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailOrderBill item = new RetailOrderBill();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
