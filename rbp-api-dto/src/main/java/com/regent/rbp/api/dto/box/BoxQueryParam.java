package com.regent.rbp.api.dto.box;

import com.regent.rbp.api.dto.base.DefaultParam;
import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@Data
public class BoxQueryParam extends DefaultParam {
    private List<String> code;
    private List<String> manualId;
    private List<String> batchNumber;
    private List<String> channelCode;
    private List<String> supplierCode;
    private List<String> distributionTypeCode;
    private Integer type;
    private List<Integer> status;
    private List<String> goodsCode;
    private List<String> goodsName;
    private List<String> mnemonicCode;

}
