package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.BusinessFormatType;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.channel.ChannelBalanceType;
import com.regent.rbp.api.core.channel.ChannelBusinessFormat;
import com.regent.rbp.api.core.eum.BusinessFormatTypeEnum;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.core.warehouse.Warehouse;
import com.regent.rbp.api.core.warehouse.WarehouseChannelRange;
import com.regent.rbp.api.dao.base.BusinessFormatTypeDao;
import com.regent.rbp.api.dao.channel.ChannelBalanceTypeDao;
import com.regent.rbp.api.dao.channel.ChannelBusinessFormatDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.dao.warehouse.WarehouseChannelRangeDao;
import com.regent.rbp.api.dao.warehouse.WarehouseDao;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.ChannelDto;
import com.regent.rbp.task.inno.model.dto.WarehouseDto;
import com.regent.rbp.task.inno.model.req.ChannelReqDto;
import com.regent.rbp.task.inno.model.req.WarehouseReqDto;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.service.ChannelService;
import com.xxl.job.core.context.XxlJobHelper;
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
    ChannelDao channelDao;
    @Autowired
    OnlinePlatformDao onlinePlatformDao;
    @Autowired
    WarehouseDao warehouseDao;
    @Autowired
    WarehouseChannelRangeDao warehouseChannelRangeDao;
    @Autowired
    ChannelBusinessFormatDao channelBusinessFormatDao;

    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;


    @Override
    public OnlinePlatform getOnlinePlatform(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().select("id", "code", "name")
                .eq("code", onlinePlatformCode));

        return onlinePlatform;
    }

    @Transactional
    @Override
    public ChannelRespDto uploadingChannel(OnlinePlatform onlinePlatform) {
        Long onlinePlatformId = onlinePlatform.getId();
        Long channelId = onlinePlatform.getWarehouseId();
        ChannelRespDto respDto = null;
        String key = SystemConstants.POST_ERP_STORE;
        List<WarehouseChannelRange> warehouseChannelRangeList = warehouseChannelRangeDao.selectList(
                new QueryWrapper<WarehouseChannelRange>().eq("warehouse_id", channelId));
        if (warehouseChannelRangeList != null && warehouseChannelRangeList.size() > 0) {
            List<Long> channelIds = warehouseChannelRangeList.stream().map(WarehouseChannelRange::getChannelId).collect(Collectors.toList());

            Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

            QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
            queryWrapper.in("id", channelIds);
            if (uploadingDate != null) {
                queryWrapper.ge("updated_time", uploadingDate);
            }

            List<Long> channelBusinessFormatIds = this.getChannelBusinessFormat(BusinessFormatTypeEnum.STORE.getKey());
            queryWrapper.in("business_format_id", channelBusinessFormatIds);

            List<Channel> channelList = channelDao.selectList(queryWrapper);
            List<ChannelDto> reqList = new ArrayList<>();
            for (Channel item : channelList) {
                ChannelDto channelDto = new ChannelDto(item.getId().toString(), item.getName(), item.getLinkManMobile(), item.getLinkMan(), item.getCode(),
                        item.getAddress(), "", item.toString(),  item.getCode(), new BigDecimal(0), new BigDecimal(0));
                reqList.add(channelDto);
            }

            ChannelReqDto channelReqDto = new ChannelReqDto();
            channelReqDto.setApp_key(onlinePlatform.getAppKey());
            channelReqDto.setApp_secrept(onlinePlatform.getAppSecret());
            channelReqDto.setData(reqList);

            String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_ERP_STORE);
            String result = HttpUtil.post(api_url, JSON.toJSONString(channelReqDto));

            respDto = JSON.parseObject(result, ChannelRespDto.class);

            Date uploadingTime = channelList.stream().max(Comparator.comparing(Channel::getUpdatedTime)).get().getUpdatedTime();
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
        return respDto;
    }

    @Transactional
    @Override
    public ChannelRespDto uploadingWarehouse(OnlinePlatform onlinePlatform) {
        Long onlinePlatformId = onlinePlatform.getId();
        Long warehouseId = onlinePlatform.getWarehouseId();
        ChannelRespDto respDto = null;
        String key = SystemConstants.POST_ERP_STORE;

        List<WarehouseChannelRange> warehouseChannelRangeList = warehouseChannelRangeDao.selectList(
                new QueryWrapper<WarehouseChannelRange>().eq("warehouse_id", warehouseId));
        if (warehouseChannelRangeList != null && warehouseChannelRangeList.size() > 0) {
            List<Long> channelIds = warehouseChannelRangeList.stream().map(WarehouseChannelRange::getChannelId).collect(Collectors.toList());

            Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

            QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
            queryWrapper.in("id", channelIds);
            if (uploadingDate != null) {
                queryWrapper.ge("updated_time", uploadingDate);
            }
            queryWrapper.in("business_format_id", this.getChannelBusinessFormat(BusinessFormatTypeEnum.WAREHOUSE.getKey()));

            List<Channel> channelList = channelDao.selectList(queryWrapper);
            List<WarehouseDto> warehouseDtoList = new ArrayList<>();
            Integer i = 1;
            for (Channel item : channelList) {
                WarehouseDto warehouse = new WarehouseDto(i.toString(), item.getRegion(), item.getCode(), item.getLinkMan(), item.getName(), item.getLinkManMobile(), item.getAddress());
                warehouseDtoList.add(warehouse);
                i++;
            }

            WarehouseReqDto warehouseReqDto = new WarehouseReqDto();
            warehouseReqDto.setApp_key(onlinePlatform.getAppKey());
            warehouseReqDto.setApp_secrept(onlinePlatform.getAppSecret());
            warehouseReqDto.setData(warehouseDtoList);

            String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_ERP_WAREHOUSE);
            String result = HttpUtil.post(api_url, JSON.toJSONString(warehouseReqDto));

            respDto = JSON.parseObject(result, ChannelRespDto.class);

            Date uploadingTime = channelList.stream().max(Comparator.comparing(Channel::getUpdatedTime)).get().getUpdatedTime();
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
        return respDto;
    }

    @Transactional
    @Override
    public ChannelRespDto uploadingCloudWarehouse(OnlinePlatform onlinePlatform) {
        Long onlinePlatformId = onlinePlatform.getId();
        Long warehouseId = onlinePlatform.getWarehouseId();
        ChannelRespDto respDto = null;
        String key = SystemConstants.POST_ERP_STORE;
        Warehouse warehouse = warehouseDao.selectById(onlinePlatform.getWarehouseId());
        List<WarehouseDto> warehouseDtoList = new ArrayList<>();
        WarehouseDto warehouseItem = new WarehouseDto("1", "", warehouse.getCode(), "", warehouse.getName(), "", "");
        warehouseDtoList.add(warehouseItem);

        WarehouseReqDto warehouseReqDto = new WarehouseReqDto();
        warehouseReqDto.setApp_key(onlinePlatform.getAppKey());
        warehouseReqDto.setApp_secrept(onlinePlatform.getAppSecret());
        warehouseReqDto.setData(warehouseDtoList);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_ERP_WAREHOUSE);
        String result = HttpUtil.post(api_url, JSON.toJSONString(warehouseReqDto));

        respDto = JSON.parseObject(result, ChannelRespDto.class);

        return respDto;
    }

    private List<Long> getChannelBusinessFormat(String code) {
        List<ChannelBusinessFormat> list = channelBusinessFormatDao.selectList(new QueryWrapper<ChannelBusinessFormat>().eq("format_type_code", code));
        return list.stream().map(ChannelBusinessFormat::getId).collect(Collectors.toList());
    }

}
