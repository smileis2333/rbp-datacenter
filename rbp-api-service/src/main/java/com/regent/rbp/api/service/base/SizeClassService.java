package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.SizeClassData;
import com.regent.rbp.api.dto.base.SizeClassDeleteParam;
import com.regent.rbp.api.dto.base.SizeClassQueryParam;
import com.regent.rbp.api.dto.base.SizeClassSaveParam;
import com.regent.rbp.api.dto.base.SizeClassUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 尺码 接口
 */
public interface SizeClassService {

    PageDataResponse<SizeClassData> searchPageData(SizeClassQueryParam param);

    DataResponse batchCreate(SizeClassSaveParam param);

    DataResponse batchUpdate(SizeClassUpdateParam param);

    DataResponse batchDelete(SizeClassDeleteParam param);
}
