package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.model.param.RetailOrderStatusDownloadParam;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/22
 * @description
 */
public interface RetailOrderService {

    void downloadOnlineOrderList(RetailOrderDownloadOnlineOrderParam param, OnlinePlatform onlinePlatform) throws Exception;

    /**
     * 下载订单状态
     *
     * @param param
     * @param onlinePlatform
     * @throws Exception
     */
    void downloadOnlineOrderStatusList(RetailOrderStatusDownloadParam param, OnlinePlatform onlinePlatform) throws Exception;

    /**
     * 下载已收货订单
     *
     * @param param
     * @param onlinePlatform
     * @return key:单号  value:收货时间
     * @throws Exception
     */
    List<String> downloadOnlineOrderReceivedList(RetailOrderStatusDownloadParam param, OnlinePlatform onlinePlatform) throws Exception;

}
