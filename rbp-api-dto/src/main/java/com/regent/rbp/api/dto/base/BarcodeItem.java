package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.validate.FromTo;
import com.regent.rbp.api.dto.validate.RuleIdCheck;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.Objects;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
@FromTo.List({
        @FromTo(fromField = "goodsCode", toField = "goodsId"),
        @FromTo(fromField = "colorCode", toField = "colorId"),
        @FromTo(fromField = "longName", toField = "longId"),
        @FromTo(fromField = "size", toField = "sizeId"),
})
public class BarcodeItem {
    @NotBlank
    private String goodsCode;

    private String colorCode;

    private String longName;

    private String size;

    @NotBlank
    private String barcode;

    @NotBlank
    @RuleIdCheck
    private String ruleId;

    @Null
    private Long goodsId;

    @Null
    private Long colorId;

    @Null
    private Long longId;

    @Null
    private Long sizeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarcodeItem that = (BarcodeItem) o;
        return Objects.equals(barcode, that.barcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(barcode);
    }

}
