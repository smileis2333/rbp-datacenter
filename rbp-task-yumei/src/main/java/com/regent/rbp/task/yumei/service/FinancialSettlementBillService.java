package com.regent.rbp.task.yumei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.task.yumei.model.YumeiFinancialSettlementBill;
import com.regent.rbp.task.yumei.param.YumeiFinancialSettlementBillSaveParam;

/**
 * @author liuzhicheng
 * @createTime 2022-05-06
 * @Description
 */
public interface FinancialSettlementBillService extends IService<YumeiFinancialSettlementBill> {

    ModelDataResponse<String> save(YumeiFinancialSettlementBillSaveParam param);
}
