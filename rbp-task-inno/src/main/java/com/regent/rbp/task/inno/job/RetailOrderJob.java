package com.regent.rbp.task.inno.job;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.service.RetailOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author chenchungui
 * @date 2021-09-22
 */
@Component
public class RetailOrderJob {

    private static final String ERROR_PARAM_NOT_EMPTY = "[inno拉取订单列表]:拉取订单列表参数不能为空";
    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "[inno拉取订单列表]:电商平台编号不存在";
    private static final String ERROR_BEGIN_TIME_NOT_EMPTY = "[inno拉取订单列表]:第一次拉取订单列表开始时间不能为空";


    @Autowired
    private RetailOrderService retailOrderService;
    @Autowired
    private OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;
    @Autowired
    private OnlinePlatformDao onlinePlatformDao;

    /**
     * 拉取订单列表
     * 调用频率：60分钟1次
     * 入参格式：{ "onlinePlatformCode": "RBP","beginTime": "2021-01-01 00:00:01","endTime": "2021-01-02 00:00:01","order_sn_list": "201510010002,201510010003"}
     *
     * @return
     */
    @XxlJob(SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB)
    public void downloadOnlineOrderListJobHandler() {

        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            RetailOrderDownloadOnlineOrderParam orderParam = JSON.parseObject(param, RetailOrderDownloadOnlineOrderParam.class);
            // 参数验证
            if (null == orderParam || "{}".equals(JSONUtil.toJsonStr(orderParam))) {
                XxlJobHelper.handleFail(ERROR_PARAM_NOT_EMPTY);
                return;
            }
            // 获取电商平台信息
            OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>()
                    .select("id,channel_id").eq("status", StatusEnum.CHECK.getStatus()).eq("code", orderParam.getOnlinePlatformCode()));
            if (null == onlinePlatform) {
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            // 获取任务执行缓存
            OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                    .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatform.getId()).eq(OnlinePlatformSyncCache::getSyncKey, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB));
            if (StringUtil.isEmpty(orderParam.getBeginTime()) && null == syncCache) {
                XxlJobHelper.handleFail(ERROR_BEGIN_TIME_NOT_EMPTY);
                return;
            }
            // 设置结束时间
            orderParam.setEndTime(OptionalUtil.ofNullable(orderParam, v -> StringUtil.isEmpty(v.getBeginTime()) ? DateUtil.getNowDateString(SystemConstants.FULL_DATE_FORMAT) : v.getBeginTime()));
            // 不存在则创建
            if (null == syncCache) {
                syncCache = OnlinePlatformSyncCache.build(onlinePlatform.getId(), SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB, orderParam.getEndTime());
                onlinePlatformSyncCacheDao.insert(syncCache);
            }
            // 开始时间不存在则读取缓存
            if (StringUtil.isEmpty(orderParam.getBeginTime())) {
                Date cacheTime = DateUtil.getDate(syncCache.getData(), SystemConstants.FULL_DATE_FORMAT);
                // 默认查询10分钟前
                orderParam.setBeginTime(DateUtil.getDateStr(new Date(cacheTime.getTime() - SystemConstants.DEFAULT_TEN_MINUTES), SystemConstants.FULL_DATE_FORMAT));
            }
            //下载线上订单列表
            retailOrderService.downloadOnlineOrderList(orderParam, onlinePlatform);

        } catch (Exception ex) {
            XxlJobHelper.handleFail(ex.getMessage());
        }
    }
}