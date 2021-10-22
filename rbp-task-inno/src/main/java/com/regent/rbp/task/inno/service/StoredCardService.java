package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.dto.storedvaluecard.AddVipValueParam;

import java.util.Map;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 10:01
 */
public interface StoredCardService {
    /**
     * 获取储值卡金额
     * @param vip
     * @return
     */
    Map<String, Object> get(String vip);

    /**
     * 储值卡增减
     * @param vipValueParam
     * @return
     */
    Map<String, String> addVipValue(AddVipValueParam vipValueParam);

    /**
     * 会员储值流水账读取
     * @param vip
     * @return
     */
    Map<String, Object> query(String vip);
}
