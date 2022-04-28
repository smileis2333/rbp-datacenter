package com.regent.rbp.api.service.stock;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryParam;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryResult;
import com.regent.rbp.api.dto.stock.StockAdjustBillSaveParam;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
public interface StockAdjustBillService {
    PageDataResponse<StockAdjustBillQueryResult> query(StockAdjustBillQueryParam param);

    DataResponse save(StockAdjustBillSaveParam param);
}
