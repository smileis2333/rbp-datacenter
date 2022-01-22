package com.regent.rbp.api.service.bean.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncError;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncErrorDao;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 线上平台同步数据失败列表 Bean
 * @author: HaiFeng
 * @create: 2022/1/20 17:52
 */
@Service
public class OnlinePlatformSyncErrorServiceBean extends ServiceImpl<OnlinePlatformSyncErrorDao, OnlinePlatformSyncError> implements OnlinePlatformSyncErrorService {

    @Autowired
    OnlinePlatformSyncErrorDao onlinePlatformSyncErrorDao;

    @Override
    public Map<String, Long> getErrorBillId(String key) {
        Integer chance = 5;
        Map<String, Long> map = new HashMap<>();
        List<OnlinePlatformSyncError> list = onlinePlatformSyncErrorDao.selectList(new LambdaQueryWrapper<OnlinePlatformSyncError>()
                .eq(OnlinePlatformSyncError::getSyncKey, key).eq(OnlinePlatformSyncError::getSuccessFlag, false)
                .le(OnlinePlatformSyncError::getRetryTimes, chance));
        if (list != null && list.size() > 0) {
            map = list.stream().collect(Collectors.toMap(v -> v.getData(), v -> v.getId(), (x1, x2) -> x1));
        }
        return map;
    }

    @Override
    public void saveOnlinePlatformSyncError(Long onlinePlatformId, String key, String data) {
        OnlinePlatformSyncError error = new OnlinePlatformSyncError();
        error.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        error.setOnlinePlatformId(onlinePlatformId);
        error.setSyncKey(key);
        error.setData(data);
        error.setRetryTimes(0);
        error.setSuccessFlag(false);
        onlinePlatformSyncErrorDao.insert(error);
    }

    @Override
    public void succeed(Long id) {
        OnlinePlatformSyncError error = onlinePlatformSyncErrorDao.selectById(id);
        error.setSuccessFlag(true);
        error.setUpdatedTime(new Date());
        onlinePlatformSyncErrorDao.updateById(error);
    }

    @Override
    public void failure(Long id) {
        OnlinePlatformSyncError error = onlinePlatformSyncErrorDao.selectById(id);
        error.setRetryTimes(error.getRetryTimes() + 1);
        error.setUpdatedTime(new Date());
        onlinePlatformSyncErrorDao.updateById(error);
    }


}
