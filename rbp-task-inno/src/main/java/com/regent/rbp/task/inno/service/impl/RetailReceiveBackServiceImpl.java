package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailReceiveBackBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.model.dto.UpdateReturnOrderStatusDto;
import com.regent.rbp.task.inno.model.dto.UpdateReturnOrderStatusReqDto;
import com.regent.rbp.task.inno.model.param.UpdateRetailReceiveBackByStatusParam;
import com.regent.rbp.task.inno.model.resp.InnoDataRespDto;
import com.regent.rbp.task.inno.model.resp.UpdateReturnOrderStatusRespDto;
import com.regent.rbp.task.inno.service.RetailReceiveBackService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
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
    OnlinePlatformService onlinePlatformService;
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    OnlinePlatformSyncErrorService onlinePlatformSyncErrorService;

    @Autowired
    RetailReceiveBackBillDao retailReceiveBackBillDao;
    @Autowired
    RetailReturnNoticeBillDao retailReturnNoticeBillDao;
    @Autowired
    RetailOrderBillDao retailOrderBillDao;

    @Override
    public void UpdateReturnOrderStatus(UpdateRetailReceiveBackByStatusParam param) throws Exception {
        String key = SystemConstants.POST_RETURN_ORDER_STATUS;
        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(param.getOnlinePlatformCode());
        Long onlinePlatformId = onlinePlatform.getId();
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        if (map.size() > 0) {
            // 全渠道收退货单
            List<RetailReceiveBackBill> retailReceiveBackBillList = retailReceiveBackBillDao.selectList(new LambdaQueryWrapper<RetailReceiveBackBill>().in(RetailReceiveBackBill::getId, map.keySet()));
            List<UpdateReturnOrderStatusDto> orderStatusList = this.packaging(onlinePlatformId, retailReceiveBackBillList);
            if (orderStatusList.size() > 0)
            {
                List<InnoDataRespDto> dataRespDtoList = this.pushRetailReceiveBackBillByStatus(onlinePlatform, orderStatusList);
                for (InnoDataRespDto innoDataResp : dataRespDtoList) {
                    UpdateReturnOrderStatusDto statusDto = orderStatusList.stream().filter(f -> f.getReturn_sn().equals(innoDataResp.getRowUniqueKey())).findAny().orElse(null);
                    Long retailReceiveBackBillId = retailReceiveBackBillList.stream().filter(f -> f.getBillNo().equals(statusDto.getOuter_return_sn())).findAny().orElse(null).getId();
                    Long errorId = map.get(retailReceiveBackBillId);
                    if (!innoDataResp.getCode().equals(1)) {
                        onlinePlatformSyncErrorService.failure(errorId);
                    } else {
                        onlinePlatformSyncErrorService.succeed(errorId);
                    }
                }
            }
        }

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);
        // 获得开始时间
        if (null == uploadingDate) {
            uploadingDate = DateUtil.getDate("2021-10-01", DateUtil.SHORT_DATE_FORMAT);
        }
        QueryWrapper queryWrapper = new QueryWrapper<RetailReceiveBackBill>();
        queryWrapper.eq("status", 1);
        if (uploadingDate != null) {
            queryWrapper.ge("check_time", uploadingDate);
        }

        List<RetailReceiveBackBill> list = retailReceiveBackBillDao.selectList(queryWrapper);
        List<UpdateReturnOrderStatusDto> updateReturnOrderStatusDtoList = this.packaging(onlinePlatformId, list);
        if (updateReturnOrderStatusDtoList.size() > 0) {
            List<InnoDataRespDto> innoDataRespDtoList = this.pushRetailReceiveBackBillByStatus(onlinePlatform, updateReturnOrderStatusDtoList);
            for (InnoDataRespDto innoDataRespDto : innoDataRespDtoList) {
                UpdateReturnOrderStatusDto statusDto = updateReturnOrderStatusDtoList.stream().filter(f -> f.getReturn_sn().equals(innoDataRespDto.getRowUniqueKey())).findAny().orElse(null);
                Long retailReceiveBackBillId = list.stream().filter(f -> f.getBillNo().equals(statusDto.getOuter_return_sn())).findAny().orElse(null).getId();
                Long errorId = map.get(retailReceiveBackBillId);
                if (!innoDataRespDto.getCode().equals(1)) {
                    onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, key, retailReceiveBackBillId.toString());
                    XxlJobHelper.log(String.format("错误信息：%s %s", innoDataRespDto.getRowUniqueKey(), innoDataRespDto.getMsg()));
                }
            }
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingDate);
            XxlJobHelper.handleSuccess();
        } else {
            XxlJobHelper.log("当前无 inno 平台下的收退货单信息");
        }
    }

    /**
     * 打包
     * @param retailReceiveBackBillList
     * @return
     */
    private List<UpdateReturnOrderStatusDto> packaging(Long onlinePlatformId ,List<RetailReceiveBackBill> retailReceiveBackBillList) {
        List<UpdateReturnOrderStatusDto> dtoList = new ArrayList<>();
        // 全渠道收退货单Id
        List<Long> retailReturnNoticeBillIds = retailReceiveBackBillList.stream().map(RetailReceiveBackBill::getRetailReturnNoticeBillId).distinct().collect(Collectors.toList());
        // 全渠道退货通知
        List<RetailReturnNoticeBill> retailReturnNoticeBillList = retailReturnNoticeBillDao.selectList(new LambdaQueryWrapper<RetailReturnNoticeBill>().in(RetailReturnNoticeBill::getId, retailReturnNoticeBillIds));
        List<Long> retailOrderBillIds = retailReturnNoticeBillList.stream().map(RetailReturnNoticeBill::getRetailOrderBillId).distinct().collect(Collectors.toList());
        // 全渠道订单
        List<RetailOrderBill> retailOrderBillList = retailOrderBillDao.selectList(new LambdaQueryWrapper<RetailOrderBill>().in(RetailOrderBill::getId, retailOrderBillIds));

        for (RetailReceiveBackBill bill : retailReceiveBackBillList) {
            // 退货通知单
            RetailReturnNoticeBill noticeBill = retailReturnNoticeBillList.stream().filter(f -> f.getId().equals(bill.getRetailReturnNoticeBillId())).findAny().orElse(null);
            if (noticeBill == null)
                continue;
            // 全渠道订单
            RetailOrderBill retailOrderBill = retailOrderBillList.stream().filter(f -> f.getId().equals(noticeBill.getRetailOrderBillId())).findAny().orElse(null);
            if (retailOrderBill == null)
                continue;
            // 排除非inno的订单
            if (!retailOrderBill.getOnlinePlatformId().equals(onlinePlatformId))
                continue;

            UpdateReturnOrderStatusDto dto = new UpdateReturnOrderStatusDto();
            dto.setReturn_sn(noticeBill.getOnlineReturnNoticeCode());
            dto.setOrder_sn(retailOrderBill.getOnlineOrderCode());
            dto.setOuter_return_sn(bill.getBillNo());
            dto.setIs_succ(1);
            dto.setRemark("定时推送");
            dto.setRec_time(DateUtil.getFullDateStr(bill.getCheckTime()));

            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 推送
     * @param onlinePlatform
     * @param dtoList
     * @return
     * @throws Exception
     */
    private List<InnoDataRespDto> pushRetailReceiveBackBillByStatus(OnlinePlatform onlinePlatform, List<UpdateReturnOrderStatusDto> dtoList) throws Exception {
        UpdateReturnOrderStatusReqDto reqDto = new UpdateReturnOrderStatusReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(dtoList);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), API_URL_UPDATERETURNORDERSTATUS);

        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        UpdateReturnOrderStatusRespDto respDto = JSON.parseObject(result, UpdateReturnOrderStatusRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }
        return respDto.getData();
    }

}
