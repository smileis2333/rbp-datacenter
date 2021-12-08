package com.regent.rbp.api.service.notice;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.notice.NoticeBillQueryParam;
import com.regent.rbp.api.dto.notice.NoticeBillQueryResult;
import com.regent.rbp.api.dto.notice.NoticeBillSaveParam;

/**
 * @author chenchungui
 * @date 2021/12/7
 * @description
 */
public interface NoticeBillService {

    /**
     * 查询
     *
     * @param param
     * @return
     */
    PageDataResponse<NoticeBillQueryResult> query(NoticeBillQueryParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    DataResponse save(NoticeBillSaveParam param);

}
