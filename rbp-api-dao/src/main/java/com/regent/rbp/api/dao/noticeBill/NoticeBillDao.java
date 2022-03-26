package com.regent.rbp.api.dao.noticeBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.noticeBill.NoticeBill;
import com.regent.rbp.api.core.noticeBill.NoticeBillGoods;
import com.regent.rbp.api.core.noticeBill.NoticeBillSize;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoodsFinal;
import com.regent.rbp.api.core.salePlan.SalePlanBillSizeFinal;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 指令单 Dap
 * @author: HaiFeng
 * @create: 2021-11-16 15:56
 */
public interface NoticeBillDao extends BaseMapper<NoticeBill> {

    /**
     * 批量插入货品明细
     *
     * @param billGoodsList
     * @return
     */
    Integer batchInsertGoodsList(@Param("billGoodsList") List<NoticeBillGoods> billGoodsList);

    /**
     * 批量插入尺码明细
     *
     * @param billSizeList
     * @return
     */
    Integer batchInsertSizeList(@Param("billSizeList") List<NoticeBillSize> billSizeList);

    /**
     * @param tableName
     * @param billSizeList
     * @return
     */
    Integer batchInsertSalePlanBillSizeList(@Param("tableName") String tableName, @Param("billSizeList") List<SalePlanBillSizeFinal> billSizeList);

    Date queryMinDate();

    List<NoticeBillGoods> queryNoticeBillGoods(@Param("noticeId") Long noticeId);

}
