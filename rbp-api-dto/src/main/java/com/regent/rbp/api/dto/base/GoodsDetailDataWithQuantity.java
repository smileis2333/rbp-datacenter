package com.regent.rbp.api.dto.base;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class GoodsDetailDataWithQuantity extends GoodsDetailDataWithId{
    private BigDecimal quantity;

    private List<CustomizeDataDto> goodsCustomizeData;
}
