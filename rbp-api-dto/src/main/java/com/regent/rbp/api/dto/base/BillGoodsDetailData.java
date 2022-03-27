package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.purchase.GoodsDetailIdentifier;
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
    private String barcode;
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    @NotNull
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tagPrice;
    private BigDecimal balancePrice;
    private BigDecimal currencyPrice;
    private BigDecimal exchangeRate;
    private String remark;
    @Valid
    private List<CustomizeDataDto> goodsCustomizeData;

    @Deprecated
    private Long goodsId;
}
