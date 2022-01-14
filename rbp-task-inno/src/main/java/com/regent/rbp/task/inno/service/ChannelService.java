package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.task.inno.model.param.ChannelUploadingParam;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;

/**
 * @program: rbp-datacenter
 * @description: 渠道/仓库 Service
 * @author: HaiFeng
 * @create: 2021-09-22 11:26
 */
public interface ChannelService {

    /**
     * 获取平台信息
     * @param onlinePlatformCode
     * @return
     */
    OnlinePlatform getOnlinePlatform(String onlinePlatformCode);

    /**
     * 上传 渠道信息
     * @param onlinePlatform
     * @return
     */
    ChannelRespDto uploadingChannel(OnlinePlatform onlinePlatform);

    /**
     * 上传 云仓相关的仓库信息
     * @param onlinePlatform
     * @return
     */
    ChannelRespDto uploadingWarehouse(OnlinePlatform onlinePlatform);

    /**
     * 上传 云仓信息
     * @param onlinePlatform
     * @return
     */
    ChannelRespDto uploadingCloudWarehouse(OnlinePlatform onlinePlatform);
}
