package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillSaveParam;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
public interface RetailSalesSendBillService {

    ModelDataResponse<String> save(RetailSalesSendBillSaveParam param);
}
