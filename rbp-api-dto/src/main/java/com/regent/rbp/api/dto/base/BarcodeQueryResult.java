package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @description
 */
@Data
public class BarcodeQueryResult {
    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private String ruleId;
    private String barcode;
    private String colorCode;
    private String longName;
    private String size;
}
