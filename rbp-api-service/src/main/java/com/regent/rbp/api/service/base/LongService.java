package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.LongDeleteParam;
import com.regent.rbp.api.dto.base.LongQueryParam;
import com.regent.rbp.api.dto.base.LongSaveParam;
import com.regent.rbp.api.dto.base.LongUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 接口
 */
public interface LongService {

    PageDataResponse<String> searchPageData(LongQueryParam param);

    DataResponse batchCreate(LongSaveParam param);

    DataResponse batchUpdate(LongUpdateParam param);

    DataResponse batchDelete(LongDeleteParam param);
}
