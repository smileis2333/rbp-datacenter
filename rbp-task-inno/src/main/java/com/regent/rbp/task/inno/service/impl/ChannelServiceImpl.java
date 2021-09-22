package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.core.warehouse.WarehouseChannelRange;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.dao.warehouse.WarehouseChannelRangeDao;
import com.regent.rbp.api.dao.warehouse.WarehouseDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.ChannelDto;
import com.regent.rbp.task.inno.model.dto.WarehouseDto;
import com.regent.rbp.task.inno.model.req.ChannelReqDto;
import com.regent.rbp.task.inno.model.req.WarehouseReqDto;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 渠道/仓库 Impl
 * @author: HaiFeng
 * @create: 2021-09-22 11:37
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    private static final String POST_ERP_STORE = "api/Store/Post_Erp_Store";
    private static final String POST_ERP_WAREHOUSE = "api/WareHouse/Post_Erp_Warehouse";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    ChannelDao channelDao;
    @Autowired
    OnlinePlatformDao onlinePlatformDao;
    @Autowired
    WarehouseDao warehouseDao;
    @Autowired
    WarehouseChannelRangeDao warehouseChannelRangeDao;
    @Autowired
    OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;


    @Override
    public OnlinePlatform getOnlinePlatform(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().select("id", "code", "name")
                .eq("code", onlinePlatformCode));

        return onlinePlatform;
    }

    @Transactional
    @Override
    public ChannelRespDto uploadingChannel(Long onlinePlatformId, Long channelId) {
        ChannelRespDto respDto = null;
        String key = SystemConstants.POST_ERP_STORE;
        Channel channel = channelDao.selectById(channelId);
        if (channel != null) {
            Date uploadingDate = this.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);
            if (uploadingDate != null && channel.getUpdatedTime().getTime() <= uploadingDate.getTime())
            {
                return respDto;
            }
            List<ChannelDto> reqList = new ArrayList<>();
            Integer isEnabled = 0;
            if (channel.getStatus() == 1)
                isEnabled = 1;
            BigDecimal latitude = new BigDecimal(0);
            ChannelDto channelDto = new ChannelDto(channel.getId().toString(), channel.getName(), channel.getLinkManMobile(), channel.getLinkMan(), channel.getCode(),
                    channel.getAddress(), "", isEnabled.toString(),  channel.getCode(), new BigDecimal(0), new BigDecimal(0));
            reqList.add(channelDto);

            ChannelReqDto channelReqDto = new ChannelReqDto();
            channelReqDto.setApp_key(innoConfig.getAppkey());
            channelReqDto.setApp_secrept(innoConfig.getAppsecret());
            channelReqDto.setData(reqList);

            String api_url = String.format("%s%s", innoConfig.getUrl(), POST_ERP_STORE);
            String result = HttpUtil.post(api_url, JSON.toJSONString(channelReqDto));

            respDto = JSON.parseObject(result, ChannelRespDto.class);

            Date uploadingTime = channel.getUpdatedTime();
            this.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
        return respDto;
    }

    @Transactional
    @Override
    public ChannelRespDto uploadingWarehouse(Long onlinePlatformId, Long warehouseId) {
        ChannelRespDto respDto = null;
        String key = SystemConstants.POST_ERP_WAREHOUSE;
        List<WarehouseChannelRange> warehouseChannelRangeList = warehouseChannelRangeDao.selectList(
                new QueryWrapper<WarehouseChannelRange>().eq("warehouse_id", warehouseId));
        if (warehouseChannelRangeList != null && warehouseChannelRangeList.size() > 0) {
            List<Long> channelIds = warehouseChannelRangeList.stream().map(WarehouseChannelRange::getChannelId).collect(Collectors.toList());

            Date uploadingDate = this.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

            QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
            queryWrapper.in("id", channelIds);
            if (uploadingDate != null) {
                queryWrapper.le("updated_time", uploadingDate);
            }

            List<Channel> channelList = channelDao.selectList(queryWrapper);
            List<WarehouseDto> warehouseDtoList = new ArrayList<>();
            for (Channel item : channelList) {
                WarehouseDto warehouse = new WarehouseDto(item.getId().toString(), item.getRegion(), item.getCode(), item.getLinkMan(), item.getName(), item.getLinkManMobile(), item.getAddress());
                warehouseDtoList.add(warehouse);
            }

            WarehouseReqDto warehouseReqDto = new WarehouseReqDto();
            warehouseReqDto.setApp_key(innoConfig.getAppkey());
            warehouseReqDto.setApp_secrept(innoConfig.getAppsecret());
            warehouseReqDto.setData(warehouseDtoList);

            String api_url = String.format("%s%s", innoConfig.getUrl(), POST_ERP_WAREHOUSE);
            String result = HttpUtil.post(api_url, JSON.toJSONString(warehouseReqDto));

            respDto = JSON.parseObject(result, ChannelRespDto.class);

            Date uploadingTime = channelList.stream().max(Comparator.comparing(Channel::getUpdatedTime)).get().getUpdatedTime();
            this.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
        return respDto;
    }

    /**
     * 查询中间配置表最大时间
     * @param onlinePlatformId
     * @param key
     * @return
     */
    private Date getOnlinePlatformSyncCacheByDate(Long onlinePlatformId, String key) {
        // 获取任务执行缓存
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            return null;
        }
        Date cacheTime = DateUtil.getDate(syncCache.getData(), DateUtil.FULL_DATE_FORMAT);
        // 默认查询10分钟前
        Date time = new Date(cacheTime.getTime() - SystemConstants.DEFAULT_TEN_MINUTES);
        return time;
    }

    /**
     * 新增/修改中间配置表
     * @param onlinePlatformId
     * @param key
     * @param uploadingTime
     */
    private void saveOnlinePlatformSyncCache(Long onlinePlatformId, String key, Date uploadingTime) {
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            syncCache.build(onlinePlatformId, key, DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.insert(syncCache);
        } else {
            syncCache.setData(DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.updateById(syncCache);
        }

    }

}
