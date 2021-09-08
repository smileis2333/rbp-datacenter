package com.regent.rbp.api.web.goods;

import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.dto.goods.GoodsSaveResult;
import com.regent.rbp.api.service.goods.GoodsService;
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
@RequestMapping("api/goods")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @PostMapping("/query")
    public GoodsQueryResult query(@RequestBody GoodsQueryParam param) {
        GoodsQueryResult result = goodsService.query(param);
        return result;
    }

    @PostMapping()
    public GoodsSaveResult save(@RequestBody GoodsSaveParam param) {
        GoodsSaveResult result = goodsService.save(param);
        return result;
    }
}
