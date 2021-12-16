package com.regent.rbp.api.core.sendBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 发货单货品明细
 * @author: HaiFeng
 * @create: 2021-11-16 17:31
 */
@Data
@ApiModel(description = "发货单货品明细表")
@TableName(value = "rbp_send_bill_goods")
public class SendBillGoods {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "指令单号")
    private Long noticeId;

    @ApiModelProperty(notes = "指令单货品明细号")
    private Long noticeGoodsId;

    @ApiModelProperty(notes = "计划单号")
    private Long salePlanId;

    @ApiModelProperty(notes = "计划单货品明细号")
    private Long salePlanGoodsId;

    @ApiModelProperty(notes = "价格类型")
    private Long priceTypeId;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

    public static SendBillGoods build() {
        SendBillGoods item = new SendBillGoods();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        return item;
    }
}
