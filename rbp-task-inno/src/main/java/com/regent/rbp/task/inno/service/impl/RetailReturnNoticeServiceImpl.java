package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillOperatorLogDao;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.RetailReturnNoticeBillService;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeDto;
import com.regent.rbp.task.inno.model.dto.RetailReturnNoticeListDto;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.model.req.RetailReturnNoticeReqDto;
import com.regent.rbp.task.inno.model.resp.MemberRespDto;
import com.regent.rbp.task.inno.model.resp.RetailReturnNoticeRespDto;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

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

                    //retailReturnNoticeBillService.save(param)
                }
            }


        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        } finally {

        }


    }
}
