package com.regent.rbp.api.service.base;

import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 线上平台同步数据失败列表 Service
 * @author: HaiFeng
 * @create: 2022/1/20 17:50
 */
public interface OnlinePlatformSyncErrorService {

    /**
     * 获得推送失败集合
     * @param key
     * @return
     */
    Map<String, Long> getErrorBillId(String key);

    /**
     * 写入错误信息
     * @param onlinePlatformId
     * @param key
     * @param data
     */
    void saveOnlinePlatformSyncError(Long onlinePlatformId, String key, String data);

    /**
     * 成功
     * @param id
     */
    void succeed(Long id);

    /**
     * 失败
     * @param id
     */
    void failure(Long id);
}
