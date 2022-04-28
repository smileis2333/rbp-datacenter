package com.regent.rbp.api.core.stock;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 库存调整单货品明细对象 rbp_stock_adjust_bill_goods
 * 
 * @author lzc
 * @date 2020-08-13
 */
@Data
@ApiModel(description="库存调整单货品明细")
@EqualsAndHashCode(callSuper=false)
@TableName(value = "rbp_stock_adjust_bill_goods")
public class StockAdjustBillGoods extends Model<StockAdjustBillGoods> {

    @ApiModelProperty(notes = "编码")
    private Long id;

    @ApiModelProperty(notes = "单据编码")
    private Long billId;

    @ApiModelProperty(notes = "货品编码")
    private Long goodsId;

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity = BigDecimal.ZERO;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice = BigDecimal.ZERO;

}
