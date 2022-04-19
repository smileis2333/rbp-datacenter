package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.eum.OnlinePlatformTypeEnum;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.retail.LogisticsCompany;
import com.regent.rbp.api.core.retail.LogisticsCompanyPlatformMapping;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailReceiveBackBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.retail.LogisticsCompanyDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillOperatorLogDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.LogisticsCompanyPlatformMappingService;
import com.regent.rbp.api.service.retail.RetailReturnNoticeBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.dto.RetailOrderSearchDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDetailDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDto;
import com.regent.rbp.task.inno.model.dto.UpdateReturnOrderStatusDto;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.model.req.RetailReturnNoticeReqDto;
import com.regent.rbp.task.inno.model.resp.InnoDataRespDto;
import com.regent.rbp.task.inno.model.resp.InnoLogisticsDto;
import com.regent.rbp.task.inno.model.resp.RetailReturnNoticeRespDto;
import com.regent.rbp.task.inno.model.resp.ReturnOrderShippingNoRespDto;
import com.regent.rbp.task.inno.model.resp.UpdateReturnOrderStatusRespDto;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.regent.rbp.task.yumei.model.YumeiRefund;
import com.regent.rbp.task.yumei.model.YumeiRefundItems;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知 Impl
 * @author: HaiFeng
 * @create: 2021-09-26 11:16
 */
@Service
public class RetailReturnNoticeServiceImpl implements RetailReturnNoticeService {

    private static final String API_URL_APPRETURNORDER = "api/ReturnOrder/Get_AppReturnOrder";
    private static final String API_URL_RETURNORDERSHIPPINGNO = "api/ReturnOrder/Get_ReturnOrderShippingNo";

    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    RetailReturnNoticeBillService retailReturnNoticeBillService;
    @Autowired
    OnlinePlatformSyncErrorService onlinePlatformSyncErrorService;
    @Autowired
    private LogisticsCompanyPlatformMappingService logisticsCompanyPlatformMappingService;
    @Autowired
    private SaleOrderService saleOrderService;

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
    @Autowired
    private LogisticsCompanyDao logisticsCompanyDao;


    @Override
    public void downloadRetailReturnNoticeList(RetailReturnNoticeParam param) throws Exception {
        String key = SystemConstants.GET_APP_RETURN_ORDER;
        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(param.getOnlinePlatformCode());

        if (onlinePlatform.getReceiveChannelId() == null) {
            XxlJobHelper.handleFail(String.format("请先在 电商平台档案 配置 %s 的收货渠道", param.getOnlinePlatformCode()));
            return;
        }
        // 销售渠道
        Channel channel = channelDao.selectById(onlinePlatform.getChannelId());
        // 收货渠道
        Channel recChannel = channelDao.selectById(onlinePlatform.getReceiveChannelId());

        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        if (map.size() > 0) {
            RetailReturnNoticeDto noticeDto = new RetailReturnNoticeDto();
            noticeDto.setReturnSn(StringUtils.join(map.keySet(), ","));
            noticeDto.setPageIndex(1);
            this.pullRetailReturnNotice(onlinePlatform, noticeDto, channel.getCode(), recChannel.getCode(), map);
        }

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatform.getId(), key);
        Boolean isDate = true;
        if (StringUtils.isNotBlank(param.getOrderSn()) || StringUtils.isNotBlank(param.getReturnSn())) {
            isDate = false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.FULL_DATE_FORMAT);

        RetailReturnNoticeDto dto = new RetailReturnNoticeDto();
        dto.setPageIndex(1);
        if (isDate) {
            // 设置结束时间
            dto.setEndTime(DateUtil.getNowDateString(SystemConstants.FULL_DATE_FORMAT));
            if (null == uploadingDate) {
                dto.setBeginTime(sdf.format(DateUtil.getDate("2021-10-01", DateUtil.SHORT_DATE_FORMAT)));
            } else {
                dto.setBeginTime(DateUtil.getDateStr(uploadingDate, SystemConstants.FULL_DATE_FORMAT));
            }
        } else {
            dto.setOrderSn(param.getOrderSn());
            dto.setReturnSn(param.getReturnSn());
        }

