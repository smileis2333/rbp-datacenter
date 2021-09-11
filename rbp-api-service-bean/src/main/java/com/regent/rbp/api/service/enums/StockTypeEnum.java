package com.regent.rbp.api.service.enums;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.stock.ForwayStockDetail;
import com.regent.rbp.api.core.stock.StockDetail;
import com.regent.rbp.api.core.stock.UsableStockDetail;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import org.apache.poi.ss.formula.functions.T;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description 库存类型
 */
public enum StockTypeEnum {

    /**
     * 库存
     */
    STOCK(1, StockDetail.class),

    /**
     * 可用库存
     */
    USABLE_STOCK(2, UsableStockDetail.class),

    /**
     * 在途库存
     */
    FORWAY_STOCK(3, ForwayStockDetail.class);

    private Integer type;

    /**
     * 实体
     */
    private Class<T> entity;

    StockTypeEnum(Integer type, Class entity) {
        this.type = type;
        this.entity = entity;
    }

    public Integer getType() {
        return type;
    }

    public Class<T> getEntity() {
        return entity;
    }

    /**
     * 获取表名
     *
     * @param type
     * @return
     */
    public static String getTableName(Integer type) {
        for (StockTypeEnum stockTypeEnum : StockTypeEnum.values()) {
            if (stockTypeEnum.getType().equals(type)) {
                TableName annotation = stockTypeEnum.getEntity().getAnnotation(TableName.class);
                return annotation.value();
            }
        }
        return null;
    }
}
