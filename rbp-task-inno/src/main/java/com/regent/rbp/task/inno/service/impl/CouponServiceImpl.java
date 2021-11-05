package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.coupon.*;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.coupon.CouponRuleService;
import com.regent.rbp.task.inno.constants.InnoApiUrl;
import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;
import com.regent.rbp.task.inno.model.req.InnoBaseReq;
import com.regent.rbp.task.inno.model.req.InnoGetAppCouponsListByCreateTimeReq;
import com.regent.rbp.task.inno.model.resp.InnoBaseResp;
import com.regent.rbp.task.inno.model.resp.InnoGetAppCouponsListByCreateTimeResp;
import com.regent.rbp.task.inno.service.CouponService;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:37
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    private CouponRuleService couponRuleService;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private GoodsDao goodsDao;
    /**
     * 错误码
     */
    private static final String ERROR_CODE = "-1";
    /**
     * 折扣券
     */
    private static final String DISCOUNT_COUPON = "4";
    /**
     * 抵用券
     */
    private static final String CASH_COUPON = "5";
    @Override
    public void getAppCouponsListByCreateTime(CouponPolicyDownLoadParam downLoadParam) {
        InnoBaseReq<InnoGetAppCouponsListByCreateTimeReq> req = new InnoBaseReq<>();

        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(downLoadParam.getOnlinePlatformCode());
        req.setApp_key(onlinePlatform.getAppKey());
        req.setApp_secrept(onlinePlatform.getAppSecret());
        InnoGetAppCouponsListByCreateTimeReq data = new InnoGetAppCouponsListByCreateTimeReq();
        data.setBeginTime(downLoadParam.getStartTime());
        data.setEndTime(downLoadParam.getEndTime());
        data.setPageIndex(downLoadParam.getPageIndex());
        data.setIsQueryAll(downLoadParam.getIsQueryAll());
        data.setTypeCodeList(downLoadParam.getTypeCodeList());
        req.setData(data);
        try {
            String apiUrl = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), InnoApiUrl.GET_APP_COUPONS_LIST_BY_CREATE_TIME);
            Log.info("拉取优惠券类型列表请求参数{}", JSON.toJSONString(req));
            String result = HttpUtil.post(apiUrl, JSON.toJSONString(req));
            Log.info("拉取优惠券类型列表响应数据{}", JSON.toJSONString(result));

            Type type = new TypeToken<InnoBaseResp<InnoGetAppCouponsListByCreateTimeResp>>() {
            }.getType();
            InnoBaseResp<InnoGetAppCouponsListByCreateTimeResp> respDto = new Gson().fromJson(result, type);
            if (ERROR_CODE.equals(respDto.getCode())) {
                throw new Exception(respDto.getMsg());
            }
            List<CouponRule> couponRuleList = couponRuleContext(respDto);
            couponRuleService.saveCouponRule(couponRuleList);

            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            Log.error("拉取优惠券类型列表",e);
            XxlJobHelper.handleFail(e.getMessage());
        }

    }

    /**
     * 将inno券政策转化为rbp券使用组的使用店铺、使用货品、使用会员
     * @param respDto
     * @return
     */
    private List<CouponRule> couponRuleContext(InnoBaseResp<InnoGetAppCouponsListByCreateTimeResp> respDto) {
        List<CouponRule> couponRuleList = new ArrayList<>();
        List<InnoGetAppCouponsListByCreateTimeResp.Data> resultList = respDto.getData().getData();
        if (Integer.parseInt(respDto.getData().getTotalPages()) > 0 && CollectionUtils.isNotEmpty(resultList)) {
            Map<String, List<InnoGetAppCouponsListByCreateTimeResp.Data>> groupBy = resultList.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getBonusType));
            for (String bonusType : groupBy.keySet()) {
                couponRuleList =  buildCouponRule(bonusType, groupBy.get(bonusType));
            }
        }
        return couponRuleList;
    }

    /**
     * 构造券使用组
     * @param bonusType
     * @param list
     */
    private List<CouponRule> buildCouponRule(String bonusType,List<InnoGetAppCouponsListByCreateTimeResp.Data> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<CouponRule> couponRuleList = new ArrayList<>();
        Map<String, List<InnoGetAppCouponsListByCreateTimeResp.Data>>  sonGroupBy = list.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getTypeCode));
        if (bonusType.equals(DISCOUNT_COUPON)) {
            sonGroupBy = list.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getTypeCode));
        } else if (bonusType.equals(CASH_COUPON)) {
            sonGroupBy = list.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getTypeCode));
        }
        if (MapUtils.isEmpty(sonGroupBy)) {
            return null;
        }
        for (String code : sonGroupBy.keySet()) {
            CouponRule couponRule = new CouponRule();
            couponRule.setCode(code);
            couponRule.setName(sonGroupBy.get(code).get(0).getTypeName());
            couponRule.setNotes("来源英朗");
            couponRule.setCreatedTime(new Date());

            List<CouponRuleChannelRange> couponRuleChannelRangeList = new ArrayList<>();
            List<CouponRuleGoodsRange> couponRuleGoodsRangeList = new ArrayList<>();
            for (InnoGetAppCouponsListByCreateTimeResp.Data item : sonGroupBy.get(code)) {
                //使用店铺
                CouponRuleChannelRange couponRuleChannelRange = null;
                if (StringUtils.isNotEmpty(item.getUseStoreCode())) {
                    couponRuleChannelRange = new CouponRuleChannelRange();
                    String[] channelCodeList = StringUtils.split(item.getUseStoreCode(), ",");
                    couponRuleChannelRange.setReverseSelect(0);
                    couponRuleChannelRange.setChannelCategory("rbp_channel");
                    couponRuleChannelRange.setChannelAttributeColumn("code");
                    List<CouponRuleChannelRangeValue> couponRuleChannelRangeValueList = new ArrayList<>();
                    for (String channelCode : channelCodeList) {
                        Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", channelCode));
                        if (channel == null) {
                            continue;
                        }
                        CouponRuleChannelRangeValue rangeValue = new CouponRuleChannelRangeValue();
                        rangeValue.setValueId(channel.getId());
                        rangeValue.setValueCode(channel.getCode());
                        rangeValue.setValueCode(channel.getName());
                        couponRuleChannelRangeValueList.add(rangeValue);
                    }
                    couponRuleChannelRange.setCouponRuleChannelRangeValueList(couponRuleChannelRangeValueList);
                }
                //使用货品
                CouponRuleGoodsRange couponRuleGoodsRange = null;
                if (StringUtils.isNotEmpty(item.getAllowUseGoodsSn())) {
                    String[] goodsCodeList = StringUtils.split(item.getAllowUseGoodsSn(), ",");
                    couponRuleGoodsRange = new CouponRuleGoodsRange();
                    couponRuleGoodsRange.setGoodsCategory("rbp_goods");
                    couponRuleGoodsRange.setGoodsAttributeColumn("code");
                    couponRuleGoodsRange.setReverseSelect(0);
                    List<CouponRuleGoodsRangeValue> couponRuleGoodsRangeValueList = new ArrayList<>();
                    buildCouponRuleGoods(goodsCodeList, couponRuleGoodsRangeValueList);
                    couponRuleGoodsRange.setCouponRuleGoodsRangeValueList(couponRuleGoodsRangeValueList);
                }
                //不允许使用货品
                CouponRuleGoodsRange couponRuleGoodsRange1 = null;
                if (StringUtils.isNotEmpty(item.getExcludeGoods())) {
                    String[] stopGoodsCodeList = StringUtils.split(item.getExcludeGoods(), ",");
                    couponRuleGoodsRange1 = new CouponRuleGoodsRange();
                    couponRuleGoodsRange1.setGoodsCategory("rbp_goods");
                    couponRuleGoodsRange1.setGoodsAttributeColumn("code");
                    couponRuleGoodsRange1.setReverseSelect(1);
                    List<CouponRuleGoodsRangeValue> couponRuleGoodsRangeValueList1 = new ArrayList<>();
                    buildCouponRuleGoods(stopGoodsCodeList, couponRuleGoodsRangeValueList1);
                    couponRuleGoodsRange1.setCouponRuleGoodsRangeValueList(couponRuleGoodsRangeValueList1);
                }
                if (couponRuleChannelRange != null) {
                    couponRuleChannelRangeList.add(couponRuleChannelRange);
                }
                if (couponRuleGoodsRange != null) {
                    couponRuleGoodsRangeList.add(couponRuleGoodsRange);
                }
                if (couponRuleGoodsRange1 != null) {
                    couponRuleGoodsRangeList.add(couponRuleGoodsRange1);
                }
            }
            couponRule.setCouponRuleGoodsRangeList(couponRuleGoodsRangeList);
            couponRule.setCouponRuleChannelRangeList(couponRuleChannelRangeList);
            couponRuleList.add(couponRule);
        }
        return couponRuleList;
    }

    /**
     * 构造使用货品范围值
     * @param goodsCodeList
     * @param couponRuleGoodsRangeValueList
     */
    private void buildCouponRuleGoods(String[] goodsCodeList, List<CouponRuleGoodsRangeValue> couponRuleGoodsRangeValueList) {
        for (String goodsCode : goodsCodeList) {
            Goods goods = goodsDao.selectOne(new QueryWrapper<Goods>().eq("code", goodsCode));
            if (goods == null) {
                continue;
            }
            CouponRuleGoodsRangeValue rangeValue = new CouponRuleGoodsRangeValue();
            rangeValue.setValueId(goods.getId());
            rangeValue.setValueCode(goods.getCode());
            rangeValue.setValueCode(goods.getName());
            couponRuleGoodsRangeValueList.add(rangeValue);
        }
    }
}
