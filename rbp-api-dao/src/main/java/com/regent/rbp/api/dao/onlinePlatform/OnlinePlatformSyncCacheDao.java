package com.regent.rbp.api.dao.onlinePlatform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;

/**
 * 线上平台同步数据的缓存
 * 场景：
 * 1.拉取平台数据，记录接口的最大拉取时间。
 *
 * @author chenchungui
 * @date 2021-09-22
 */
public interface OnlinePlatformSyncCacheDao extends BaseMapper<OnlinePlatformSyncCache> {
}
