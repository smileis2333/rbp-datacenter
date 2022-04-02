package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.purchase.GoodsDetailIdentifier;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/03/14
 * @description
 */
@Data
public class BillGoodsDetailData implements GoodsDetailIdentifier {
    @ApiModelProperty("条形码")
    private String barcode;
    @ApiModelProperty("货品")
    private String goodsCode;
    @ApiModelProperty("颜色编码")
    private String colorCode;
    @ApiModelProperty("内长")
    private String longName;
    @ApiModelProperty("尺码")
    private String size;
    @NotNull
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("折扣")
    private BigDecimal discount;
    @ApiModelProperty("吊牌价")
    private BigDecimal tagPrice;
    @ApiModelProperty("结算价")
    private BigDecimal balancePrice;
    @ApiModelProperty("币种价格")
    private BigDecimal currencyPrice;
    @ApiModelProperty("汇率")
    private BigDecimal exchangeRate;
    @ApiModelProperty("备注")
    private String remark;
    @Valid
    @ApiModelProperty("货品自定义字段")
    private List<CustomizeDataDto> goodsCustomizeData;

    @Deprecated
    @ApiModelProperty(hidden = true)
    private Long goodsId;
}
