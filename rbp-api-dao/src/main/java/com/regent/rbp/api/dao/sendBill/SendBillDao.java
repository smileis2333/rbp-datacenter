package com.regent.rbp.api.dao.sendBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 发货单 Dao
 * @author: HaiFeng
 * @create: 2021-11-16 17:50
 */
public interface SendBillDao extends BaseMapper<SendBill> {

    List<SendBillGoods> querySendBillGoods(@Param("salePlanId") Long salePlanId, @Param("noticeId") Long noticeId);
}
