package com.regent.rbp.api.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.channel.ChannelReceiveInfo;
import com.regent.rbp.api.dto.channel.AddressData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道收货信息 Dao
 * @author: HaiFeng
 * @create: 2021-09-13 10:53
 */
public interface ChannelReceiveInfoDao extends BaseMapper<ChannelReceiveInfo> {

    List<AddressData> selectChannelReceiveInfoByChannelIds(@Param("channelIds") List<Long> channelIds);
}
