package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailReceiveBackBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.UpdateReturnOrderStatusDto;
import com.regent.rbp.task.inno.model.dto.UpdateReturnOrderStatusReqDto;
import com.regent.rbp.task.inno.model.param.UpdateRetailReceiveBackByStatusParam;
import com.regent.rbp.task.inno.model.req.RetailReturnNoticeReqDto;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.model.resp.UpdateReturnOrderStatusRespDto;
import com.regent.rbp.task.inno.service.RetailReceiveBackService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 全渠道收退货单 Impl
 * @author: HaiFeng
 * @create: 2021-10-14 17:54
 */
@Service
public class RetailReceiveBackServiceImpl implements RetailReceiveBackService {

    private static final String API_URL_UPDATERETURNORDERSTATUS= "api/ReturnOrder/Post_UpdateReturnOrderStatus";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;

    @Autowired
    RetailReceiveBackBillDao retailReceiveBackBillDao;
    @Autowired
    RetailReturnNoticeBillDao retailReturnNoticeBillDao;
    @Autowired
    RetailOrderBillDao retailOrderBillDao;

    @Override
    public void UpdateReturnOrderStatus(UpdateRetailReceiveBackByStatusParam param) {
        String key = SystemConstants.POST_RETURN_ORDER_STATUS;
        Long onlinePlatformId = onlinePlatformService.getOnlinePlatformById(param.getOnlinePlatformCode());

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

        // 得到 inno 平台下的全渠道订单
        List<RetailOrderBill> orderBillList = retailOrderBillDao.selectList(new QueryWrapper<RetailOrderBill>().eq("online_platform_id", onlinePlatformId));
        if (orderBillList == null || orderBillList.size() == 0) {
            XxlJobHelper.log("未找到 inno 平台下的全渠道订单");
            return;
        }
        List<Long> orderIds = orderBillList.stream().map(RetailOrderBill::getId).collect(Collectors.toList());
        // 得到 inno 平台下的退货通知单
        List<RetailReturnNoticeBill> returnNoticeBillList = retailReturnNoticeBillDao.selectList(new QueryWrapper<RetailReturnNoticeBill>().in("retail_order_bill_id", orderIds));
        List<Long> returnNoticeIds = returnNoticeBillList.stream().map(RetailReturnNoticeBill::getId).collect(Collectors.toList());
        if (orderBillList == null || orderBillList.size() == 0) {
            XxlJobHelper.log("未找到 inno 平台下的退货通知单");
            return;
        }

        QueryWrapper queryWrapper = new QueryWrapper<RetailReceiveBackBill>();
        queryWrapper.eq("status", 1);
        queryWrapper.in("retail_return_notice_bill_id", returnNoticeIds);
        if (uploadingDate != null) {
            queryWrapper.ge("check_time", uploadingDate);
        }

        List<RetailReceiveBackBill> list = retailReceiveBackBillDao.selectList(queryWrapper);
        if (list != null && list.size() > 0) {
            List<UpdateReturnOrderStatusDto> dtoList = new ArrayList<>();
            for (RetailReceiveBackBill bill : list) {
                // 退货通知单
                RetailReturnNoticeBill noticeBill = returnNoticeBillList.stream().filter(f -> f.getId().equals(bill.getRetailReturnNoticeBillId())).findAny().orElse(null);
                // 全渠道订单
                RetailOrderBill retailOrderBill = orderBillList.stream().filter(f -> f.getId().equals(noticeBill.getRetailOrderBillId())).findAny().orElse(null);

                UpdateReturnOrderStatusDto dto = new UpdateReturnOrderStatusDto();
                dto.setReturn_sn(noticeBill.getOnlineReturnNoticeCode());
                dto.setOrder_sn(retailOrderBill.getOnlineOrderCode());
                dto.setOuter_return_sn(noticeBill.getBillNo());
                dto.setIs_succ(1);
                dto.setRemark("定时推送");
                dto.setRec_time(DateUtil.getFullDateStr(bill.getCheckTime()));

                dtoList.add(dto);
            }

            UpdateReturnOrderStatusReqDto reqDto = new UpdateReturnOrderStatusReqDto();
            reqDto.setApp_key(innoConfig.getAppkey());
            reqDto.setApp_secrept(innoConfig.getAppsecret());
            reqDto.setData(dtoList);

            String api_url = String.format("%s%s", innoConfig.getUrl(), API_URL_UPDATERETURNORDERSTATUS);

            String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
            XxlJobHelper.log(String.format("请求Url：%s", api_url));
            XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
            XxlJobHelper.log(String.format("返回Json：%s", result));

            UpdateReturnOrderStatusRespDto respDto = JSON.parseObject(result, UpdateReturnOrderStatusRespDto.class);
            if (respDto.getCode().equals("-1")) {
                XxlJobHelper.log(String.format("全渠道收退货货单：失败原因：%s", respDto.getMsg()));
            } else {
                XxlJobHelper.log(String.format("全渠道收退货货单：成功：%s", result));
            }

            XxlJobHelper.handleSuccess();
            // 全部推送成功在更新中时间
            Integer winCount = respDto.getData().stream().filter(f -> f.getCode().equals("1")).collect(Collectors.toList()).size();
            if (list.size() == winCount) {
                Date recordTime = list.stream().max(Comparator.comparing(RetailReceiveBackBill::getCheckTime)).get().getCheckTime();
                onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, recordTime);
            }
        } else {
            XxlJobHelper.log("当前无 inno 平台下的收退货单信息");
        }
    }


}
