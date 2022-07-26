package com.regent.rbp.api.dao.retail;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.dto.retail.OrderBusinessPersonDto;
import com.regent.rbp.api.dto.retail.RetailOrderInfoDto;
import com.regent.rbp.api.dto.retail.RetailOrderSampleDto;
import com.regent.rbp.api.dto.retail.RetalOrderGoodsInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author chenchungui
 * @date 2021-09-14
 */
public interface RetailOrderBillDao extends BaseMapper<RetailOrderBill> {

    Integer batchInsertGoodsList(@Param("billGoodsList") List<RetailOrderBillGoods> billGoodsList);

    void updateDistributionStatus(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 发货后更新订单状态
     *
     * @param id
     * @param userId
     */
    void updateSendStatus(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 获取满足数据中台的订单
     *
     * @param billIdList
     * @return
     */
    List<RetailOrderSampleDto> getDataCenterRetailOrderSampleList(@Param("billIdList") Set<Long> billIdList);

    /**
     * 获取全渠道订单的第一个营业员
     *
     * @param id
     * @return
     */
    OrderBusinessPersonDto getOrderBusinessPersonDto(@Param("id") Long id);

    /**
     * 获取会员发卡渠道
     *
     * @param id
     * @return
     */
    OrderBusinessPersonDto getMemberCardChannel(@Param("id") Long id);

    /**
     * 获取全渠道订单信息
     *
     * @param id
     * @return
     */
    RetailOrderInfoDto getRetailOrderInfoDto(@Param("id") Long id);

    /**
     * 获取全渠道订单货品信息
     *
     * @param id
     * @return
     */
    List<RetalOrderGoodsInfoDto> getRetalOrderGoodsInfoDto(@Param("id") Long id);
}
