package com.regent.rbp.task.inno.job;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.omiChannel.OnlineSyncGoodsStock;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlineSyncGoodsStockDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.param.OnlineSyncGoodsStockParam;
import com.regent.rbp.task.inno.service.OnlineSyncGoodsStockService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 上传ERP仓库库存
 *
 * @author chenchungui
 * @date 2021-09-23
 */
@Component
public class OnlineSyncGoodsStockJob {

    private static final String ERROR_PARAM_NOT_EMPTY = "[上传ERP仓库库存]:拉取订单列表参数不能为空";
    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "[上传ERP仓库库存]:电商平台编号不存在";
    private static final String ERROR_WAREHOUSE_CODE_NOT_EXIST = "[上传ERP仓库库存]:电商平台所属云仓编号不存在";
    private static final String ERROR_CHANNEL_CODE_NOT_EXIST = "[上传ERP仓库库存]:电商平台对应销售渠道编号不存在";


    @Autowired
    private OnlinePlatformDao onlinePlatformDao;
    @Autowired
    private OnlineSyncGoodsStockService onlineSyncGoodsStockService;
    @Autowired
    private OnlineSyncGoodsStockDao onlineSyncGoodsStockDao;
    @Autowired
    private BaseDbDao baseDbDao;

    /**
     * 全量更新上传inno仓库库存
     * 调用频率：凌晨2点1次
     * 入参格式：{ "onlinePlatformCode": "RBP"}
     *
     * @return
     */
    @XxlJob(SystemConstants.ONLINE_SYNC_GOODS_STOCK_FULL_JOB)
    public void onlineSyncGoodsStockFullJobHandler() {
        try {
            OnlinePlatform onlinePlatform = this.convertAndSyncData();
            if (null == onlinePlatform) {
                return;
            }
            // 全量更新库存
            onlineSyncGoodsStockService.fullSyncGoodsStockEvent(onlinePlatform);

        } catch (Exception ex) {
            XxlJobHelper.handleFail(ex.getMessage());
        }
    }

    /**
     * 定时更新上传inno仓库库存
     * 调用频率：60分钟1次
     * 入参格式：{ "onlinePlatformCode": "RBP"}
     *
     * @return
     */
    @XxlJob(SystemConstants.ONLINE_SYNC_GOODS_STOCK_JOB)
    public void onlineSyncGoodsStockJobHandler() {
        try {
            OnlinePlatform onlinePlatform = this.convertAndSyncData();
            if (null == onlinePlatform) {
                return;
            }
            // 更新库存
            onlineSyncGoodsStockService.syncGoodsStockEvent(onlinePlatform);

        } catch (Exception ex) {
            XxlJobHelper.handleFail(ex.getMessage());
        }
    }

    /**
     * 参数转换并同步数据
     */
    private OnlinePlatform convertAndSyncData() {
        //读取参数
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log(param);
        OnlineSyncGoodsStockParam stockParam = JSON.parseObject(param, OnlineSyncGoodsStockParam.class);
        // 参数验证
        if (null == stockParam || "{}".equals(JSONUtil.toJsonStr(stockParam))) {
            XxlJobHelper.handleFail(ERROR_PARAM_NOT_EMPTY);
            return null;
        }
        // 获取电商平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>()
                .select("id,warehouse_id,channel_id").eq("status", StatusEnum.CHECK.getStatus()).eq("code", stockParam.getOnlinePlatformCode()));
        if (null == onlinePlatform) {
            XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
            return null;
        }
        if (null == onlinePlatform.getWarehouseId()) {
            XxlJobHelper.handleFail(ERROR_WAREHOUSE_CODE_NOT_EXIST);
            return null;
        }
        // 电商平台编号
        String warehouseCode = baseDbDao.getStringDataBySql(String.format("SELECT code FROM rbp_warehouse WHERE id = %s", onlinePlatform.getWarehouseId()));
        if (StringUtil.isEmpty(warehouseCode)) {
            XxlJobHelper.handleFail(ERROR_WAREHOUSE_CODE_NOT_EXIST);
            return null;
        }
        onlinePlatform.setWarehouseCode(warehouseCode);
        // 销售渠道编码
        String channelCode = baseDbDao.getStringDataBySql(String.format("SELECT code FROM rbp_channel WHERE id = %s", onlinePlatform.getChannelId()));
        if (StringUtil.isEmpty(channelCode)) {
            XxlJobHelper.handleFail(ERROR_CHANNEL_CODE_NOT_EXIST);
            return null;
        }
        onlinePlatform.setChannelCode(channelCode);
        // 清空相同电商平台同步表数据
        onlineSyncGoodsStockDao.delete(new LambdaQueryWrapper<OnlineSyncGoodsStock>().eq(OnlineSyncGoodsStock::getOnlinePlatformId, onlinePlatform.getId()));
        // 同步电商平台无异常线上货品
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO rbp_online_sync_goods_stock (id,online_platform_id,barcode,quantity) ");
        sql.append(" SELECT id,online_platform_id,barcode,online_quantity from rbp_online_goods WHERE online_platform_id =  ");
        sql.append(onlinePlatform.getId()).append(" AND abnormal_flag = 0");
        baseDbDao.insertSql(sql.toString());

        return onlinePlatform;
    }
}
