package com.regent.rbp.api.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SalesPlanBillGoodsResult {
    // 入参

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

    @ApiModelProperty(notes = "数量")
    private BigDecimal quantity;

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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date deliveryDate;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "货品自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

}
