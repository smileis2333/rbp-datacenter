package com.regent.rbp.api.service.bean.coupon;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.coupon.*;
import com.regent.rbp.api.dao.base.RetailPayTypeDao;
import com.regent.rbp.api.dao.coupon.*;
import com.regent.rbp.api.service.coupon.CouponRuleService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import net.bytebuddy.asm.Advice;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    /**
     * 折扣券
     */
    private static final String DISCOUNT_COUPON = "4";
    /**
     * 抵用券
     */
    private static final String CASH_COUPON = "5";

    @Autowired
    private CashCouponPolicyDao cashCouponPolicyDao;
    @Autowired
    private DiscountCouponPolicyDao discountCouponPolicyDao;

    @Autowired
    private RetailPayTypeDao retailPayTypeDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
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
                oldCoupon.setUpdatedTime(new Date());
                couponRuleDao.update(oldCoupon, new QueryWrapper<CouponRule>().eq("id", oldCoupon.getId()));
            }
            cashCouponPolicyDao.delete(new QueryWrapper<CashCouponPolicy>().eq("code", data.getCode()));
            discountCouponPolicyDao.delete(new QueryWrapper<DiscountCouponPolicy>().eq("code", data.getCode()));
            retailPayTypeDao.delete(new QueryWrapper<RetailPayType>().eq("code", data.getPaymentCode()));
            if (data.getBonusType().equals(CASH_COUPON)) {
                // 自动生成抵用券政策和支付方式
                CashCouponPolicy cashCouponPolicy = new CashCouponPolicy();
                cashCouponPolicy.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                cashCouponPolicy.setCode(data.getCode());
                cashCouponPolicy.setName(data.getName());
                cashCouponPolicy.setRemark("拉取英朗券政策自动生成");
                cashCouponPolicy.setNumberOfCoupon(1000);
                cashCouponPolicy.setPolicyGroup(1000);
                cashCouponPolicy.setCreatedTime(new Date());
                cashCouponPolicyDao.insert(cashCouponPolicy);

                RetailPayType retailPayType = new RetailPayType();
                retailPayType.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                retailPayType.setCode(data.getPaymentCode());
                retailPayType.setName("inno券支付方式");
                retailPayType.setRetailPayPlatform("6");
                retailPayType.setRetailPayPlatformId(1609200376474119L);
                retailPayType.setUseAble(1);
                retailPayType.setVisible(1);
                retailPayType.setNotIncome(1);
                retailPayType.setNotInPoint(1);
                retailPayType.setPos(1);
                retailPayType.setSystemInline(0);
                retailPayType.setDpPrice(0);
                retailPayType.setLimitRatio(100);
                retailPayTypeDao.insert(retailPayType);

            } else if (data.getBonusType().equals(DISCOUNT_COUPON)) {
                // 自动生成折扣券政策
                DiscountCouponPolicy discountCouponPolicy = new DiscountCouponPolicy();
                discountCouponPolicy.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                discountCouponPolicy.setCode(data.getCode());
                discountCouponPolicy.setName(data.getName());
                discountCouponPolicy.setRemark("拉取英朗券政策自动生成");
                discountCouponPolicy.setRestoreTagPrice(0);
                discountCouponPolicy.setUseDiscountMinimum(0);
                discountCouponPolicy.setCreatedTime(new Date());
                discountCouponPolicyDao.insert(discountCouponPolicy);
            }
            couponRuleChannelRangeDao.delete(new QueryWrapper<CouponRuleChannelRange>().eq("coupon_rule_id", couponRuleId));
            couponRuleChannelRangeValueDao.delete(new QueryWrapper<CouponRuleChannelRangeValue>().eq("coupon_rule_id", couponRuleId));
            //couponRuleMemberRangeDao.delete(new QueryWrapper<CouponRuleMemberRange>().eq("coupon_rule_id",couponRuleId));
            //couponRuleMemberRangeValueDao.delete(new QueryWrapper<CouponRuleMemberRangeValue>().eq("coupon_rule_id",couponRuleId));
            couponRuleGoodsRangeDao.delete(new QueryWrapper<CouponRuleGoodsRange>().eq("coupon_rule_id", couponRuleId));
            couponRuleGoodsRangeValueDao.delete(new QueryWrapper<CouponRuleGoodsRangeValue>().eq("coupon_rule_id", couponRuleId));
            //店铺范围
            if (CollectionUtils.isNotEmpty(data.getCouponRuleChannelRangeList())) {
                for (CouponRuleChannelRange couponRuleChannelRange : data.getCouponRuleChannelRangeList()) {
                    couponRuleChannelRange.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    couponRuleChannelRange.setCouponRuleId(couponRuleId);
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
                    couponRuleGoodsRange.setCouponRuleId(couponRuleId);
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
