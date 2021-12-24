package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;


@Data
public class PurchaseReceiveNoticeBillGoodsDetailData {
    private String barcode;
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    @NotNull
    @Range(min = 0)
    private BigDecimal quantity;
    private BigDecimal discount;
    private BigDecimal tagPrice;
    private BigDecimal balancePrice;
    private BigDecimal currencyPrice;
    private BigDecimal exchangeRate;
    private String remark;
    private List<CustomizeDataDto> goodsCustomizeData;
    @JsonIgnore
    private Long columnId;
    @JsonIgnore
    private Long goodsId;

    /**
     * 同款多价，根据货品ID+价格分组
     *
     * @return
     */
    @JsonIgnore
    public String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s",this.getGoodsId(), balancePriceStr);
    }

}
