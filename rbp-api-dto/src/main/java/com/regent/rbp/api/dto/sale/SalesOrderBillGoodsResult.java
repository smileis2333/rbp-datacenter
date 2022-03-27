package com.regent.rbp.api.dto.sale;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单货品明细
 * @author: HaiFeng
 * @create: 2021-11-08 14:29
 */
@Data
public class SalesOrderBillGoodsResult extends BillGoodsDetailData {

    @ApiModelProperty(notes = "状态")
    private Integer status;

    @DiscreteRange(ranges = {0, 1, 2, 3}, message = "入参非法，合法输入0.正常销售、1.预售直发、2.预售自提、3.全渠道发货")
    @ApiModelProperty(notes = "销售类型(0.正常销售、1.预售直发、2.预售自提、3.全渠道发货)")
    private Integer saleType;

    @ApiModelProperty(notes = "计收价")
    private BigDecimal retailPrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal salesPrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal originalPrice;

    @ApiModelProperty(notes = "积分")
    private BigDecimal point;

    @ApiModelProperty(notes = "吊牌折扣")
    private BigDecimal tagDiscount;

    @ApiModelProperty(notes = "零售折扣")
    private BigDecimal retailDiscount;

    @ApiModelProperty(notes = "结算折扣")
    private BigDecimal balanceDiscount;

    @Valid
    private List<EmployeeAchievement> employeeGoodsAchievement;

    @Null
    private Long goodsId;

    @Null
    private Long colorId;

    @Null
    private Long longId;

    @Null
    private Long sizeId;

    @Null
    private Long barcodeId;

    @Null
    private Integer rowIndex;

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
        if (CollUtil.isNotEmpty(employeeGoodsAchievement)) {
            employeeGoodsAchievement.forEach(e -> e.setRowIndex(rowIndex));
        }
    }
}
