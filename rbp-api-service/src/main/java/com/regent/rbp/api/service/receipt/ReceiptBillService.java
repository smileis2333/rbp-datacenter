package com.regent.rbp.api.service.receipt;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryParam;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryResult;
import com.regent.rbp.api.dto.receipt.ReceiptBillSaveParam;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
public interface ReceiptBillService {
    DataResponse save(ReceiptBillSaveParam param);

    PageDataResponse<ReceiptBillQueryResult> query(ReceiptBillQueryParam param);
}
