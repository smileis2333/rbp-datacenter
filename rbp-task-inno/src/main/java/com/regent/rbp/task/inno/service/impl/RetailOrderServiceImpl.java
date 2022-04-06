package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.model.dto.RetailOrderGoodsDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderItemDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderMainDto;
import com.regent.rbp.task.inno.model.dto.RetailOrderSearchDto;
import com.regent.rbp.task.inno.model.param.RetailOrderDownloadOnlineOrderParam;
import com.regent.rbp.task.inno.model.req.RetailOrderSearchReqDto;
import com.regent.rbp.task.inno.model.resp.RetailOrderSearchRespDto;
import com.regent.rbp.task.inno.service.RetailOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenchungui
 * @date 2021/9/22
 * @description
 */
@Service
public class RetailOrderServiceImpl implements RetailOrderService {

    private static final String POST_GET_APP_ORDER_LIST = "api/Order/Post_Get_App_Order_Info";

    @Autowired
    private RetailOrderBillService retailOrderBillService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;
    @Autowired
    private OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    private OnlinePlatformSyncErrorService onlinePlatformSyncErrorService;

    /**
     * 拉取订单列表
     *
     * @param param
     */
    @Transactional
    @Override
    public void downloadOnlineOrderList(RetailOrderDownloadOnlineOrderParam param, OnlinePlatform onlinePlatform) throws Exception {
        String key = SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB;
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        // 获取销售渠道编号
        String channelCode = baseDbDao.getStringDataBySql(String.format("select code from rbp_channel where id = %s", onlinePlatform.getChannelId()));
        if (map.size() > 0) {
            RetailOrderSearchDto orderSearchDto = new RetailOrderSearchDto();
            orderSearchDto.setOrder_sn_list(StringUtils.join(map.keySet(), ","));
            orderSearchDto.setPageIndex(1);
            this.pullRetailOrderBill(channelCode, param.getOnlinePlatformCode(), onlinePlatform, orderSearchDto, map);
        }

        try {
            RetailOrderSearchDto searchDto = new RetailOrderSearchDto();
            searchDto.setBeginTime(DateUtil.getStartDateTimeStr(param.getBeginTime()));
            searchDto.setEndTime(DateUtil.getEndDateTimeStr(param.getEndTime()));
            searchDto.setOrder_sn_list(param.getOrder_sn_list());
            searchDto.setPageIndex(1);

            this.pullRetailOrderBill(channelCode, param.getOnlinePlatformCode(), onlinePlatform, searchDto, null);

        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        } finally {
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatform.getId(), key, param.getEndTime());
        }

