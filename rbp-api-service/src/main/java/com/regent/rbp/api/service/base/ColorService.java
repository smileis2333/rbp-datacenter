package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.ColorData;
import com.regent.rbp.api.dto.base.ColorDeleteParam;
import com.regent.rbp.api.dto.base.ColorQueryParam;
import com.regent.rbp.api.dto.base.ColorSaveParam;
import com.regent.rbp.api.dto.base.ColorUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author chenchungui
 * @date 2021/9/9
 * @description 颜色 接口
 */
public interface ColorService {

    PageDataResponse<ColorData> searchPageData(ColorQueryParam param);

    DataResponse batchCreate(ColorSaveParam param);

    DataResponse batchUpdate(ColorUpdateParam param);

    DataResponse batchDelete(ColorDeleteParam param);
}
