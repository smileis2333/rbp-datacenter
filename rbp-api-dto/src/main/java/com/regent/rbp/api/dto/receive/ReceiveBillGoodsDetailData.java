package com.regent.rbp.api.dto.receive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillGoodsDetailData {
    private String barcode;
    private String goodsCode;
    private String priceType;
    private String colorCode;
    private String longName;
    private String size;
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tagPrice;
    private BigDecimal balancePrice;
    private BigDecimal currencyPrice;
    private BigDecimal exchangeRate;
    private String remark;
    private List<CustomizeDataDto> goodsCustomizeData;
    @JsonIgnore
    private Long goodsId;
    @JsonIgnore
    private Long columnId;
    /**
     * 同款多价，根据货品ID+价格分组
     *
     * @return
     */
    public String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s",this.getGoodsId(), balancePriceStr);
    }
}
