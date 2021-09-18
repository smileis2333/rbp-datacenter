package com.regent.rbp.task.inno.model.dto;

import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class GoodsSearchDto {
    /**
     * 获取的页码（第一页开始）
     */
    private int pageIndex;
    /**
     * 每页的 记录数
     */
    private int pageSize;
    /**
     * 搜索关键字
     */
    private String txtKey;
    /**
     * 分类ID
     */
    private Integer Category;
    /**
     * 排序 (asc/desc 升序/降序)
     */
    private String orderByType;
    /**
     * 排序字段(1-SalePrice，2-UnitSales，3-Integral)
     */
    private String orderwhere;
    /**
     * 上架状态，0下架，1上架， 2全部
     */
    private int onSaleStatus;
}
