package com.regent.rbp.api.dto.goods;


import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * link outsite field to nest field for further jsr303 validate
 * not allow assume any valid data after jsr303!!!must do data check
 */
public class GoodsFieldLinker extends StdConverter<GoodsSaveParam,GoodsSaveParam> {
    @Override
    public GoodsSaveParam convert(GoodsSaveParam param) {
        Integer type = param.getType();
        if (type != null && CollUtil.isNotEmpty(param.getBarcodeData())) {
            param.getBarcodeData().forEach(e->e.setGoodsType(type));
        }
        return param;
    }
}