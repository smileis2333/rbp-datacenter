package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.IntegralQueryParam;
import com.regent.rbp.task.inno.model.param.VipAddIntegralParam;

import java.util.Map;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/20 13:19
 */
public interface VipIntegralService {
    /**
     * 通过卡号获取积分
     * @param vip
     * @return
     */
    Map<String, Object> get(String vip);

    /**
     * 会员积分修改（增加/扣减）
     * @param vipAddIntegralParam
     * @return
     */
    Map<String, String> vipAddIntegral(VipAddIntegralParam vipAddIntegralParam);

    /**
     * 会员积分流水明细（分页）
     * @param param
     * @return
     */
    Map<String, Object> query(IntegralQueryParam param);
}
