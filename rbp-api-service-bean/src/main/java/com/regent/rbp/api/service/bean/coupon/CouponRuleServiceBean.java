package com.regent.rbp.api.service.bean.coupon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.coupon.*;
import com.regent.rbp.api.dao.coupon.*;
import com.regent.rbp.api.service.coupon.CouponRuleService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 17:04
 */
@Service
public class CouponRuleServiceBean implements CouponRuleService {
    @Autowired
    private CouponRuleDao couponRuleDao;
    @Autowired
    private CouponRuleChannelRangeDao couponRuleChannelRangeDao;
    @Autowired
    private CouponRuleChannelRangeValueDao couponRuleChannelRangeValueDao;
    @Autowired
    private CouponRuleMemberRangeDao couponRuleMemberRangeDao;
    @Autowired
    private CouponRuleMemberRangeValueDao couponRuleMemberRangeValueDao;
    @Autowired
    private CouponRuleGoodsRangeDao couponRuleGoodsRangeDao;
    @Autowired
    private CouponRuleGoodsRangeValueDao couponRuleGoodsRangeValueDao;
    @Override
    public void saveCouponRule(List<CouponRule> couponRuleList) {
        if (CollectionUtils.isEmpty(couponRuleList)) {
            return;
        }
        for (CouponRule data : couponRuleList) {
            CouponRule oldCoupon = couponRuleDao.selectOne(new QueryWrapper<CouponRule>().eq("code", data.getCode()));
            Long couponRuleId;
            if (oldCoupon == null) {
                data.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                couponRuleDao.insert(data);
                couponRuleId = data.getId();
            } else {
                couponRuleId = oldCoupon.getId();
            }
            couponRuleChannelRangeDao.delete(new QueryWrapper<CouponRuleChannelRange>().eq("coupon_rule_id",couponRuleId));
            couponRuleChannelRangeValueDao.delete(new QueryWrapper<CouponRuleChannelRangeValue>().eq("coupon_rule_id",couponRuleId));
            //couponRuleMemberRangeDao.delete(new QueryWrapper<CouponRuleMemberRange>().eq("coupon_rule_id",couponRuleId));
            //couponRuleMemberRangeValueDao.delete(new QueryWrapper<CouponRuleMemberRangeValue>().eq("coupon_rule_id",couponRuleId));
            couponRuleGoodsRangeDao.delete(new QueryWrapper<CouponRuleGoodsRange>().eq("coupon_rule_id",couponRuleId));
            couponRuleGoodsRangeValueDao.delete(new QueryWrapper<CouponRuleGoodsRangeValue>().eq("coupon_rule_id",couponRuleId));
            //店铺范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleChannelRangeList())) {
                for (CouponRuleChannelRange couponRuleChannelRange : data.getCouponRuleChannelRangeList()) {
                    couponRuleChannelRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleChannelRange.setCreatedTime(new Date());
                    couponRuleChannelRangeDao.insert(couponRuleChannelRange);
                    if (CollectionUtils.isNotEmpty(couponRuleChannelRange.getCouponRuleChannelRangeValueList())) {
                        for (CouponRuleChannelRangeValue value : couponRuleChannelRange.getCouponRuleChannelRangeValueList()) {
                            value.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                            value.setCouponRuleId(couponRuleId);
                            value.setCouponRuleChannelRangeId(couponRuleChannelRange.getId());
                            value.setCreatedTime(new Date());
                            couponRuleChannelRangeValueDao.insert(value);
                        }
                    }
                }
            }
            //货品范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleGoodsRangeList())) {
                for (CouponRuleGoodsRange couponRuleGoodsRange : data.getCouponRuleGoodsRangeList()) {
                    couponRuleGoodsRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleGoodsRange.setCreatedTime(new Date());
                    couponRuleGoodsRangeDao.insert(couponRuleGoodsRange);
                    if (CollectionUtils.isNotEmpty(couponRuleGoodsRange.getCouponRuleGoodsRangeValueList())) {
                        for (CouponRuleGoodsRangeValue value : couponRuleGoodsRange.getCouponRuleGoodsRangeValueList()) {
                            value.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                            value.setCouponRuleId(couponRuleId);
                            value.setCouponRuleGoodsRangeId(couponRuleGoodsRange.getId());
                            value.setCreatedTime(new Date());
                            couponRuleGoodsRangeValueDao.insert(value);
                        }
                    }
                }
            }
            //会员范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleMemberRangeList())) {
                for (CouponRuleMemberRange couponRuleMemberRange : data.getCouponRuleMemberRangeList()) {
                    couponRuleMemberRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleMemberRange.setCreatedTime(new Date());
                    couponRuleMemberRangeDao.insert(couponRuleMemberRange);
                    if (CollectionUtils.isNotEmpty(couponRuleMemberRange.getCouponRuleMemberRangeValueList())) {
                        for (CouponRuleMemberRangeValue value : couponRuleMemberRange.getCouponRuleMemberRangeValueList()) {
                            value.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                            value.setCouponRuleId(couponRuleId);
                            value.setCouponRuleMemberRangeId(couponRuleMemberRange.getId());
                            value.setCreatedTime(new Date());
                            couponRuleMemberRangeValueDao.insert(value);
                        }
                    }
                }
            }
        }

    }
}
