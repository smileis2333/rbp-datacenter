package com.regent.rbp.api.web.goods;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.dto.goods.GoodsSaveResult;
import com.regent.rbp.api.service.goods.GoodsService;
import com.regent.rbp.api.web.constants.ApiConstants;
import com.regent.rbp.infrastructure.response.BaseResponse;
import com.regent.rbp.infrastructure.response.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuxing
 */
@RestController
@RequestMapping(ApiConstants.API_GOODS)
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @PostMapping("/query")
    public PageDataResponse<GoodsQueryResult> query(@RequestBody GoodsQueryParam param) {
        PageDataResponse<GoodsQueryResult> result = goodsService.query(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody GoodsSaveParam param) {
        DataResponse result = goodsService.save(param);
        return result;
    }
}
