package com.regent.rbp.api.dao.channel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.channel.ChannelBrand;
import com.regent.rbp.api.dto.channel.ChannelBrandDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道品牌关系 Dao
 * @author: HaiFeng
 * @create: 2021-09-11 16:07
 */
public interface ChannelBrandDao extends BaseMapper<ChannelBrand> {

    List<ChannelBrandDto> selectChannelBrandDtoByChannelIds(@Param("channelIds") List<Long> channelIds);
}
