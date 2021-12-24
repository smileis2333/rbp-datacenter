package com.regent.rbp.api.service.purchase;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillSaveParam;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
public interface PurchaseReceiveNoticeBillService {

    /**
     * 查询
     *
     * @param param
     * @return
     */
    PageDataResponse<PurchaseReceiveNoticeBillQueryResult> query(PurchaseReceiveNoticeBillQueryParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    DataResponse save(PurchaseReceiveNoticeBillSaveParam param);
}
