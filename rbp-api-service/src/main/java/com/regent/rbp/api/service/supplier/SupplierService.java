package com.regent.rbp.api.service.supplier;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.supplier.SupplierQueryParam;
import com.regent.rbp.api.dto.supplier.SupplierQueryResult;
import com.regent.rbp.api.dto.supplier.SupplierSaveParam;

public interface SupplierService {
    PageDataResponse<SupplierQueryResult> query(SupplierQueryParam param);

    DataResponse save(SupplierSaveParam param);

}
