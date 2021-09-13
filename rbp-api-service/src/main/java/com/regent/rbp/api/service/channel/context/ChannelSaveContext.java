package com.regent.rbp.api.service.channel.context;

import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.channel.ChannelBrand;
import com.regent.rbp.api.core.channel.ChannelReceiveInfo;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.goods.GoodsPriceDto;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
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
        long userId = ThreadLocalGroup.getUserId();
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
        this.channel.setFullName(param.getFundAccount());

        Date buildDate = DateUtil.getDate(param.getChannelBuildDate(), DateUtil.FULL_DATE_FORMAT);
        this.channel.setBuildDate(buildDate);

        this.channel.setAddress(param.getChannelAddress());
        this.channel.setLinkMan(param.getLinkMan());
        this.channel.setLinkManMobile(param.getLinkManMobile());

        this.channel.setMinPrice(param.getMinPrice());
        this.channel.setMaxPrice(param.getMaxPrice());
        if (param.getPhysicalRegion() != null) {
            this.channel.setNation(param.getPhysicalRegion().getNation());
            this.channel.setRegion(param.getPhysicalRegion().getRegion());
            this.channel.setProvince(param.getPhysicalRegion().getProvince());
            this.channel.setCity(param.getPhysicalRegion().getCity());
            this.channel.setCounty(param.getPhysicalRegion().getCounty());
        }
        this.channel.setStatus(0);
    }
}
