package com.regent.rbp.api.dto.goods;

import com.regent.rbp.api.dto.base.Barcode;
import com.regent.rbp.api.dto.base.CustomizeData;
import com.regent.rbp.api.dto.base.DisableSize;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsQueryResult {
    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private int type;
    private String sizeClassName;
    private String brand;
    private String category;
    private String series;
    private String pattern;
    private String style;
    private String saleClass;
    private String year;
    private String season;
    private String band;
    private String material;
    private String assistMaterial;
    private String sex;
    private Integer minAge;
    private Integer maxAge;
    private String exchangeCategory;
    private String discountCategory;
    private String qrcodeLink;
    private boolean uniqueCodeFlag;
    private String supplierCode;
    private String supplierGoodsNo;
    private boolean metricFlag;

    private String modelClass;
    private String notes;
    private String buildDate;
    private String[] colorList;
    private String[] longList;
    private GoodsPriceData priceData;
    private List<CustomizeData> customizeData;
    private List<Barcode> barcodeData;
    private List<DisableSize> disableSizeData;
}
