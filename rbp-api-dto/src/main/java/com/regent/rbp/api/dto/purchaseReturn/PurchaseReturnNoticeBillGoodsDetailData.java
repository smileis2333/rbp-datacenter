package com.regent.rbp.api.dto.purchaseReturn;

import com.regent.rbp.api.dto.base.BillGoodsDetailData;
import lombok.Data;

import javax.validation.constraints.Null;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知货品明细
 * @author: HaiFeng
 * @create: 2021/12/30 14:18
 */
@Data
public class PurchaseReturnNoticeBillGoodsDetailData extends BillGoodsDetailData {
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
