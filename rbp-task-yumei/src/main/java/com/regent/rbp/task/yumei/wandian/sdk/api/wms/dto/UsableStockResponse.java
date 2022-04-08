package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/08
 * @description
 */
@Data
public class UsableStockResponse {
    private int totalCount;

    private List<UsableStock> stocks;

    @Data
    public static class UsableStock {
        private String goodsName;
        private String specCode;
        private BigDecimal num;
        private String goodsNo;
        private String brandName;
        private String type;
        private String specNo;
        private Boolean defect;
        private String warehouseNo;
        private String shortName;
        private String specName;
        private String barcode;
        private String className;
        private Integer status;
    }
}
