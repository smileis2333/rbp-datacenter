package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;

import java.util.Map;

/**
 * @author chenchungui
 * @date 2021/9/22
 * @description
 */
public interface RetailOrderService {

    void downloadOnlineOrderList(RetailOrderDownloadOnlineOrderParam param, OnlinePlatform onlinePlatform) throws Exception;



}
