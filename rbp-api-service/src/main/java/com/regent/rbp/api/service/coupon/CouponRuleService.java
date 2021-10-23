package com.regent.rbp.api.service.coupon;

import com.regent.rbp.api.core.coupon.CouponRule;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 17:04
 */
public interface CouponRuleService {
    /**
     * 保存券使用组
     * @param couponRuleList
     */
    void saveCouponRule(List<CouponRule> couponRuleList);
}
