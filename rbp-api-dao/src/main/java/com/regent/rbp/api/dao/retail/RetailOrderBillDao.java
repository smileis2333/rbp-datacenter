package com.regent.rbp.api.dao.retail;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-14
 */
public interface RetailOrderBillDao extends BaseMapper<RetailOrderBill> {

    Integer batchInsertGoodsList(@Param("billGoodsList") List<RetailOrderBillGoods> billGoodsList);


}
