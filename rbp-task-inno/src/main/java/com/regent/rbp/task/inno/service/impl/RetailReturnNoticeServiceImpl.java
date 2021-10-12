package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
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
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDetailDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDto;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.model.req.RetailReturnNoticeReqDto;
import com.regent.rbp.task.inno.model.resp.MemberRespDto;
import com.regent.rbp.task.inno.model.resp.RetailReturnNoticeRespDto;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RetailReturnNoticeServiceImpl implements RetailReturnNoticeService {

    private static final String API_URL_APPRETURNORDER = "api/ReturnOrder/Get_AppReturnOrder";

    @Autowired
    private InnoConfig innoConfig;

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


    @Override
    public void downloadRetailReturnNoticeList(RetailReturnNoticeParam param)  {
        String key = SystemConstants.GET_APP_RETURN_ORDER;
        Long onlinePlatformId = onlinePlatformService.getOnlinePlatformById(param.getOnlinePlatformCode());

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);
        Boolean isDate = true;
        if (!StringUtils.isNotBlank(param.getOrderSn()) || StringUtils.isNotBlank(param.getReturnSn())) {
            isDate = false;
        }

        RetailReturnNoticeReqDto reqDto = new RetailReturnNoticeReqDto();
        reqDto.setApp_key(innoConfig.getAppkey());
        reqDto.setApp_secrept(innoConfig.getAppsecret());
        int pageIndex = 1;

        try {
            RetailReturnNoticeDto dto = new RetailReturnNoticeDto();
            dto.setPageIndex(pageIndex);
            if (isDate) {
                SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.FULL_DATE_FORMAT);
                if (uploadingDate != null) {
                    dto.setBeginTime(sdf.format(uploadingDate));
                    dto.setEndTime(sdf.format(new Date()));
                }
            } else {
                dto.setOrderSn(param.getOrderSn());
                dto.setReturnSn(param.getReturnSn());
            }
            reqDto.setData(dto);
            String api_url = String.format("%s%s", innoConfig.getUrl(), API_URL_APPRETURNORDER);
            String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

            RetailReturnNoticeRespDto respDto = JSON.parseObject(result, RetailReturnNoticeRespDto.class);
            if (respDto.getCode().equals("-1")) {
                new Exception(respDto.getMsg());
            }
            for (RetailReturnNoticeListDto notice : respDto.getData().getData()) {
                RetailReturnNoticeBill bill = retailReturnNoticeBillDao.selectOne(new QueryWrapper<RetailReturnNoticeBill>().eq("bill_no", notice.getReturn_sn()));
                if (bill == null) {
                    RetailReturnNoticeBillSaveParam saveParam = new RetailReturnNoticeBillSaveParam();
                    saveParam.setManualId(notice.getReturn_sn());
                    saveParam.setBillDate(new Date());
                    saveParam.setSaleChannelCode(notice.getStore_code());
                    saveParam.setReceiveChannelCode("收货渠道");
                    saveParam.setLogisticsCompanyCode("快递公司");
                    saveParam.setLogisticsBillCode(notice.getShipping_no().toString());
                    saveParam.setRetailOrdereBillNo(notice.getErp_order_sn());
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
                    if (response.getCode() !=ResponseCode.OK){
                        XxlJobHelper.log(String.format("全渠道退货通知单：%s，失败原因：%s", saveParam.getManualId(), response.getMessage()));
                    }
                }
            }
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        } finally {

        }


    }
}
