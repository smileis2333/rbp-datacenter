package com.regent.rbp.api.dto.purchase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BarcodeOrGoodsCode;
import com.regent.rbp.api.dto.validate.BarcodeRelationCheck;
import com.regent.rbp.api.dto.validate.FromTo;
import com.regent.rbp.api.dto.validate.group.Complex;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;


@Data
@FromTo.List({
        @FromTo(fromField = "goodsCode", toField = "goodsId", groups = Complex.class, message = "{regent.FromTo.goodsCodeNotExist}"),
        @FromTo(fromField = "colorCode", toField = "colorId", groups = Complex.class, message = "{regent.FromTo.colorCodeNotExist}"),
        @FromTo(fromField = "longName", toField = "longId", groups = Complex.class, message = "{regent.FromTo.longNameNotExist}"),
        @FromTo(fromField = "size", toField = "sizeId", groups = Complex.class, message = "{regent.FromTo.sizeNotExist}"),
})
@BarcodeRelationCheck(message = "{regent.BarcodeRelationCheck.invalidBarcode}",groups = Complex.class)
@BarcodeOrGoodsCode(message = "{regent.barcodeOrGoodsCode.onlyAllowOne}")
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

    /**
     * 同款多价，根据货品ID+价格分组
     *
     * @return
     */
    @JsonIgnore
    public String getSameGoodsDiffPriceKey() {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000#");
        String balancePriceStr = decimalFormat.format(Optional.ofNullable(this.getBalancePrice()).orElse(BigDecimal.ZERO));
        return String.format("%s_%s", this.getGoodsId(), balancePriceStr);
    }

    private Integer idx;

    @Null
    private Long goodsId;

    @Null
    private Long colorId;

    @Null
    private Long longId;

    @Null
    private Long sizeId;

    @Null
    private Long billGoodId;
}
