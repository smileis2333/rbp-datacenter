package com.regent.rbp.api.dto.goods;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.dto.base.BarcodeDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.validate.BillStatus;
import com.regent.rbp.api.dto.validate.Dictionary;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import com.regent.rbp.api.dto.validate.SupplierCodeCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.regent.rbp.infrastructure.util.DateUtil.SHORT_DATE_FORMAT;

/**
 * @author xuxing
 */
@Data
public class GoodsSaveParam {
    @NotBlank
    @ApiModelProperty("货号")
    private String goodsCode;

    @ApiModelProperty("货品名称")
    private String goodsName;

    @ApiModelProperty("助记码")
    private String mnemonicCode;

    @NotNull
    @DiscreteRange(ranges = {1, 2}, message = "1.普通物料, 2.单一物料")
    @ApiModelProperty("货品类型。\n" +
            "type=1.普通物料;\n" +
            "type=2.单一物料")
    private Integer type;

    @Dictionary(targetTable = "rbp_size_class", targetField = "name")
    @ApiModelProperty("尺码类别。\n" +
            "特殊说明：\n" +
            "type=1,sizeClassName必填；\n" +
            "type=2, sizeClassName非必填")
    private String sizeClassName;

    @Dictionary(targetTable = "rbp_brand", targetField = "name")
    @ApiModelProperty("品牌(名称)")
    private String brand;
    @ApiModelProperty("类别")
    private String category;
    @ApiModelProperty("系列")
    private String series;
    @ApiModelProperty("款型")
    private String pattern;
    @ApiModelProperty("风格")
    private String style;
    @ApiModelProperty("销售分类")
    private String saleClass;
    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty("季节")
    private String season;
    @ApiModelProperty("波段")
    private String band;
    @ApiModelProperty("面料")
    private String material;
    @ApiModelProperty("辅料")
    private String assistMaterial;
    @ApiModelProperty("辅料")
    private String sex;
    @ApiModelProperty("年龄段-最小年龄")
    private Integer minAge;
    @ApiModelProperty("年龄段-最大年龄")
    private Integer maxAge;
    @ApiModelProperty("换货类别")
    private String exchangeCategory;
    @ApiModelProperty("折扣类别")
    private String discountCategory;
    @ApiModelProperty("二维码链接")
    private String qrcodeLink;
    @ApiModelProperty("启用唯一码")
    private boolean uniqueCodeFlag;

    @SupplierCodeCheck
    @ApiModelProperty("供应商编号")
    private String supplierCode;
    @ApiModelProperty("厂家编号")
    private String supplierGoodsNo;
    @ApiModelProperty("计量商品标记")
    private boolean metricFlag;
    private String unit;

    @NotNull
    @BillStatus
    @ApiModelProperty("单据状态(0.未审核,1.已审核)")
    private Integer status;

    @Dictionary(targetTable = "rbp_model_class", targetField = "name")
    @ApiModelProperty("号型")
    private String modelClass;

    @ApiModelProperty("备注")
    private String notes;

    @JsonFormat(pattern = SHORT_DATE_FORMAT)
    @ApiModelProperty("建档日期")
    private Date buildDate = new Date();

    @ApiModelProperty("货品的颜色编号列表。\n" +
            "注：只追加，不替换。\n" +
            "type=1,colorList必填；\n" +
            "type=2,colorList留空")
    private Set<String> colorList;

    @ApiModelProperty("货品的内长列表。\n" +
            "注：只追加，不替换。\n" +
            "type=1,longList必填；\n" +
            "type=2,longList留空")
    private Set<String> longList;

    @Valid
    @ApiModelProperty("价格数据")
    private GoodsPriceDto priceData;

    @Valid
    @ApiModelProperty("自定义字段")
    private List<CustomizeDataDto> customizeData;

    @Valid
    @ApiModelProperty("条形码数据")
    private List<BarcodeDto> barcodeData;

    @Valid
    @ApiModelProperty("尺码停用数据")
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
}
