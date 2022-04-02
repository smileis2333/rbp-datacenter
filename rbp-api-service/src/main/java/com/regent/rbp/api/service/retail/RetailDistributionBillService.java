package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailDistributionBillSaveParam;

/**
 * @author liuzhicheng
 * @createTime 2022-03-31
 * @Description
 */
public interface RetailDistributionBillService {

    ModelDataResponse<String> save(RetailDistributionBillSaveParam param);
}
