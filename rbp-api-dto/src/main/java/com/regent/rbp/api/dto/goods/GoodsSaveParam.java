package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.BarcodeDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.regent.rbp.infrastructure.util.DateUtil.SHORT_DATE_FORMAT;

/**
 * @author xuxing
 */
@Data
public class GoodsSaveParam {
    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    private Integer type;
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
    private String unit;
    private Integer status = 0;

    private String modelClass;
    private String notes;
    @JsonFormat(pattern = SHORT_DATE_FORMAT)
    private Date buildDate;
    private String[] colorList;
    private String[] longList;
    private GoodsPriceDto priceData;
    private List<CustomizeDataDto> customizeData;
    private List<BarcodeDto> barcodeData;
    private List<DisableSizeDto> disableSizeData;
}
