package com.regent.rbp.api.core.noticeBill;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 指令单货品明细表
 * @author: HaiFeng
 * @create: 2021-11-17 10:38
 */
@Data
@ApiModel(description = "指令单货品明细表")
@TableName(value = "rbp_notice_bill_goods")
public class NoticeBillGoods {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "计划单号")
    @TableField(exist = false)
    private Long salePlanId;

    @ApiModelProperty(notes = "计划单货品明细号")
    private Long salePlanGoodsId;

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

    @ApiModelProperty(notes = "尺码明细")
    @TableField(exist = false)
    List<NoticeBillSize> sizeList;

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> customFieldMap;

    public static NoticeBillGoods build() {
        NoticeBillGoods item = new NoticeBillGoods();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        return item;
    }
}
