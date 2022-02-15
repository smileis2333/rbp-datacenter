package com.regent.rbp.api.service.purchaseReturn;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillSaveParam;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
public interface PurchaseReturnBillService {
    PageDataResponse<PurchaseReturnBillQueryResult> query(PurchaseReturnBillQueryParam param);

    DataResponse save(PurchaseReturnBillSaveParam param);
}
