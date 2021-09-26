package com.regent.rbp.api.service.base;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 线上平台同步数据的缓存
 * @author: HaiFeng
 * @create: 2021-09-24 11:20
 */
public interface OnlinePlatformSyncCacheService {

    /**
     * 查询中间配置表最大时间
     * @param onlinePlatformId
     * @param key
     * @return
     */
    Date getOnlinePlatformSyncCacheByDate(Long onlinePlatformId, String key);

    /**
     * 新增/修改中间配置表
     * @param onlinePlatformId
     * @param key
     * @param uploadingTime
     */
    void saveOnlinePlatformSyncCache(Long onlinePlatformId, String key, Date uploadingTime);

}
