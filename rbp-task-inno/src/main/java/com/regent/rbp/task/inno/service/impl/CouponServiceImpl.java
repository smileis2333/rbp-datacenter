package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.coupon.*;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.service.coupon.CouponRuleService;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;
import com.regent.rbp.task.inno.model.req.InnoGetAppCouponsListByCreateTimeReq;
import com.regent.rbp.task.inno.model.resp.InnoGetAppCouponsListByCreateTimeResp;
import com.regent.rbp.task.inno.service.CouponService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:37
 */
@Service
public class CouponServiceImpl implements CouponService {
    public static final String GET_APP_COUPONS_LIST_BY_CREATE_TIME = "api/Coupons/Get_App_CouponsListByCreateTime";
    @Autowired
    private InnoConfig innoConfig;
    @Autowired
    private CouponRuleService couponRuleService;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private GoodsDao goodsDao;
    @Override
    public void getAppCouponsListByCreateTime(CouponPolicyDownLoadParam downLoadParam) {
        InnoGetAppCouponsListByCreateTimeReq req = new InnoGetAppCouponsListByCreateTimeReq();
        req.setApp_key(innoConfig.getAppkey());
        req.setApp_secrept(innoConfig.getAppsecret());
        req.setBegintime("2021-10-01 00:00:00");
        req.setEndtime("2022-10-01 00:00:00");
        //req.setPageIndex(1);

        String apiUrl = String.format("%s%s", innoConfig.getUrl(), GET_APP_COUPONS_LIST_BY_CREATE_TIME);
        String result = HttpUtil.post(apiUrl, JSON.toJSONString(req));
        InnoGetAppCouponsListByCreateTimeResp respDto = JSON.parseObject(result, InnoGetAppCouponsListByCreateTimeResp.class);
        if ("-1".equals(respDto.getCode())) {
            new Exception(respDto.getMsg());
        }
        List<InnoGetAppCouponsListByCreateTimeResp.Data> resultList = respDto.getData();
        if (CollectionUtils.isNotEmpty(resultList)) {
            List<CouponRule> couponRuleList = new ArrayList<>();
            CouponRule couponRule = new CouponRule();
            couponRule.setCode("inno001");
            couponRule.setName("英朗折扣券使用组");
            couponRule.setNotes("来源英朗");

            List<CouponRuleChannelRange> couponRuleChannelRangeList = new ArrayList<>();
            List<CouponRuleGoodsRange> couponRuleGoodsRangeList = new ArrayList<>();
            //List<CouponRuleMemberRange> couponRuleMemberRangeList = new ArrayList<>();
            couponRule.setCouponRuleChannelRangeList(couponRuleChannelRangeList);
            couponRule.setCouponRuleGoodsRangeList(couponRuleGoodsRangeList);

            CouponRule couponRule1 = new CouponRule();
            couponRule1.setCode("inno002");
            couponRule1.setName("英朗抵用券使用组");
            couponRule1.setNotes("来源英朗");
            List<CouponRuleChannelRange> couponRuleChannelRangeList1 = new ArrayList<>();
            List<CouponRuleGoodsRange> couponRuleGoodsRangeList1 = new ArrayList<>();
           // List<CouponRuleMemberRange> couponRuleMemberRangeList1 = new ArrayList<>();
            couponRule1.setCouponRuleChannelRangeList(couponRuleChannelRangeList1);
            couponRule1.setCouponRuleGoodsRangeList(couponRuleGoodsRangeList1);

            couponRuleList.add(couponRule);
            couponRuleList.add(couponRule1);

            for (InnoGetAppCouponsListByCreateTimeResp.Data item : resultList) {
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
                    for (String goodsCode : stopGoodsCodeList) {
                        Goods goods = goodsDao.selectOne(new QueryWrapper<Goods>().eq("code", goodsCode));
                        if (goods == null) {
                            continue;
                        }
                        CouponRuleGoodsRangeValue rangeValue1 = new CouponRuleGoodsRangeValue();
                        rangeValue1.setValueId(goods.getId());
                        rangeValue1.setValueCode(goods.getCode());
                        rangeValue1.setValueCode(goods.getName());
                        couponRuleGoodsRangeValueList1.add(rangeValue1);
                    }
                    couponRuleGoodsRange1.setCouponRuleGoodsRangeValueList(couponRuleGoodsRangeValueList1);
                }

                if (item.getBonusType().equals("4")) {
                    //折扣券
                    if (couponRuleChannelRange != null) {
                        couponRuleChannelRangeList.add(couponRuleChannelRange);
                    }
                    if (couponRuleGoodsRange != null) {
                        couponRuleGoodsRangeList.add(couponRuleGoodsRange);
                    }
                    if (couponRuleGoodsRange1 != null) {
                        couponRuleGoodsRangeList.add(couponRuleGoodsRange1);
                    }
                } else if (item.getBonusType().equals("5")) {
                    //代用券
                    if (couponRuleChannelRange != null) {
                        couponRuleChannelRangeList1.add(couponRuleChannelRange);
                    }
                    if (couponRuleGoodsRange != null) {
                        couponRuleGoodsRangeList1.add(couponRuleGoodsRange);
                    }
                    if (couponRuleGoodsRange1 != null) {
                        couponRuleGoodsRangeList1.add(couponRuleGoodsRange1);
                    }
                }
            }
            couponRuleService.saveCouponRule(couponRuleList);
        }
    }
}
