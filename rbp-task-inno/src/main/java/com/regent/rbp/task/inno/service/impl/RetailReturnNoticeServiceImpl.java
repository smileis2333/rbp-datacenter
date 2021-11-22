package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillOperatorLogDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.RetailReturnNoticeBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDetailDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDto;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.model.req.RetailReturnNoticeReqDto;
import com.regent.rbp.task.inno.model.resp.RetailReturnNoticeRespDto;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知 Impl
 * @author: HaiFeng
 * @create: 2021-09-26 11:16
 */
@Service
public class RetailReturnNoticeServiceImpl implements RetailReturnNoticeService {

    private static final String API_URL_APPRETURNORDER = "api/ReturnOrder/Get_AppReturnOrder";

    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    RetailReturnNoticeBillService retailReturnNoticeBillService;

    @Autowired
    RetailReturnNoticeBillDao retailReturnNoticeBillDao;
    @Autowired
    RetailReturnNoticeBillGoodsDao retailReturnNoticeBillGoodsDao;
    @Autowired
    RetailReturnNoticeBillOperatorLogDao retailReturnNoticeBillOperatorLogDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    RetailOrderBillDao retailOrderBillDao;


    @Override
    public void downloadRetailReturnNoticeList(RetailReturnNoticeParam param)  {
        String key = SystemConstants.GET_APP_RETURN_ORDER;
        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(param.getOnlinePlatformCode());

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatform.getId(), key);
        Boolean isDate = true;
        if (StringUtils.isNotBlank(param.getOrderSn()) || StringUtils.isNotBlank(param.getReturnSn())) {
            isDate = false;
        }
        if(onlinePlatform.getReceiveChannelId() == null) {
            XxlJobHelper.handleFail(String.format("请先在 电商平台档案 配置 %s 的收货渠道", param.getOnlinePlatformCode()));
            return;
        }

        // 销售渠道
        Channel channel = channelDao.selectById(onlinePlatform.getChannelId());
        // 收货渠道
        Channel recChannel = channelDao.selectById(onlinePlatform.getReceiveChannelId());

        RetailReturnNoticeReqDto reqDto = new RetailReturnNoticeReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        int pageIndex = 1;
        DateTime recordTime = null;
        try {
            RetailReturnNoticeDto dto = new RetailReturnNoticeDto();
            dto.setPageIndex(pageIndex);
            if (isDate) {
                SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.FULL_DATE_FORMAT);
                if (uploadingDate != null) {
                    dto.setBeginTime(sdf.format(uploadingDate));
                    dto.setEndTime(sdf.format(new Date()));
                } else {
                    dto.setBeginTime(null);
                    dto.setEndTime(sdf.format(new Date()));
                }

            } else {
                dto.setOrderSn(param.getOrderSn());
                dto.setReturnSn(param.getReturnSn());
            }
            reqDto.setData(dto);
            String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), API_URL_APPRETURNORDER);
            String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

            XxlJobHelper.log(String.format("请求Url：%s", api_url));
            XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
            XxlJobHelper.log(String.format("返回Json：%s", result));

            RetailReturnNoticeRespDto respDto = JSON.parseObject(result, RetailReturnNoticeRespDto.class);
            if (respDto.getCode().equals("-1")) {
                throw new Exception(respDto.getMsg());
            }
            recordTime = new DateTime();
            for (RetailReturnNoticeListDto notice : respDto.getData().getData()) {
                RetailReturnNoticeBill bill = retailReturnNoticeBillDao.selectOne(new QueryWrapper<RetailReturnNoticeBill>().eq("bill_no", notice.getReturn_sn()));
                if (bill == null) {
                    RetailReturnNoticeBillSaveParam saveParam = new RetailReturnNoticeBillSaveParam();
                    saveParam.setBillNo(notice.getReturn_sn());
                    saveParam.setManualId(notice.getReturn_sn());
                    saveParam.setOnlineReturnNoticeCode(notice.getReturn_sn());
                    saveParam.setBillDate(new Date());
                    saveParam.setSaleChannelCode(channel.getCode());
                    saveParam.setReceiveChannelCode(recChannel.getCode());
                    //saveParam.setLogisticsCompanyCode("快递公司");
                    saveParam.setLogisticsBillCode(notice.getShipping_no());

                    // 全渠道订单验证
                    RetailOrderBill orderBill = retailOrderBillDao.selectOne(new QueryWrapper<RetailOrderBill>().eq("online_order_code", notice.getErp_order_sn()));
                    if (orderBill == null) {
                        XxlJobHelper.log(String.format("全渠道退货通知单：%s，失败原因：%s", saveParam.getManualId(), "ERP订单SN(erp_order_sn)订单不存在"));
                    } else {
                        saveParam.setRetailOrdereBillNo(orderBill.getBillNo());
                    }

                    saveParam.setStatus(0);
                    saveParam.setNotes("inno推送");

                    List<RetailReturnNoticeBillGoodsDetailData> goodsDetailList = new ArrayList<>();
                    for (RetailReturnNoticeListDetailDto detail: notice.getListDetail()) {
                        RetailReturnNoticeBillGoodsDetailData data = new RetailReturnNoticeBillGoodsDetailData();
                        data.setBarcode(detail.getSku());
                        data.setQuantity(new BigDecimal(detail.getGoods_number()));
                        data.setTagPrice(detail.getGoods_price());
                        data.setBalancePrice(detail.getReal_price());
                        BigDecimal discount = data.getBalancePrice().divide(data.getTagPrice());
                        data.setDiscount(discount);
                        goodsDetailList.add(data);
                    }
                    saveParam.setGoodsDetailData(goodsDetailList);
                    ModelDataResponse<String> response = retailReturnNoticeBillService.save(saveParam);
                    if (response.getCode() != ResponseCode.OK){
                        XxlJobHelper.log(String.format("全渠道退货通知单：%s，失败原因：%s", saveParam.getManualId(), response.getMessage()));
                    }
                } else {
                    XxlJobHelper.log(String.format("全渠道退货通知单：%s，已经存在", notice.getReturn_sn()));
                }
            }
            XxlJobHelper.handleSuccess();
            // 按单号下载不更新中间表日期
            if (isDate) {
                // 记录接口的最大拉取时间
                onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, recordTime);
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

}
