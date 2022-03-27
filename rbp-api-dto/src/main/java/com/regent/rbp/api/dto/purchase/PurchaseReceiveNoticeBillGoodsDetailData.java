package com.regent.rbp.api.dto.purchase;

import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import lombok.Data;

import javax.validation.constraints.Null;


@Data
public class PurchaseReceiveNoticeBillGoodsDetailData extends BillGoodsDetailData {
    @Null
    private Long goodsId;

    @Null
    private Long colorId;

    @Null
    private Long longId;

    @Null
    private Long sizeId;

    @Null
    private Long barcodeId;

    @Null
    private Long billGoodId;

}
