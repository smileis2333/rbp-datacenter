package com.regent.rbp.api.service.purchaseReturn;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchaseReturn.PurchaseReturnNoticeBillSaveParam;

/**
 * @program: rbp-datacenter
 * @description: 采购退货通知 Service
 * @author: HaiFeng
 * @create: 2021/12/30 14:26
 */
public interface PurchaseReturnNoticeBillService {

    PageDataResponse<PurchaseReturnNoticeBillQueryResult> query(PurchaseReturnNoticeBillQueryParam param);

    DataResponse save(PurchaseReturnNoticeBillSaveParam param);
}
