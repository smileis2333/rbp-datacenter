package com.regent.rbp.api.service.channel;

import com.regent.rbp.api.dto.channel.ChannelQueryParam;
import com.regent.rbp.api.dto.channel.ChannelQueryResult;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;

/**
 * @program: rbp-datacenter
 * @description: 渠道资料 Service
 * @author: HaiFeng
 * @create: 2021-09-11 13:35
 */
public interface ChannelService {

    /**
     * 查询
     * @param param
     * @return
     */
    PageDataResponse<ChannelQueryResult> query(ChannelQueryParam param);

    /**
     * 新增/修改
     * @param param
     * @return
     */
    DataResponse save(ChannelSaveParam param);
}
