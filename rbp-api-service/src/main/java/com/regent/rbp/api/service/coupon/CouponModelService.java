package com.regent.rbp.api.service.coupon;

import com.regent.rbp.api.core.coupon.CouponModel;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2022/4/14 11:14
 */
public interface CouponModelService {

    void saveCouponModel(List<CouponModel> couponModelList);
}
