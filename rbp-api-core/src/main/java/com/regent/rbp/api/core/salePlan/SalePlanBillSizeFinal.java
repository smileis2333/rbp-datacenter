package com.regent.rbp.api.core.salePlan;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillGoodsSizeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 计划单调整后尺码明细
 * @author: HaiFeng
 * @create: 2021-11-15 15:26
 */
@Data
@TableName(value = "rbp_sale_plan_bill_size_final")
public class SalePlanBillSizeFinal extends BillGoodsSizeData {

    @ApiModelProperty(notes = "欠数")
    private BigDecimal oweQuantity;

}
