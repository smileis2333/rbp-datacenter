package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.validate.BarcodeCheck;
import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeQueryParam {

    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private String ruleId;
    @BarcodeCheck
    private List<String> barcode;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;

}
