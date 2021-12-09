package com.regent.rbp.api.service.sale;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.*;


public interface SalesPlanBillService {

    PageDataResponse<SalesPlanBillQueryResult> query(SalePlanQueryParam param);

    DataResponse save(SalePlanSaveParam param);
}
