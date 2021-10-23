package com.regent.rbp.app.coupon;

import com.regent.rbp.app.RbpAppApplication;
import com.regent.rbp.task.inno.model.param.CouponPolicyDownLoadParam;
import com.regent.rbp.task.inno.service.CouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/23 10:24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RbpAppApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CouponTest {
    @Autowired
    private CouponService couponService;

    /**
     * 测试券政策拉取
     */
    @Test
    public void test01(){
        CouponPolicyDownLoadParam downLoadParam = new CouponPolicyDownLoadParam();
        downLoadParam.setOnlinePlatformCode("inno");
        downLoadParam.setStartTime("2020-10-01 00:00:00");
        downLoadParam.setEndTime("2022-10-01 00:00:00");
        downLoadParam.setPageIndex(1);
        couponService.getAppCouponsListByCreateTime(downLoadParam);
    }
}
