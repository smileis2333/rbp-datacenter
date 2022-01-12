package com.regent.rbp.api.service.channel.context;

import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.channel.ChannelBrand;
import com.regent.rbp.api.core.channel.ChannelReceiveInfo;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 渠道保存上下文对象
 * @author: HaiFeng
 * @create: 2021-09-13 13:35
 */
@Data
public class ChannelSaveContext {

    private Channel channel;
    private List<ChannelReceiveInfo> channelReceiveInfoList;
    private List<ChannelBrand> channelBrandList;

    public ChannelSaveContext() { this(null); }

    public ChannelSaveContext(ChannelSaveParam param) {
        this.channel = new Channel();
        Long userId = ThreadLocalGroup.getUserId();
        this.channel.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.channel.setCreatedBy(userId);
        this.channel.setUpdatedBy(userId);

        if(param != null) {
            this.readProperties(param);
        }
    }

    /**
     * 利用ChannelSaveParam属性值更新当前货品对象
     * @param param
     */
    public void readProperties(ChannelSaveParam param) {
        if(this.channel==null) {
            return;
        }
        this.channel.setCode(param.getChannelCode());
        this.channel.setName(param.getChannelName());
        this.channel.setFullName(param.getChannelFullName());

        this.channel.setBuildDate(param.getChannelBuildDate());

        this.channel.setAddress(param.getChannelAddress());
        this.channel.setLinkMan(param.getLinkMan());
        this.channel.setLinkManMobile(param.getLinkManMobile());

        this.channel.setMinPrice(param.getMinPrice());
        this.channel.setMaxPrice(param.getMaxPrice());
        this.channel.setStatus(param.getStatus());
    }
}
