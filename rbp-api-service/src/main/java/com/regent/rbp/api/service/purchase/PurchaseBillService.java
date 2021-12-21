package com.regent.rbp.api.service.purchase;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseBillSaveParam;

/**
 * @author chenchungui
 * @date 2021/12/21
 * @description
 */
public interface PurchaseBillService {

    /**
     * 查询
     *
     * @param param
     * @return
     */
    PageDataResponse<PurchaseBillQueryResult> query(PurchaseBillQueryParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    DataResponse save(PurchaseBillSaveParam param);

}
