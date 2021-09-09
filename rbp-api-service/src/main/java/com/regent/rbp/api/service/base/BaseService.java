package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.BaseData;
import com.regent.rbp.api.dto.base.BaseQueryParam;
import com.regent.rbp.api.dto.base.BaseSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 基础资料 接口
 */
public interface BaseService {

    PageDataResponse<BaseData> searchPageData(BaseQueryParam param);

    DataResponse batchCreate(BaseSaveParam param);

}
