package com.regent.rbp.api.service.barcode;

import com.regent.rbp.api.dto.base.BarcodeQueryResult;
import com.regent.rbp.api.dto.base.BarcodeQueryParam;
import com.regent.rbp.api.dto.base.BarcodeSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
public interface BarcodeService {

    PageDataResponse<BarcodeQueryResult> searchPageData(BarcodeQueryParam param);

    DataResponse batchCreate(BarcodeSaveParam param);

}
