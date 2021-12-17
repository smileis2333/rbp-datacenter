package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @program: rbp-datacenter
 * @description: 计划单调整后货品明细表
 * @author: HaiFeng
 * @create: 2021-11-15 15:23
 */
@Data
@TableName(value = "rbp_sale_plan_bill_goods_final")
public class SalePlanBillGoodsFinal {

    @ApiModelProperty(notes = "编码")
    @TableId("id")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

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

    @ApiModelProperty(notes = "交货日期")
    private Date deliveryDate;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "调整后尺码明细")
    @TableField(exist = false)
    List<SalePlanBillSizeFinal> finalSizeList;

    /**
     * 同款多价，根据货品ID+价格分组
     *
     * @return
     */
    public String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s", this.getGoodsId(), balancePriceStr);
    }
}
