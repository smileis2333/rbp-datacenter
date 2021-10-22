package com.regent.rbp.api.service.coupon;

import com.regent.rbp.api.core.coupon.DiscountCouponPolicy;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 16:17
 */
public interface DiscountCouponService {
    /**
     * 保存
     * @param discountCouponPolicyList
     */
    void saveCouponPolicy(List<DiscountCouponPolicy> discountCouponPolicyList);
}
