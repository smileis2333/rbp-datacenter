package com.regent.rbp.api.dto.purchaseReturn;

import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.purchase.GoodsDetailIdentifier;
import com.regent.rbp.api.dto.validate.Code;
import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知货品明细
 * @author: HaiFeng
 * @create: 2021/12/30 14:18
 */
@FromTo.List({
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
        @FromTo(fromField = "barcode", toField = "barcodeId"),
})
@Data
public class PurchaseReturnNoticeBillGoodsDetailData implements GoodsDetailIdentifier {

    private String barcode;
    @Code(targetTable = "rbp_goods")
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
    private Long billGoodId;
}
