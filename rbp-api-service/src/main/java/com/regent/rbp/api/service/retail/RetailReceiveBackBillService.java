package com.regent.rbp.api.service.retail;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReceiveBackBillSaveParam;

/**
 * @author liuzhicheng
 * @createTime 2022-04-03
 * @Description
 */
public interface RetailReceiveBackBillService extends IService<RetailReceiveBackBill> {

    ModelDataResponse<String> save(RetailReceiveBackBillSaveParam param);
}
