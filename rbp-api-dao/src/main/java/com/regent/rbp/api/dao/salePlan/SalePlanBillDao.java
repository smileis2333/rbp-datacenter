package com.regent.rbp.api.dao.salePlan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoodsFinal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 计划单 Dao
 * @author: HaiFeng
 * @create: 2021-11-15 15:38
 */
public interface SalePlanBillDao extends BaseMapper<SalePlanBill> {

    Date queryMinDate();

    List<SalePlanBillGoodsFinal> querySendPlanBillGoods(@Param("salePlanId") Long salePlanId);
}
