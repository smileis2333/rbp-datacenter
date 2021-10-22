package com.regent.rbp.api.service.coupon;

import com.regent.rbp.api.core.coupon.CashCouponPolicy;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 16:17
 */
public interface CashCouponService {
    /**
     * 保存政策
     * @param cashCouponPolicyList
     */
    void saveCouponPolicy(List<CashCouponPolicy> cashCouponPolicyList);
}
