package com.regent.rbp.api.dao.purchaseBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoodsFinal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购调整后货品明细 Dao
 * @author: HaiFeng
 * @create: 2021-11-17 11:08
 */
public interface PurchaseBillGoodsFinalDao extends BaseMapper<PurchaseBillGoodsFinal> {

    List<PurchaseBillGoodsFinal> queryPurchaseBillGoodsFinal(@Param("billIds") List<Long> billIds);
}
