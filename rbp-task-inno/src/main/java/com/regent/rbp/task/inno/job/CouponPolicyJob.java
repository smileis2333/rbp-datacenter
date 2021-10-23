package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;
import com.regent.rbp.task.inno.service.CouponService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 拉取优惠券类型列表
 * @Author czd
 * @Date 2021/10/22 14:22
 */
@Slf4j
@Component
public class CouponPolicyJob {
    @Autowired
    private CouponService couponService;

    @XxlJob(SystemConstants.GET_COUPON_POLICY)
    public void downloadCouponPolicy() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            CouponPolicyDownLoadParam downLoadParam = JSON.parseObject(param, CouponPolicyDownLoadParam.class);
            //开始下载
            couponService.getAppCouponsListByCreateTime(downLoadParam);
        } catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }
}
