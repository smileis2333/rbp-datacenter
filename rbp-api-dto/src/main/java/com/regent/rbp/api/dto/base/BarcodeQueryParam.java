package com.regent.rbp.api.dto.base;

import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeQueryParam extends DefaultParam{

    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private String ruleId;
    private List<String> barcode;
}
