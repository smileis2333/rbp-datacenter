package com.regent.rbp.api.service.enums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.Band;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.core.base.Category;
import com.regent.rbp.api.core.base.DiscountCategory;
import com.regent.rbp.api.core.base.ExchangeCategory;
import com.regent.rbp.api.core.base.Material;
import com.regent.rbp.api.core.base.Pattern;
import com.regent.rbp.api.core.base.SaleClass;
import com.regent.rbp.api.core.base.Season;
import com.regent.rbp.api.core.base.Series;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.base.Style;
import com.regent.rbp.api.core.base.Year;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description
 */
public enum BaseDataEnum {

    BRAND("brand", Brand.class, true),

    CATEGORY("category", Category.class, false),

    SERIES("series", Series.class, false),

    PATTERN("pattern", Pattern.class, false),

    STYLE("brand", Style.class, false),

    SALE_CLASS("sale_class", SaleClass.class, false),

    YEAR("year", Year.class, false),

    SEASON("season", Season.class, false),

    BAND("band", Band.class, false),

    MATERIAL("material", Material.class, false),

    SEX("sex", Sex.class, false),

    EXCHANGE_CATEGORY("exchange_category", ExchangeCategory.class, false),

    DISCOUNT_CATEGORY("discount_category", DiscountCategory.class, false),

    ;

    /**
     * 基础资料类型
     */
    private String type;

    /**
     * 实体
     */
    private Class<T> entity;

    /**
     * 是否返回code
     */
    private Boolean codeFlag;

    public String getType() {
        return type;
    }

    public Class<T> getEntity() {
        return entity;
    }

    public Boolean getCodeFlag() {
        return codeFlag;
    }

    BaseDataEnum(String type, Class entity, Boolean codeFlag) {
        this.type = type;
        this.entity = entity;
        this.codeFlag = codeFlag;
    }

    /**
     * 获取表名
     *
     * @param type
     * @return
     */
    public static String getTableName(String type) {
        for (BaseDataEnum dataEnum : BaseDataEnum.values()) {
            if (dataEnum.getType().equals(type)) {
                TableName annotation = dataEnum.getEntity().getAnnotation(TableName.class);
                return annotation.value();
            }
        }

        return null;
    }

    /**
     * 是否返回code
     *
     * @param type
     * @return
     */
    public static Boolean isCodeFlag(String type) {
        for (BaseDataEnum dataEnum : BaseDataEnum.values()) {
            if (dataEnum.getType().equals(type)) {
                return dataEnum.getCodeFlag();
            }
        }
        return false;
    }

}
