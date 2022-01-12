package com.regent.rbp.api.dto.purchase;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BarcodeOrGoodsCode;
import com.regent.rbp.api.dto.validate.BarcodeRelationCheck;
import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;


@Data
@FromTo.List({
        @FromTo(fromField = "goodsCode", toField = "goodsId"),
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
})
@BarcodeRelationCheck
@BarcodeOrGoodsCode
public class PurchaseReceiveNoticeBillGoodsDetailData implements GoodsDetailIdentifier {
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
