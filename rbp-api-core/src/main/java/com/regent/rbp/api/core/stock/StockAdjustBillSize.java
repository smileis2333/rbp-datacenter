package com.regent.rbp.api.core.stock;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库存调整单尺码明细对象 rbp_stock_adjust_bill_size
 *
 * @author lzc
 * @date 2020-08-13
 */
@Data
@ApiModel(description = "库存调整单尺码明细")
@EqualsAndHashCode(callSuper = false)
@TableName(value = "rbp_stock_adjust_bill_size")
public class StockAdjustBillSize extends BillGoodsSizeData {

}