        /*RetailOrderSearchReqDto searchReqDto = new RetailOrderSearchReqDto();
        searchReqDto.setApp_key(onlinePlatform.getAppKey());
        searchReqDto.setApp_secrept(onlinePlatform.getAppSecret());
        int pageIndex = 1;
        // 记录最后一条记录时间
        String lastTimeStr = param.getEndTime();
        try {
            // 获取销售渠道编号
            String channelCode = baseDbDao.getStringDataBySql(String.format("select code from rbp_channel where id = %s", onlinePlatform.getChannelId()));

            RetailOrderSearchDto searchDto = new RetailOrderSearchDto();
            searchReqDto.setData(searchDto);
            searchDto.setBeginTime(param.getBeginTime());
            searchDto.setEndTime(param.getEndTime());
            searchDto.setOrder_sn_list(param.getOrder_sn_list());
            // 循环拉取订单列表
            while (true) {
                searchDto.setPageIndex(pageIndex);

                String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_GET_APP_ORDER_LIST);
                String result = HttpUtil.post(api_url, JSON.toJSONString(searchReqDto));
                // 装换
                RetailOrderSearchRespDto responseDto = JSON.parseObject(result, RetailOrderSearchRespDto.class);
                if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
                    XxlJobHelper.log(responseDto.getMsg());
                    break;
                }
                if (responseDto == null || responseDto.getData() == null) {
                    break;
                }
                RetailOrderSearchPageDto pageDto = responseDto.getData();
                if (pageDto == null || pageDto.getData() == null) {
                    break;
                }
                List<RetailOrderMainDto> orderList = pageDto.getData();
                if (orderList.size() == 0) {
                    break;
                }
                // 循环创建全渠道订单
                for (RetailOrderMainDto mainDto : orderList) {
                    RetailOrderBillSaveParam saveParam = new RetailOrderBillSaveParam();
                    // 参数转换
                    this.convertOrder(mainDto, saveParam);
                    saveParam.setOnlinePlatformCode(param.getOnlinePlatformCode());
                    saveParam.setRetailChannelNo(channelCode);
                    // 插入订单
                    ModelDataResponse<String> response = retailOrderBillService.save(saveParam);
                    // 返回结果
                    if (ResponseCode.OK != response.getCode()) {
                        XxlJobHelper.log(String.format("订单号：%s，失败原因：%s", saveParam.getOnlineOrderCode(), response.getMessage()));
                    }
                }
                // 处理查询分页
                int totalPages = Integer.parseInt(pageDto.getTotalPages());
                if (pageIndex >= totalPages) {
                    lastTimeStr = orderList.get(orderList.size() - 1).getOrderinfo().getLastModifiedTime();
                    break;
                }
                pageIndex++;
            }
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        } finally {
            // 记录接口的最大拉取时间
            OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                    .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatform.getId()).eq(OnlinePlatformSyncCache::getSyncKey, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB));
            if (null != syncCache) {
                syncCache.setData(lastTimeStr);
                onlinePlatformSyncCacheDao.updateById(syncCache);
            }
        }*/
    }

    private void pullRetailOrderBill(String channelCode, String onlinePlatformCode, OnlinePlatform onlinePlatform, RetailOrderSearchDto retailOrderSearch, Map<String, Long> map) throws Exception {
        RetailOrderSearchReqDto reqDto = new RetailOrderSearchReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(retailOrderSearch);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_GET_APP_ORDER_LIST);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        RetailOrderSearchRespDto respDto = JSON.parseObject(result, RetailOrderSearchRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }
        if (respDto.getData().getData().size() > 0) {
            for (RetailOrderMainDto mainDto : respDto.getData().getData()) {
                this.saveRetailOrderBill(onlinePlatform.getId(), onlinePlatformCode, channelCode, mainDto, map);
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                retailOrderSearch.setPageIndex(i);
                this.pullRetailOrderBill(channelCode, onlinePlatformCode, onlinePlatform, retailOrderSearch, map);
            }
        }

    }

    private void saveRetailOrderBill(Long onlinePlatformId, String onlinePlatformCode, String channelCode, RetailOrderMainDto mainDto, Map<String, Long> map) {
        RetailOrderBillSaveParam saveParam = new RetailOrderBillSaveParam();
        // 参数转换
        this.convertOrder(mainDto, saveParam);
        saveParam.setOnlinePlatformCode(onlinePlatformCode);
        saveParam.setRetailChannelNo(channelCode);
        // 插入订单
        ModelDataResponse<String> response = retailOrderBillService.save(saveParam);
        // 返回结果
        String orderSn = mainDto.getOrderinfo().getOrder_sn();
        if (map != null && map.containsKey(orderSn)) {
            Long errorId = map.get(orderSn);
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.failure(errorId);
            } else {
                onlinePlatformSyncErrorService.succeed(errorId);
            }
        } else {
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, SystemConstants.DOWNLOAD_ONLINE_ORDER_LIST_JOB, orderSn);
                XxlJobHelper.log(String.format("错误信息：%s %s", orderSn, response.getMessage()));
            }
        }
    }

    /**
     * 参数转换器
     */
    private void convertOrder(RetailOrderMainDto sourceDto, RetailOrderBillSaveParam targetDto) {
        RetailOrderItemDto order = sourceDto.getOrderinfo();
        List<RetailOrderGoodsDto> goods = sourceDto.getOrdergoods();
        List<RetailOrderBillGoodsDetailData> goodsDetailData = new ArrayList<>();
        targetDto.setGoodsDetailData(goodsDetailData);
        /********************** 订单主体 ******************************/
        targetDto.setBillDate(DateUtil.getDate(order.getAdd_time(), DateUtil.SHORT_DATE_FORMAT));
        targetDto.setManualNo(order.getOrder_sn());
        targetDto.setAcceptGoodsCode(order.getPickup_code());
        targetDto.setOnlineOrderCode(order.getOrder_sn());
        targetDto.setOnlinePlatformTypeId(1);
        // 状态转换
        targetDto.setStatus(StatusEnum.NONE.getStatus());
        if ("0".equals(order.getPay_status())) {
            targetDto.setOnlineStatus(0);
        } else {
            targetDto.setOnlineStatus(3);
        }
        targetDto.setEmployeeName(order.getStaff_code());
        targetDto.setBuyerNotes(order.getUser_remark());
        targetDto.setSellerNotes(StrUtil.EMPTY);
        targetDto.setNotes(order.getAdmin_remark());
        /********************** 物流信息 ******************************/
        targetDto.setMemberCardCode(order.getCard_num());
        targetDto.setBuyerNickname(order.getUser_name());
        targetDto.setBuyerAccount(order.getUser_name());
        targetDto.setBuyerEmail(StrUtil.EMPTY);
        targetDto.setLogisticsCompanyCode(order.getShipping_code());
        targetDto.setNation(SystemConstants.NATION);
        targetDto.setProvince(order.getProvince());
        targetDto.setCity(order.getCity());
        targetDto.setCounty(order.getDistinct());
        targetDto.setAddress(order.getAddress());
        targetDto.setContactsPerson(order.getConsigee());
        targetDto.setMobile(order.getMobile());
        targetDto.setPostCode(order.getZip());
        targetDto.setLogisticsAmount(new BigDecimal(order.getShipping_fee()));
        targetDto.setNote(StrUtil.EMPTY);
        /********************** 货品明细信息 ******************************/
        targetDto.setPayCode(order.getPay_code());
        targetDto.setCardNo(order.getCard_num());
        targetDto.setTransactionNo(order.getAlipay_sn());
        targetDto.setAmount(StringUtils.isNotEmpty(order.getOrder_amount()) ? new BigDecimal(order.getOrder_amount()) : BigDecimal.ZERO);
        /********************** 货品明细信息 ******************************/
        if (CollUtil.isNotEmpty(goods)) {
            for (RetailOrderGoodsDto goodsDto : goods) {
                RetailOrderBillGoodsDetailData item = new RetailOrderBillGoodsDetailData();
                goodsDetailData.add(item);
                item.setBarcode(goodsDto.getProduct_sn());
                item.setSaleType(0);
                item.setRetailGoodsType(0);
                item.setTagPrice(new BigDecimal(goodsDto.getMarket_price()));
                item.setBalancePrice(new BigDecimal(goodsDto.getGoods_real_price()));
                item.setDiscount(item.getBalancePrice().divide(new BigDecimal(goodsDto.getMarket_price()), 4, BigDecimal.ROUND_HALF_UP));
                item.setQuantity(new BigDecimal(goodsDto.getGoods_num()));
                item.setRemark(StrUtil.EMPTY);
            }
        }
    }


}
