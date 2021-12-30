package com.regent.rbp.api.service.purchase;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveBillSaveParam;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
public interface PurchaseReceiveBillService {

    PageDataResponse<PurchaseReceiveBillQueryResult> query(PurchaseReceiveBillQueryParam param);

    DataResponse save(PurchaseReceiveBillSaveParam param);
}
