package com.regent.rbp.api.core.salesOrder;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 销售单货品
 * @author: HaiFeng
 * @create: 2021-11-08 16:47
 */
@Data
@ApiModel(description="销售单货品明细表")
@TableName(value = "rbp_sales_order_bill_goods")
public class SalesOrderBillGoods {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单据编号")
    private Long billId;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

//    @ApiModelProperty(notes = "状态")
//    private Integer status;

//    @ApiModelProperty(notes = "销售类型")
//    private Integer saleType;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "零售价")
    private BigDecimal retailPrice;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal salesPrice;

    @ApiModelProperty(notes = "原始实卖价")
    private BigDecimal originalPrice;

    @ApiModelProperty(notes = "积分")
    private BigDecimal point;

    @ApiModelProperty(notes = "吊牌折扣")
    private BigDecimal tagDiscount;

    @ApiModelProperty(notes = "零售折扣")
    private BigDecimal retailDiscount;

    @ApiModelProperty(notes = "结算折扣")
    private BigDecimal balanceDiscount;

    @ApiModelProperty(notes = "成本价")
    private BigDecimal costPrice;

    @ApiModelProperty(notes = "计划成本价")
    private BigDecimal planCostPrice;

    @ApiModelProperty(notes = "备注")
    private Long notes;

    @ApiModelProperty(notes = "发票税率")
    private BigDecimal invoiceTaxRate;

    @ApiModelProperty(notes = "发票税额")
    private BigDecimal invoiceTaxPrice;

    @ApiModelProperty(notes = "发票不含税额")
    private BigDecimal invoiceNotTaxPrice;

    @ApiModelProperty(notes = "发票分类")
    private BigDecimal invoiceTaxType;

    @ApiModelProperty(notes = "券分摊金额")
    private BigDecimal couponShareAmount;

    @ApiModelProperty(notes = "创建人")
    private Long createdBy;

    @ApiModelProperty(notes = "创建时间")
    private Date createdTime;

    @ApiModelProperty(notes = "更新人")
    private Long updatedBy;

    @ApiModelProperty(notes = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(notes = "类型 (0-销售 1-退货 2-换货 3-发货)")
    private Integer type;

    @ApiModelProperty(notes = "销售模式 (0-零售 1-团购 2-内购 3-赠送)")
    private Integer saleModel;

    @ApiModelProperty(notes = "属性 (0-现货 1-全渠道 2-预售)")
    private Integer attribute;

    @ApiModelProperty(notes = "交付方式 (0-库存 1-直发 2-自提)")
    private Integer deliveryMethod;

    @ApiModelProperty(notes = "库存模式 (0-本店 1-寻源)")
    private Integer stockModel;

}
