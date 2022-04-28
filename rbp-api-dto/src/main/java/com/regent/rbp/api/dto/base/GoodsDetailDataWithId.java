package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class GoodsDetailDataWithId extends GoodsDetailData {
    private Long barcodeId;
    private Long goodsId;
    private Long colorId;
    private Long longId;
    private Long sizeId;
}
