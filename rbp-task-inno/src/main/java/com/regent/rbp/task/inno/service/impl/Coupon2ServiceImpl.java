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
import com.regent.rbp.api.service.coupon.CouponModelService;
import com.regent.rbp.task.inno.constants.InnoApiUrl;
import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;
import com.regent.rbp.task.inno.model.req.InnoBaseReq;
import com.regent.rbp.task.inno.model.req.InnoGetAppCouponsListByCreateTimeReq;
import com.regent.rbp.task.inno.model.resp.InnoBaseResp;
import com.regent.rbp.task.inno.model.resp.InnoGetAppCouponsListByCreateTimeResp;
import com.regent.rbp.task.inno.service.Coupon2Service;
import com.xxl.job.core.context.XxlJobHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author czd
 * @Date 2022/4/14 10:48
 */
@Service
@Slf4j
public class Coupon2ServiceImpl implements Coupon2Service {
    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    private CouponModelService couponModelService;
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
    private static final String CASH_COUPON = "1";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

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
            List<CouponModel> couponModelList = couponModelContext(respDto);
            couponModelService.saveCouponModel(couponModelList);

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
    private List<CouponModel> couponModelContext(InnoBaseResp<InnoGetAppCouponsListByCreateTimeResp> respDto) {
        List<CouponModel> couponModelList = new ArrayList<>();
        List<InnoGetAppCouponsListByCreateTimeResp.Data> resultList = respDto.getData().getData();
        if (Integer.parseInt(respDto.getData().getTotalPages()) > 0 && CollectionUtils.isNotEmpty(resultList)) {
            Map<String, List<InnoGetAppCouponsListByCreateTimeResp.Data>> groupBy = resultList.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getBonusType));
            for (String bonusType : groupBy.keySet()) {
                if (bonusType.equals(DISCOUNT_COUPON) || bonusType.equals(CASH_COUPON)) {
                    List<CouponModel> list = buildCouponModel(bonusType, groupBy.get(bonusType));
                    if (CollectionUtils.isNotEmpty(list)) {
                        couponModelList.addAll(list);
                    }
                }
            }
        }
        return couponModelList;
    }

    /**
     * 构造券使用组
     * @param bonusType
     * @param list
     */
    private List<CouponModel> buildCouponModel(String bonusType,List<InnoGetAppCouponsListByCreateTimeResp.Data> list) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        List<CouponModel> couponModelList = new ArrayList<>();
        Map<String, List<InnoGetAppCouponsListByCreateTimeResp.Data>>  sonGroupBy = list.stream().collect(Collectors.groupingBy(InnoGetAppCouponsListByCreateTimeResp.Data::getTypeCode));
        if (MapUtils.isEmpty(sonGroupBy)) {
            return null;
        }
        for (String code : sonGroupBy.keySet()) {
            CouponModel couponModel = createCouponModel(bonusType, sonGroupBy.get(code), code);
            List<CouponRuleChannelRange> couponRuleChannelRangeList = new ArrayList<>();
            List<CouponRuleGoodsRange> couponRuleGoodsRangeList = new ArrayList<>();
            for (InnoGetAppCouponsListByCreateTimeResp.Data item : sonGroupBy.get(code)) {
                //使用店铺
                CouponRuleChannelRange couponRuleChannelRange = buildCouponRuleChannelRange(item);
                //允许使用货品
                CouponRuleGoodsRange couponRuleGoodsRange = buildCouponRuleGoodsRange(item.getAllowUseGoodsSn(),0);
                //不允许使用货品
                CouponRuleGoodsRange couponRuleGoodsRangeStop = buildCouponRuleGoodsRange(item.getExcludeGoods(), 1);
                if (couponRuleChannelRange != null) {
                    couponRuleChannelRangeList.add(couponRuleChannelRange);
                }
                if (couponRuleGoodsRange != null) {
                    couponRuleGoodsRangeList.add(couponRuleGoodsRange);
                }
                if (couponRuleGoodsRangeStop != null) {
                    couponRuleGoodsRangeList.add(couponRuleGoodsRangeStop);
                }
            }
            couponModel.setCouponRuleGoodsRangeList(couponRuleGoodsRangeList);
            couponModel.setCouponRuleChannelRangeList(couponRuleChannelRangeList);
            couponModelList.add(couponModel);
        }
        return couponModelList;
    }

    /**
     * 创建CouponRule对象
     * @param bonusType
     * @param sonGroupList
     * @param code
     * @return
     */
    private CouponModel createCouponModel(String bonusType, List<InnoGetAppCouponsListByCreateTimeResp.Data> sonGroupList,
                                          String code) {
        CouponModel couponModel = new CouponModel();
        couponModel.setModelNo(code);
        couponModel.setModelName(sonGroupList.get(0).getTypeName());
        couponModel.setOrigin(2);
        couponModel.setRemark("来源英朗");
        couponModel.setBonusType(bonusType);
        Integer dateType;
        if (StringUtils.isNotEmpty(sonGroupList.get(0).getValidityLimitType())) {
            dateType = Integer.parseInt(sonGroupList.get(0).getValidityLimitType());
        } else {
            dateType = 0;
        }
        couponModel.setDateType(dateType);
        if (dateType == 1) {
            couponModel.setEffectiveDate(getDate(sonGroupList.get(0).getUseStartDate(), YYYY_MM_DD));
            couponModel.setExpirationDate(getDate(sonGroupList.get(0).getUseEndDate(), YYYY_MM_DD));
        } else if (dateType == 2) {
            String validityMonth = sonGroupList.get(0).getValidityMonth();
            couponModel.setEffectiveDate(new Date());
            couponModel.setExpirationDate(getDateMonth(Integer.parseInt(validityMonth)));
        } else if(dateType == 3){
            String validityMonth = sonGroupList.get(0).getValidityMonth();
            couponModel.setEffectiveDate(new Date());
            couponModel.setExpirationDate(addDay(Integer.parseInt(validityMonth)));
        }
        couponModel.setPaymentCode(sonGroupList.get(0).getPyamentCode());
        couponModel.setEmployeeCreate(0);
        couponModel.setNumber(0);
        couponModel.setStatus(1);
        couponModel.setDiscount(sonGroupList.get(0).getDiscount());
        couponModel.setMaxAmount(sonGroupList.get(0).getMaxAmount());
        couponModel.setTypeMoney(sonGroupList.get(0).getTypeMoney());
        couponModel.setMinGoodsAmount(sonGroupList.get(0).getMinGoodsAmount());
        return couponModel;
    }

    public static Date addDay(int n) {
        try {
            Calendar cd = Calendar.getInstance();
            cd.add(Calendar.DATE, n);
            return cd.getTime();

        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateMonth(int month){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return cal.getTime();
    }

    public static Date getDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.length() == 0) {
            return Calendar.getInstance().getTime();
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 构造券使用或者禁用货品
     * @param excludeGoods
     * @param reverseSelect
     * @return
     */
    private CouponRuleGoodsRange buildCouponRuleGoodsRange(String excludeGoods, int reverseSelect) {
        CouponRuleGoodsRange couponRuleGoodsRange = null;
        if (StringUtils.isNotEmpty(excludeGoods)) {
            String[] stopGoodsCodeList = StringUtils.split(excludeGoods, ",");
            couponRuleGoodsRange = new CouponRuleGoodsRange();
            couponRuleGoodsRange.setGoodsCategory("rbp_goods");
            couponRuleGoodsRange.setGoodsAttributeColumn("code");
            couponRuleGoodsRange.setReverseSelect(reverseSelect);
            List<CouponRuleGoodsRangeValue> couponRuleGoodsRangeValueList1 = new ArrayList<>();
            buildCouponRuleGoods(stopGoodsCodeList, couponRuleGoodsRangeValueList1);
            couponRuleGoodsRange.setCouponRuleGoodsRangeValueList(couponRuleGoodsRangeValueList1);
        }
        return couponRuleGoodsRange;
    }

    /**
     * 构造券使用店铺
     * @param item
     * @return
     */
    private CouponRuleChannelRange buildCouponRuleChannelRange(InnoGetAppCouponsListByCreateTimeResp.Data item) {
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
        return couponRuleChannelRange;
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
