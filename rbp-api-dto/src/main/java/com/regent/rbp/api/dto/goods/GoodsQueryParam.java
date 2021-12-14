package com.regent.rbp.api.dto.goods;

import com.regent.rbp.infrastructure.request.PageRequest;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class GoodsQueryParam {
    private String[] goodsCode;
    private String[] goodsName;
    private String[] mnemonicCode;
    private int[] type;
    private String[] brand;
    private String[] unit;
    private String[] category;
    private String[] series;
    private String[] pattern;
    private String[] sex;
    private String[] band;
    private String[] year;
    private String[] season;
    private String[] style;
    private String[] supplierCode;
    private int[] status;
    private String createdDateStart;
    private String createdDateEnd;
    private String checkDateStart;
    private String checkDateEnd;
    private String updatedDateStart;
    private String updatedDateEnd;
    private String fields;

    private int pageNo;
    private int pageSize;
}
