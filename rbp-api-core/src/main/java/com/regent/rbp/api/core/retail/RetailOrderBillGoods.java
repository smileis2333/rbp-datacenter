package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.regent.rbp.infrastructure.util.MD5Util;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 全渠道订单货品明细 对象 rbp_retail_order_bill_goods
 *
 * @author chenchungui
 * @date 2021-09-14
 */
@Data
@ApiModel(description = "全渠道订单货品明细 ")
@TableName(value = "rbp_retail_order_bill_goods")
public class RetailOrderBillGoods {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "销售单明细编码")
    private Long salesOrderBillGoodsId;

    @ApiModelProperty(notes = "销售类型 0-销售、1-现货直发、2-现货自提、3-预售直发、4-预售自提")
    private Integer saleType;

    @ApiModelProperty(notes = "货品类型 0-普通、1-赠品、2-换货")
    private Integer retailGoodsType;

    @ApiModelProperty(notes = "货品处理状态 (0.未处理;1.已配货;2.已打印;3.已验货;4.已发货;5.已作废;6.换货中;7.已换货)")
    private Integer processStatus;

    @ApiModelProperty(notes = "货品退款状态 (0.无;1.待同意;2.待退货;3.拒绝退款;4.退款成功;5.退款关闭)")
    private Integer refundStatus;

    @ApiModelProperty(notes = "货品退货状态 (0.无;1.已退货)")
    private Integer returnStatus;

    @ApiModelProperty(notes = " 货品线上状态 0-等待买家付款、1-已付款+货到付款、2-货到付款等待发货、3-买家已付款、4-卖家部分发货、5-卖家已发货、6-买家已签收、7-买家拒签、8-交易成功、9-交易关闭")
    private Integer onlineStatus;

    @ApiModelProperty(notes = "货品售后状态 (0.无;1.待处理;2.同意退款;3.同意退货;4.拒绝退款;5.拒绝退货;6.仓库已收货;7.仓库已拒收;8.换货中;9.换货已发货;10.换货部分发货;11.作废)")
    private Integer afterSaleProcessStatus;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "条形码")
    private String barcode;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "尺码id, 就是size_detail_id")
    private Long sizeId;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

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

    @JsonIgnoreProperties
    public String getSingleCode() {
        return MD5Util.shortenKeyString(this.goodsId, this.longId, this.colorId, this.sizeId);
    }

    public static RetailOrderBillGoods build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailOrderBillGoods item = new RetailOrderBillGoods();

        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

    public void preUpdate() {
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }
}
