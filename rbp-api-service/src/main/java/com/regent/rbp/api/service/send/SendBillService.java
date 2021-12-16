package com.regent.rbp.api.service.send;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.send.SendBillQueryParam;
import com.regent.rbp.api.dto.send.SendBillQueryResult;
import com.regent.rbp.api.dto.send.SendBillSaveParam;

/**
 * @author chenchungui
 * @date 2021/12/16
 * @description
 */
public interface SendBillService {

    /**
     * 查询
     *
     * @param param
     * @return
     */
    PageDataResponse<SendBillQueryResult> query(SendBillQueryParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    DataResponse save(SendBillSaveParam param);

}
