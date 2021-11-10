package com.regent.rbp.api.service.goods;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.dto.goods.GoodsSaveResult;

import java.util.Map;

/**
 * @author xuxing
 */
public interface GoodsService {

    PageDataResponse<GoodsQueryResult> query(GoodsQueryParam param);

    DataResponse save(GoodsSaveParam param);

}
