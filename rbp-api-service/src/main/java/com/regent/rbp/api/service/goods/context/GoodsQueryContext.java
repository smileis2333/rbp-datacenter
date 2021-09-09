package com.regent.rbp.api.service.goods.context;

import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dto.goods.GoodsPriceData;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 货品查询上下文对象
 * @author xuxing
 */
@Data
public class GoodsQueryContext {
    private String[] goodsCode;
    private String[] goodsName;
    private String[] mnemonicCode;
    private int[] type;
    private long[] brandIds;
    private long[] categoryIds;
    private long[] seriesIds;
    private long[] patternIds;
    private long[] sexIds;
    private long[] bandIds;
    private long[] yearIds;
    private long[] seasonIds;
    private long[] styleIds;
    private long[] supplierIds;
    private int[] status;
    private Date createdDateStart;
    private Date createdDateEnd;
    private Date checkDateStart;
    private Date checkDateEnd;
    private Date updatedDateStart;
    private Date updatedDateEnd;
    private List<String> fields;

    private int pageNo;
    private int pageSize;

}
