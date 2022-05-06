package com.regent.rbp.task.yumei.service;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.task.yumei.model.YumeiReceiptBillSaveParam;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
public interface YumeiReceiptBillService {
    DataResponse customSave(YumeiReceiptBillSaveParam param);
}
