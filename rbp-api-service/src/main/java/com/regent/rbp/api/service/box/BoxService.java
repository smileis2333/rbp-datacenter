package com.regent.rbp.api.service.box;

import com.regent.rbp.api.dto.box.BoxQueryParam;
import com.regent.rbp.api.dto.box.BoxQueryResult;
import com.regent.rbp.api.dto.box.BoxSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
public interface BoxService {
    PageDataResponse<BoxQueryResult> query(BoxQueryParam param);

    DataResponse save(BoxSaveParam param);

}
