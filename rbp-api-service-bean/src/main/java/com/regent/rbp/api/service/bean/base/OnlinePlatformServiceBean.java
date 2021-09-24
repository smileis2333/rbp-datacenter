package com.regent.rbp.api.service.bean.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: rbp-datacenter
 * @description: 平台信息 Bean
 * @author: HaiFeng
 * @create: 2021-09-24 11:44
 */
@Service
public class OnlinePlatformServiceBean extends ServiceImpl<OnlinePlatformDao, OnlinePlatform> implements OnlinePlatformService {

    private static final String ERROR_ONLINEPLATFORMCODE = "[获取平台信息]:onlinePlatformCode电商平台编号参数值不存在";

    @Autowired
    OnlinePlatformDao onlinePlatformDao;

    @Override
    public Long getOnlinePlatformById(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().select("id", "code", "name")
                .eq("code", onlinePlatformCode));
        if(onlinePlatform == null) {
            new Exception(ERROR_ONLINEPLATFORMCODE);
        }
        return onlinePlatform.getId();
    }

    @Override
    public OnlinePlatform getOnlinePlatform(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().eq("code", onlinePlatformCode));
        if(onlinePlatform == null) {
            new Exception(ERROR_ONLINEPLATFORMCODE);
        }
        return onlinePlatform;
    }
}
