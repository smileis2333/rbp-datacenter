package com.regent.rbp.api.service.stock;

import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.stock.StockQueryParam;
import com.regent.rbp.api.dto.stock.StockQueryResult;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
public interface StockQueryService {

    PageDataResponse<StockQueryResult> query(StockQueryParam param);
}
