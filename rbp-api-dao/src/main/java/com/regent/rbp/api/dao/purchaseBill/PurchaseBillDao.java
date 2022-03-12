package com.regent.rbp.api.dao.purchaseBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.purchaseBill.PurchaseBill;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoods;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillGoodsFinal;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSize;
import com.regent.rbp.api.core.purchaseBill.PurchaseBillSizeFinal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 采购单 Dao
 * @author: chenchungui
 * @create: 2021-12-21
 */
public interface PurchaseBillDao extends BaseMapper<PurchaseBill> {

    /**
     * 批量插入货品明细
     *
     * @param billGoodsList
     * @return
     */
    Integer batchInsertGoodsList(@Param("billGoodsList") List<PurchaseBillGoods> billGoodsList);

    /**
     * 批量插入调整后货品明细
     *
     * @param billGoodsList
     * @return
     */
    Integer batchInsertGoodsFinalList(@Param("billGoodsList") List<PurchaseBillGoodsFinal> billGoodsList);

    /**
     * 批量插入尺码明细
     *
     * @param billSizeList
     * @return
     */
    Integer batchInsertSizeList(@Param("billSizeList") List<PurchaseBillSize> billSizeList);

    /**
     * 批量插入调整后尺码明细
     *
     * @param billSizeList
     * @return
     */
    Integer batchInsertSizeFinalList(@Param("billSizeList") List<PurchaseBillSizeFinal> billSizeList);

    Date queryMinDate(@Param("date") Date date);

}
