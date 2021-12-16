package com.regent.rbp.api.dao.sendBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.sendBill.SendBill;
import com.regent.rbp.api.core.sendBill.SendBillGoods;
import com.regent.rbp.api.core.sendBill.SendBillSize;
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

    /**
     * 批量插入货品明细
     *
     * @param billGoodsList
     * @return
     */
    Integer batchInsertGoodsList(@Param("billGoodsList") List<SendBillGoods> billGoodsList);

    /**
     * 批量插入尺码明细
     *
     * @param billSizeList
     * @return
     */
    Integer batchInsertSizeList(@Param("billSizeList") List<SendBillSize> billSizeList);

    /**
     * @param tableName
     * @param billSizeList
     * @return
     */
    Integer batchInsertNoticeBillSizeList(@Param("tableName") String tableName, @Param("billSizeList") List<NoticeBillSize> billSizeList);
}
