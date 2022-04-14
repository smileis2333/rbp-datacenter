package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;

/**
 * @Description
 * @Author czd
 * @Date 2022/4/14 10:48
 */
public interface Coupon2Service {
    /**
     * 下载
     * @param downLoadParam
     */
    void getAppCouponsListByCreateTime(CouponPolicyDownLoadParam downLoadParam);


}
