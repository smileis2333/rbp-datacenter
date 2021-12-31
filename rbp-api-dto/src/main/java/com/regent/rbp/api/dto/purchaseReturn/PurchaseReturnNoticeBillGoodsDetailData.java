package com.regent.rbp.api.dto.purchaseReturn;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知货品明细
 * @author: HaiFeng
 * @create: 2021/12/30 14:18
 */
@Data
public class PurchaseReturnNoticeBillGoodsDetailData {

    @ApiModelProperty(notes = "条形码")
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

    @ApiModelProperty(notes = "采购价")
    private BigDecimal balancePrice;

    @ApiModelProperty(notes = "币种价格")
    private BigDecimal currencyPrice;

    @ApiModelProperty(notes = "汇率")
    private BigDecimal exchangeRate;

    @ApiModelProperty(notes = "备注")
    private String remark;

    @ApiModelProperty(notes = "自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

}
