package com.regent.rbp.api.dto.goods;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.BarcodeDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.SupplierCodeCheck;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static com.regent.rbp.infrastructure.util.DateUtil.SHORT_DATE_FORMAT;

/**
 * @author xuxing
 */
@Data
public class GoodsSaveParam {
    @NotBlank
    private String goodsCode;
    private String goodsName;
    private String mnemonicCode;
    @NotNull
    @DiscreteRange(ranges = {1, 2}, message = "1.普通物料, 2.单一物料")
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
    @SupplierCodeCheck
    private String supplierCode;
    private String supplierGoodsNo;
    private boolean metricFlag;
    private String unit;
    @NotNull
    @BillStatus
    private Integer status;

    private String modelClass;
    private String notes;
    @JsonFormat(pattern = SHORT_DATE_FORMAT)
    private Date buildDate;
    private List<String> colorList;
    private List<String> longList;
    private GoodsPriceDto priceData;
    private List<CustomizeDataDto> customizeData;
    @Valid
    private List<BarcodeDto> barcodeData;
    @Valid
    private List<DisableSizeDto> disableSizeData;

    // --------------------
    // cross param validate

    @AssertTrue(message = "type=1,sizeClassName必填; type=2, sizeClassName非必填")
    private boolean isSizeClassName() {
        return (type != null) && ((type == 2) || (type == 1 && StrUtil.isNotEmpty(sizeClassName)));
    }

    @AssertTrue(message = "type=1,colorList必填; type=2,colorList留空")
    private boolean isColorList() {
        return (type != null) && ((type == 2 && CollUtil.isEmpty(colorList)) || (type == 1 && CollUtil.isNotEmpty(colorList)));
    }

    @AssertTrue(message = "type=1,longList必填; type=2,longList留空")
    private boolean isLongList() {
        return (type != null) && ((type == 2 && CollUtil.isEmpty(longList)) || (type == 1 && CollUtil.isNotEmpty(longList)));
    }

    public void setType(Integer type) {
        this.type = type;
        barcodeData.forEach(e -> e.setGoodsType(type));
    }
}
