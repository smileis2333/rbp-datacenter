package com.regent.rbp.api.web.goods;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.service.goods.GoodsService;
import com.regent.rbp.api.web.constants.ApiConstants;
import com.regent.rbp.infrastructure.annotation.PassToken;
import io.swagger.annotations.Api;
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
@Api(tags = "货品档案")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @PassToken
    @PostMapping("/query")
    public PageDataResponse<GoodsQueryResult> query(@RequestBody GoodsQueryParam param) {
        PageDataResponse<GoodsQueryResult> result = goodsService.query(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody GoodsSaveParam param) {
        if (CollUtil.isNotEmpty(param.getBarcodeData())){
            param.getBarcodeData().forEach(e->e.setGoodsType(param.getType()));
        }
        DataResponse result = goodsService.save(param);
        return result;
    }
}
