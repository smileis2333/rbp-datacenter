package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailOrderBillUpdateParam;

/**
 * @author chenchungui
 * @date 2021/9/14
 * @description 全渠道订单 接口
 */
public interface RetailOrderBillService {

    ModelDataResponse<String> save(RetailOrderBillSaveParam param);

    DataResponse updateStatus(RetailOrderBillUpdateParam param);
}
