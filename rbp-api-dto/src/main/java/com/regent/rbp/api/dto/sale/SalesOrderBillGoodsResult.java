package com.regent.rbp.api.dto.sale;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.validate.BarcodeOrGoodsCode;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.FromTo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

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
@FromTo.List({
        @FromTo(fromField = "goodsCode", toField = "goodsId"),
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
        @FromTo(fromField = "barcode", toField = "barcodeId"),
})
@Data
@BarcodeOrGoodsCode
public class SalesOrderBillGoodsResult {

    @ApiModelProperty(notes = "条形码。注：条形码和「货号、颜色编号、内长、尺码」二选一。")
    private String barcode;

    @ApiModelProperty(notes = "货号")
    private String goodsCode;

    @ApiModelProperty(notes = "颜色编号")
    private String colorCode;

    @ApiModelProperty(notes = "内长")
    private String longName;

    @ApiModelProperty(notes = "尺码")
    private String size;

    @ApiModelProperty(notes = "状态")
    private Integer status;

    @DiscreteRange(ranges = {0, 1, 2, 3}, message = "入参非法，合法输入0.正常销售、1.预售直发、2.预售自提、3.全渠道发货")
    @ApiModelProperty(notes = "销售类型(0.正常销售、1.预售直发、2.预售自提、3.全渠道发货)")
    private Integer saleType;

    @ApiModelProperty(notes = "吊牌价")
    private BigDecimal tagPrice;

    @ApiModelProperty(notes = "计收价")
    private BigDecimal retailPrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal salesPrice;

    @ApiModelProperty(notes = "实卖价")
    private BigDecimal originalPrice;

    @ApiModelProperty(notes = "数量。非计量货品的数量必须是1，可以分多行")
    @Range(min = 0)
    private BigDecimal quantity;

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
