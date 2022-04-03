package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 全渠道收退货单明细
 * @author: HaiFeng
 * @create: 2021-10-14 17:33
 */
@Data
@TableName(value = "rbp_retail_receive_back_bill_goods")
public class RetailReceiveBackBillGoods {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

//    @ApiModelProperty(notes = "全渠道订单行号")
//    private Long retailOrderBillGoodsId;

    @ApiModelProperty(notes = "条码")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private Long goodsId;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "尺码")
    private Long sizeId;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

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

    public static RetailReceiveBackBillGoods build() {
        Long userId = ThreadLocalGroup.getUserId();
        RetailReceiveBackBillGoods item = new RetailReceiveBackBillGoods();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setCreatedBy(userId);
        item.setUpdatedBy(userId);
        return item;
    }

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
        this.setUpdatedBy(ThreadLocalGroup.getUserId());
    }

}
