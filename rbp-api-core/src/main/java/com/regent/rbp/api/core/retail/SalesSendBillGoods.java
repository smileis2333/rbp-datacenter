package com.regent.rbp.api.core.retail;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Data
@ApiModel(description="发货销售单货品详情")
@TableName(value = "rbp_sales_send_bill_goods")
public class SalesSendBillGoods {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编号")
    private Long billId;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "货品编号")
    @TableField(exist = false)
    private String goodsCode;

    @ApiModelProperty(notes = "品名")
    @TableField(exist = false)
    private String goodsName;

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

    @ApiModelProperty(notes = "吊牌折扣 原始实卖价/吊牌价")
    private BigDecimal tagDiscount;

    @ApiModelProperty(notes = "零售折扣 零售价/吊牌价")
    private BigDecimal retailDiscount;

    @ApiModelProperty(notes = "结算折扣 结算价/吊牌价")
    private BigDecimal balanceDiscount;

    @ApiModelProperty(notes = "成本价")
    private BigDecimal costPrice;

    @ApiModelProperty(notes = "计划成本价")
    private BigDecimal planCostPrice;

    @ApiModelProperty(notes = "备注")
    private String notes;

    @ApiModelProperty(notes = "发票税率")
    private BigDecimal invoiceTaxRate;

    @ApiModelProperty(notes = "发票税额")
    private BigDecimal invoiceTaxPrice;

    @ApiModelProperty(notes = "发票不含税额")
    private BigDecimal invoiceNotTaxPrice;

    @ApiModelProperty(notes = "发票分类")
    private String invoiceTaxType;

    @ApiModelProperty(notes = "券分摊金额")
    private BigDecimal couponShareAmount;

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
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }
}
