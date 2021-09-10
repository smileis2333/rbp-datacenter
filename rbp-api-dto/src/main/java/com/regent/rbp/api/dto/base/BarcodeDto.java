package com.regent.rbp.api.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

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
    private String barcode;
    private Long ruleId;
}
