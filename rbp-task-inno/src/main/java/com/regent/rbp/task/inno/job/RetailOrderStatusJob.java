package com.regent.rbp.task.inno.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.model.param.RetailOrderStatusDownloadParam;
import com.regent.rbp.task.inno.service.RetailOrderService;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * @author liuzhicheng
 * @createTime 2022-04-15
 * @Description
 */
@Component
public class RetailOrderStatusJob {

    private static final String ERROR_PARAM_NOT_EMPTY = "[inno拉取订单列表]:拉取订单列表参数不能为空";
    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "[inno拉取订单列表]:电商平台编号不存在";

    @Value("${yumei.orderReceipt:false}")
    private boolean orderReceipt;

    @Autowired
    private RetailOrderService retailOrderService;
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    private OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;
    @Autowired
    private OnlinePlatformDao onlinePlatformDao;
    @Autowired
    private SaleOrderService saleOrderService;

    @XxlJob(SystemConstants.DOWNLOAD_ONLINE_ORDER_STATUS_LIST_JOB)
    public void downloadOnlineOrderStatusListJobHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            RetailOrderStatusDownloadParam orderParam = JSON.parseObject(param, RetailOrderStatusDownloadParam.class);
            // 参数验证
            if (null == orderParam || "{}".equals(JSONUtil.toJsonStr(orderParam))) {
                XxlJobHelper.handleFail(ERROR_PARAM_NOT_EMPTY);
                return;
            }
            // 获取电商平台信息
            OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>()
                    .eq("status", StatusEnum.CHECK.getStatus()).eq("code", orderParam.getOnlinePlatformCode()));
            if (null == onlinePlatform) {
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            // 获取任务执行缓存
            OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                    .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatform.getId()).eq(OnlinePlatformSyncCache::getSyncKey, SystemConstants.DOWNLOAD_ONLINE_ORDER_STATUS_LIST_JOB));

            if (null == orderParam.getBeginTime() && null == syncCache) {
                orderParam.setBeginTime(DateUtil.getNowDateShort());
            }
            // 设置结束时间
            orderParam.setEndTime(OptionalUtil.ofNullable(orderParam, v -> v.getEndTime() == null ? new Date() : v.getEndTime()));
            // 不存在则创建
            if (null == syncCache) {
                syncCache = OnlinePlatformSyncCache.build(onlinePlatform.getId(), SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB, DateUtil.getStartDateTimeStr(orderParam.getEndTime()));
                onlinePlatformSyncCacheDao.insert(syncCache);
            }
            // 开始时间不存在则读取缓存
            if (orderParam.getBeginTime() == null) {
                Date cacheTime = DateUtil.getDate(syncCache.getData(), SystemConstants.FULL_DATE_FORMAT);
                // 默认查询10分钟前
                orderParam.setBeginTime(new Date(cacheTime.getTime() - SystemConstants.DEFAULT_TEN_MINUTES));
            }
            //下载线上订单列表
            retailOrderService.downloadOnlineOrderStatusList(orderParam, onlinePlatform);

            // 确认收货状态推送到玉美
            if (orderReceipt) {
                this.pushOrderReceiveStatusToYuMei(onlinePlatform.getId());
            }

        } catch (BusinessException e) {
            e.printStackTrace();
            XxlJobHelper.log(e.getMessage());
            XxlJobHelper.handleFail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            XxlJobHelper.log(e.getMessage());
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    private void pushOrderReceiveStatusToYuMei(Long onlinePlatformId) {
        Object orderNoList = ThreadLocalGroup.get("yumei_receive_orderno_list");
        Set<String> orderNoList2 = (Set<String>) orderNoList;
        if (CollUtil.isNotEmpty(orderNoList2)) {
            String errorMsg = saleOrderService.pushOrderToYuMei(new ArrayList<>(orderNoList2));
            if (StrUtil.isNotEmpty(errorMsg)) {
                XxlJobHelper.log(errorMsg);
                XxlJobHelper.handleFail(errorMsg);
            }
        }
    }
}
