package com.regent.rbp.api.service.goods;

import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.dto.goods.GoodsSaveResult;

/**
 * @author xuxing
 */
public interface GoodsService {
    GoodsQueryResult query(GoodsQueryParam param);

    GoodsSaveResult save(GoodsSaveParam param);
}
