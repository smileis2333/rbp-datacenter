package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/22 15:34
 */
public interface CouponService {
    /**
     * 下载
     * @param downLoadParam
     */
    void getAppCouponsListByCreateTime(CouponPolicyDownLoadParam downLoadParam);
}
