package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.validate.group.Complex;
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
        @FromTo(fromField = "goodsCode", toField = "goodsId",groups = Complex.class,message = "{regent.FromTo.goodsCodeNotExist}"),
        @FromTo(fromField = "colorCode", toField = "colorId",groups = Complex.class,message = "{regent.FromTo.colorCodeNotExist}"),
        @FromTo(fromField = "longName", toField = "longId",groups = Complex.class,message = "{regent.FromTo.longNameNotExist}"),
        @FromTo(fromField = "size", toField = "sizeId",groups = Complex.class,message = "{regent.FromTo.sizeNotExist}"),
})
public class BarcodeItem {
    @NotBlank(message = "{javax.NotEmpty.goodsCode}")
    private String goodsCode;

    private String colorCode;

    private String longName;

    private String size;

    @NotBlank(message = "{javax.NotEmpty.barcode}")
    private String barcode;

    @NotBlank(message = "{javax.NotEmpty.ruleId}")
    @RuleIdCheck(message = "{regent.ruleId.ruleIdNotExist}")
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

    private Integer idx;

}
