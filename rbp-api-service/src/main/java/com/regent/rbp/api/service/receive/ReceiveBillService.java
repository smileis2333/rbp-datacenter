package com.regent.rbp.api.service.receive;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryParam;
import com.regent.rbp.api.dto.receive.ReceiveBillQueryResult;
import com.regent.rbp.api.dto.receive.ReceiveBillSaveParam;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
public interface ReceiveBillService {
    /**
     * 查询
     */
    PageDataResponse<ReceiveBillQueryResult> query(ReceiveBillQueryParam param);

    /**
     * 新增
     */
    DataResponse save(ReceiveBillSaveParam param);

}
