package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenchungui
 * @date 2021/12/21
 * @description
 */
@Data
public class PurchaseBillGoodsDetailData extends BillGoodsDetailData {

    @ApiModelProperty(notes = "交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    @ApiModelProperty(notes = "来货超差类型")
    private String receiveDifferentType;

    @ApiModelProperty(notes = "来货超差比例")
    private BigDecimal receiveDifferentPercent;

    @ApiModelProperty(notes = "行ID")
    @JsonIgnore
    private Long columnId;

    @ApiModelProperty(notes = "货品ID")
    @JsonIgnore
    private Long goodsId;

}
