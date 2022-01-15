package com.regent.rbp.api.dto.base;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author xuxing
 */
@Data
public class BarcodeDto {
    @JsonIgnore
    private Long goodsId;
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    @NotBlank
    private String barcode;
    @NotNull
    @DiscreteRange(ranges = {6888783191082240l,6888783191082241l,6888783191082242l,6888783191082243l},
            message = "6888783191082240 内部码, 6888783191082241 内部码(含内长), 6888783191082242 国标码7位, 6888783191082243 国标码8位")
    private Long ruleId;

    // --------------------
    // cross param validate
    private Integer goodsType;

    @AssertTrue(message = "type=1,必填；type=2,非必填")
    private boolean isSize() {
        return (goodsType != null) && ((goodsType == 2) || (goodsType == 1 && StrUtil.isNotEmpty(size)));
    }

    @AssertTrue(message = "type=1,必填；type=2,非必填")
    private boolean isColorCode() {
        return (goodsType != null) && ((goodsType == 2) || (goodsType == 1 && StrUtil.isNotBlank(colorCode)));
    }

    @AssertTrue(message = "type=1,必填；type=2,非必填")
    private boolean isLongName() {
        return (goodsType != null) && ((goodsType == 2) || (goodsType == 1 && StrUtil.isNotBlank(longName)));
    }
}
