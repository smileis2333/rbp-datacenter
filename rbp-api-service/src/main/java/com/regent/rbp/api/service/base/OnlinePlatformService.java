package com.regent.rbp.api.service.base;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;

/**
 * @program: rbp-datacenter
 * @description: 平台信息 Service
 * @author: HaiFeng
 * @create: 2021-09-24 11:39
 */
public interface OnlinePlatformService {

    Long getOnlinePlatformById(String onlinePlatformCode);

    OnlinePlatform getOnlinePlatform(String onlinePlatformCode);
}
