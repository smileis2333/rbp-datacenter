package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单 Service
 * @author: HaiFeng
 * @create: 2021-09-27 14:21
 */
public interface RetailReturnNoticeBillService {

    /**
     * 新增
     * @param param
     * @return
     */
    ModelDataResponse<String> save(RetailReturnNoticeBillSaveParam param);
}
