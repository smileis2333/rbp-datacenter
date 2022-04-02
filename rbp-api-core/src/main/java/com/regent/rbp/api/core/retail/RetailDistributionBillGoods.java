package com.regent.rbp.api.core.retail;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@ApiModel(description = "全渠道配单货品明细 ")
@TableName(value = "rbp_retail_distribution_bill_goods")
public class RetailDistributionBillGoods {

    @ApiModelProperty(notes = "ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "配单号")
    @TableField(exist = false)
    private String billNo;

    @ApiModelProperty(notes = "全渠道订单号")
    @TableField(exist = false)
    private String retailOrderBillNo;

    @ApiModelProperty(notes = "全渠道订单手工单号")
    @TableField(exist = false)
    private String retailOrderManualId;

    @ApiModelProperty(notes = "销售类型 0-销售、1-现货直发、2-现货自提、3-预售直发、4-预售自提")
    @TableField(exist = false)
    private Integer saleType;

    @ApiModelProperty(notes = "条形码")
    @TableField(exist = false)
    private String saleTypeName;

    @ApiModelProperty(notes = "货品类型 0-普通、1-赠品、2-换货")
    @TableField(exist = false)
    private Integer retailGoodsType;

    @ApiModelProperty(notes = "条形码")
    @TableField(exist = false)
    private String retailGoodsTypeName;

    @ApiModelProperty(notes = "条形码")
    @TableField(exist = false)
    private String barcode;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "全渠道订单号")
    private Long retailOrderBillId;

    @ApiModelProperty(notes = "全渠道订单货品ID")
    private Long retailOrderBillGoodsId;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "折扣")
    private BigDecimal discount;

    @ApiModelProperty(notes = "结算价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "总结算价")
    @TableField(exist = false)
    private BigDecimal totalBalancePrice = BigDecimal.ZERO;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "扫描数量 （用于验货校验）")
    @TableField(exist = false)
    private BigDecimal scanQuantity = BigDecimal.ZERO;

    @ApiModelProperty(notes = "发货数量")
    @TableField(exist = false)
    private BigDecimal deliveryQuantity = BigDecimal.ZERO;

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

    @ApiModelProperty(notes = "唯一码编码")
    private Long labelId;

    @ApiModelProperty(notes = "唯一码编号")
    @TableField(exist = false)
    private String labelCode;

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

    @JsonIgnoreProperties
    public String getSingleCode() {
        StringBuilder key = new StringBuilder();
        key.append(this.goodsId).append(StrUtil.C_UNDERLINE).append(this.colorId).append(StrUtil.C_UNDERLINE)
                .append(this.longId).append(StrUtil.C_UNDERLINE).append(this.sizeId);
        return key.toString();
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
        Date date = new Date();
        date = DateUtil.getDateTime(date);
        setUpdatedBy(ThreadLocalGroup.getUserId());
        setUpdatedTime(date);
    }

}
