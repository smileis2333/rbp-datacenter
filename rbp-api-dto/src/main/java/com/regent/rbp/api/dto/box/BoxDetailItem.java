package com.regent.rbp.api.dto.box;

import com.regent.rbp.api.dto.validate.Code;
import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;

import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@FromTo.List({
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
        @FromTo(fromField = "barcode", toField = "barcodeId"),
})
@Data
public class BoxDetailItem {
    private String barcode;
    @Code(targetTable = "rbp_goods")
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    private BigDecimal quantity;
    private Long boxId;

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
}
