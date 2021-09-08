package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class Barcode {
    private String goodsCode;
    private String colorCode;
    private String longName;
    private String size;
    private String barcode;
    private Long ruleId;
}
