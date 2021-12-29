package com.regent.rbp.api.service.barcode.context;

import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeQueryContext {
    private List<Long> goodsIds;
    private String ruleId;
    private List<String> barcode;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;

}
