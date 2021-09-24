package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;

/**
 * @author chenchungui
 * @date 2021/9/23
 * @description
 */
public interface OnlineSyncGoodsStockService {

    void fullSyncGoodsStockEvent(OnlinePlatform onlinePlatform);

    void syncGoodsStockEvent(OnlinePlatform onlinePlatform);

}
