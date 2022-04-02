package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiSaveParam;

/**
 * @author liuzhicheng
 * @createTime 2022-04-01
 * @Description
 */
public interface SalesSendYuMeiService {

    ModelDataResponse<String> save(SalesSendYuMeiSaveParam param);
}
