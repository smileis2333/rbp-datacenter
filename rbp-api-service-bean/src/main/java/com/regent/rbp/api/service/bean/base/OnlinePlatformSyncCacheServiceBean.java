package com.regent.rbp.api.service.bean.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.SizeClass;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.base.SizeClassDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 线上平台同步数据的缓存 Bean
 * @author: HaiFeng
 * @create: 2021-09-24 11:21
 */
@Service
public class OnlinePlatformSyncCacheServiceBean extends ServiceImpl<OnlinePlatformSyncCacheDao, OnlinePlatformSyncCache> implements OnlinePlatformSyncCacheService {

    @Autowired
    OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;

    @Override
    public Date getOnlinePlatformSyncCacheByDate(Long onlinePlatformId, String key) {
        // 获取任务执行缓存
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            return null;
        }
        Date cacheTime = DateUtil.getDate(syncCache.getData(), DateUtil.FULL_DATE_FORMAT);
        // 默认查询10分钟前
        Date time = new Date(cacheTime.getTime() - SystemConstants.DEFAULT_TEN_MINUTES);
        return time;
    }

    @Override
    public void saveOnlinePlatformSyncCache(Long onlinePlatformId, String key, Date uploadingTime) {
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            syncCache.build(onlinePlatformId, key, DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.insert(syncCache);
        } else {
            syncCache.setData(DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.updateById(syncCache);
        }
    }
}
