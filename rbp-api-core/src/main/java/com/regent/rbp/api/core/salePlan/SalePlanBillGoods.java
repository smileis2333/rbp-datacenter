package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @program: rbp-datacenter
 * @description: 计划单货品明细表
 * @author: HaiFeng
 * @create: 2021-11-15 15:17
 */
@Data
@TableName(value = "rbp_sale_plan_bill_goods")
public class SalePlanBillGoods {

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

    @ApiModelProperty(notes = "自定义字段")
    @TableField(exist = false)
    private Map<String, Object> goodsCustomizeData;

    @AllArgsConstructor
    @Data
    public static class GoodsIdBalancePricePair{
        private Long goodsId;
        private BigDecimal balancePrice;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GoodsIdBalancePricePair that = (GoodsIdBalancePricePair) o;
            return Objects.equals(getGoodsId(), that.getGoodsId()) && Objects.equals(getBalancePrice(), that.getBalancePrice());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getGoodsId(), getBalancePrice());
        }
    }

}