        this.pullRetailReturnNotice(onlinePlatform, dto, channel.getCode(), recChannel.getCode(), null);
        XxlJobHelper.handleSuccess();
        // 按单号下载不更新中间表日期
        if (isDate) {
            // 记录接口的最大拉取时间
            Date date = DateUtil.getDate(dto.getEndTime(), SystemConstants.FULL_DATE_FORMAT);
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, date);
        }
    }

    /**
     * 拉取inno退货通知单
     * @param onlinePlatform
     * @param dto
     * @param channelCode
     * @param toChannelCode
     * @param map
     * @throws Exception
     */
    private void pullRetailReturnNotice(OnlinePlatform onlinePlatform, RetailReturnNoticeDto dto, String channelCode, String toChannelCode, Map<String, Long> map) throws Exception {
        RetailReturnNoticeReqDto reqDto = new RetailReturnNoticeReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
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
        if (respDto.getData().getData().size() > 0) {
            for (RetailReturnNoticeListDto notice : respDto.getData().getData()) {
                this.saveRetailReturnNotice(onlinePlatform.getId(), notice, channelCode, toChannelCode, map);
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                dto.setPageIndex(i);
                this.pullRetailReturnNotice(onlinePlatform, dto, channelCode, toChannelCode, map);
            }
        }

    }

    /**
     * 写入退货通知单
     * @param onlinePlatformId
     * @param notice
     * @param channelCode
     * @param toChannelCode
     * @param map
     */
    private void saveRetailReturnNotice(Long onlinePlatformId, RetailReturnNoticeListDto notice, String channelCode, String toChannelCode, Map<String, Long> map) {
        RetailReturnNoticeBill bill = retailReturnNoticeBillDao.selectOne(new QueryWrapper<RetailReturnNoticeBill>().eq("bill_no", notice.getReturn_sn()));
        if (bill == null) {
            RetailReturnNoticeBillSaveParam saveParam = new RetailReturnNoticeBillSaveParam();
            saveParam.setBillNo(notice.getReturn_sn());
            saveParam.setManualId(notice.getReturn_sn());
            saveParam.setOnlineReturnNoticeCode(notice.getReturn_sn());
            saveParam.setBillDate(new Date());
            saveParam.setSaleChannelCode(channelCode);
            saveParam.setReceiveChannelCode(toChannelCode);
            //saveParam.setLogisticsCompanyCode("快递公司");
            saveParam.setLogisticsBillCode(notice.getShipping_no());

            // 全渠道订单验证
            RetailOrderBill orderBill = retailOrderBillDao.selectOne(new QueryWrapper<RetailOrderBill>().eq("bill_no", notice.getErp_order_sn()));
            if (orderBill == null) {
                XxlJobHelper.log(String.format("全渠道退货通知单：%s，失败原因：%s", saveParam.getManualId(), "ERP订单SN(erp_order_sn)订单不存在"));
                return;
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
            if (map != null && map.containsKey(notice.getReturn_sn())) {
                Long errorId = map.get(notice.getReturn_sn());
                if (response.getCode() != ResponseCode.OK) {
                    onlinePlatformSyncErrorService.failure(errorId);
                } else {
                    onlinePlatformSyncErrorService.succeed(errorId);
                }
            } else {
                if (response.getCode() != ResponseCode.OK) {
                    onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, SystemConstants.GET_USER_LIST, notice.getReturn_sn());
                    XxlJobHelper.log(String.format("错误信息：%s %s", notice.getReturn_sn(), response.getMessage()));
                }
            }
        } else {
            XxlJobHelper.log(String.format("全渠道退货通知单：%s，已经存在", notice.getReturn_sn()));
        }
    }

    @Override
    public void returnOrderShippingNo(RetailReturnNoticeParam param) throws Exception {
        String key = SystemConstants.POST_RETURN_ORDER_SHIPPING_NO;
        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(param.getOnlinePlatformCode());
        Long onlinePlatformId = onlinePlatform.getId();
        Date endTime = new Date();
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        if (map.size() > 0) {
            for (String returnSn : map.keySet()) {
                RetailReturnNoticeDto retailReturnNoticeDto = new RetailReturnNoticeDto();
                retailReturnNoticeDto.setReturnSn(returnSn);
                retailReturnNoticeDto.setPageIndex(1);
                this.pushReturnOrderShippingNo(onlinePlatform, retailReturnNoticeDto, map);
            }

        }
        try {
            Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

            RetailReturnNoticeDto searchDto = new RetailReturnNoticeDto();
            searchDto.setBeginTime(DateUtil.getFullDateStr(uploadingDate));
            searchDto.setEndTime(DateUtil.getFullDateStr(endTime));
            searchDto.setPageIndex(1);
            this.pushReturnOrderShippingNo(onlinePlatform, searchDto, map);

            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, endTime);
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void pushReturnOrderShippingNo(OnlinePlatform onlinePlatform, RetailReturnNoticeDto dto, Map<String, Long> map) throws Exception {
        RetailReturnNoticeReqDto reqDto = new RetailReturnNoticeReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(dto);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), API_URL_RETURNORDERSHIPPINGNO);

        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        ReturnOrderShippingNoRespDto respDto = JSON.parseObject(result, ReturnOrderShippingNoRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }

        if (respDto.getData().size() > 0) {
            for (InnoLogisticsDto logistics : respDto.getData()) {
                try {
                    this.updateRetailReturnNoticeBill(onlinePlatform.getId(), logistics, map);
                } catch (Exception ex) {
                    XxlJobHelper.log(String.format("错误信息：%s", ex.getMessage()));
                    ex.printStackTrace();
                    throw ex;
                }
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                dto.setPageIndex(i);
                this.pushReturnOrderShippingNo(onlinePlatform, dto, map);
            }
        }
    }

    private void updateRetailReturnNoticeBill(Long onlinePlatformId, InnoLogisticsDto logistics, Map<String, Long> map) {
        String returnSn = logistics.getReturn_sn();
        if (map != null && map.containsKey(returnSn)) {
            Long errorId = map.get(returnSn);
            if (this.updateLogistics(logistics)) {
                onlinePlatformSyncErrorService.succeed(errorId);
                this.yumeiNoticeIds(returnSn);
            } else {
                onlinePlatformSyncErrorService.failure(errorId);
            }
        } else {
            if (this.updateLogistics(logistics)) {
                this.yumeiNoticeIds(returnSn);
            } else {
                onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB, logistics.getReturn_sn());
            }
        }


    }

    private Boolean updateLogistics(InnoLogisticsDto logistics) {
        Boolean isType = false;
        RetailReturnNoticeBill noticeBill = retailReturnNoticeBillDao.selectOne(new LambdaQueryWrapper<RetailReturnNoticeBill>()
                .eq(RetailReturnNoticeBill::getStatus, 1).eq(RetailReturnNoticeBill::getManualId, logistics.getReturn_sn()));
        if (noticeBill == null) {
            XxlJobHelper.log(String.format("错误信息：%s %s", logistics.getReturn_sn(), "当前通知单不存在或未审核"));
        } else {
            // 物流公司
            LogisticsCompany company = logisticsCompanyPlatformMappingService.getLogisticsCompanyCodeByName(logistics.getShipping_name(), OnlinePlatformTypeEnum.INNO.getId());
            noticeBill.preUpdate();
            noticeBill.setLogisticsCompanyId(company.getId());
            noticeBill.setLogisticsBillCode(logistics.getShipping_no());
            // 修改 Nebule 物流信息
            retailReturnNoticeBillDao.updateById(noticeBill);
            isType = true;
        }
        return isType;
    }

    private void yumeiNoticeIds(String orderSn) {
        Object orderNoList = ThreadLocalGroup.get("yumei_logistics_list");
        Set<String> orderNoList2 = (Set<String>) orderNoList;
        if (null == orderNoList2) {
            orderNoList2 = new HashSet<String>();
        }
        orderNoList2.add(orderSn);
        ThreadLocalGroup.set("yumei_logistics_list", orderNoList2);
    }

    @Override
    public YumeiRefund getYumeiRefund(String orderSn) {
        YumeiRefund refund = new YumeiRefund();
        List<YumeiRefundItems> data = new ArrayList<>();
        refund.setData(data);

        RetailReturnNoticeBill noticeBill = retailReturnNoticeBillDao.selectOne(new LambdaQueryWrapper<RetailReturnNoticeBill>().eq(RetailReturnNoticeBill::getManualId, orderSn));
        RetailOrderBill orderBill = retailOrderBillDao.selectOne(new LambdaQueryWrapper<RetailOrderBill>().eq(RetailOrderBill::getId, noticeBill.getRetailOrderBillId()));
        List<RetailReturnNoticeBillGoods> goodsList = retailReturnNoticeBillGoodsDao.selectList(new LambdaQueryWrapper<RetailReturnNoticeBillGoods>().eq(RetailReturnNoticeBillGoods::getBillId, noticeBill));

        LogisticsCompanyPlatformMapping company = logisticsCompanyPlatformMappingService.getOnlinePlatformLogisticsCodeById(noticeBill.getLogisticsCompanyId(), OnlinePlatformTypeEnum.WDT.getId());
        OrderBusinessPersonDto personDto = saleOrderService.getOrderBusinessPersonDto(noticeBill.getRetailOrderBillId());

        refund.setStoreNo(personDto.getChannelNo());
        refund.setOutOrderNo(orderBill.getManualId());

        for (RetailReturnNoticeBillGoods goods : goodsList) {
            YumeiRefundItems items = new YumeiRefundItems();
            items.setSkuCode(goods.getBarcode());
            items.setLogisticsNo(noticeBill.getLogisticsBillCode());
            items.setLogisticsName(company.getOnlinePlatformLogisticsName());
            data.add(items);
        }
        return refund;
    }



}
