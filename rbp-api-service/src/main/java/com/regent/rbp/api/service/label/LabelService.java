package com.regent.rbp.api.service.label;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.label.LabelQueryParam;
import com.regent.rbp.api.dto.label.LabelSaveParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
public interface LabelService {
    PageDataResponse<PurchaseReturnBillQueryResult> query(LabelQueryParam param);

    DataResponse save(LabelSaveParam param);
}
