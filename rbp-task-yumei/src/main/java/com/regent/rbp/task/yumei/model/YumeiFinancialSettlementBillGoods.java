package com.regent.rbp.task.yumei.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
@Data
@TableName(value = "yumei_financial_settlement_bill_goods")
public class YumeiFinancialSettlementBillGoods {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "金额")
    private BigDecimal amount;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "货品编号")
    @TableField(exist = false)
    private String goodsCode;

    @ApiModelProperty(notes = "货品名称")
    @TableField(exist = false)
    private String goodsName;

    @ApiModelProperty(notes = "颜色")
    private Long colorId;

    @ApiModelProperty(notes = "颜色编号")
    @TableField(exist = false)
    private String colorNo;

    @ApiModelProperty(notes = "颜色名称")
    @TableField(exist = false)
    private String colorName;

    @ApiModelProperty(notes = "内长")
    private Long longId;

    @ApiModelProperty(notes = "内长名称")
    @TableField(exist = false)
    private String longName;

    @ApiModelProperty(notes = "尺码id, 就是size_detail_id")
    private Long sizeId;

    @ApiModelProperty(notes = "尺码名称")
    @TableField(exist = false)
    private String sizeName;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "备注")
    private String remark;
}
