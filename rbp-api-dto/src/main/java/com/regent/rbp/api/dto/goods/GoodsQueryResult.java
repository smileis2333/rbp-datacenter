package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.regent.rbp.api.dto.base.BarcodeDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.List;

/**
 * @author xuxing
 */
@Data
public class GoodsQueryResult {
    @JsonIgnore
    private Long goodsId;
    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private int type;
    private String sizeClassName;
    private String brand;
    private String unit;
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
    private Boolean uniqueCodeFlag;
    private String supplierCode;
    private String supplierGoodsNo;
    private Boolean metricFlag;

    private String modelClass;
    private String notes;
    private String buildDate;
    private String[] longList;
    private GoodsPriceDto priceData;
    private List<GoodsColorDto> colorData;
    private List<CustomizeDataDto> customizeData;
    private List<BarcodeDto> barcodeData;
    private List<DisableSizeDto> disableSizeData;
}
