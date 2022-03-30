package com.regent.rbp.api.dao.sourcing;

import com.regent.rbp.api.dto.sourcing.NewGoodsGuardOptionDto;
import com.regent.rbp.api.dto.sourcing.TaskChannelNewGoodsGuardDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Moruins
 * @date 2022-03-28
 */
public interface TaskChannelNewGoodsGuardDao {

    /**
     * 获取需要计算的寻源规则
     * @return 规则ids
     */
    List<NewGoodsGuardOptionDto> getRuleBill();

    /**
     * 获取对应规则的云仓范围
     * @param billId 规则id
     * @return 云仓范围渠道
     */
    List<Long> getWarehouseRange(@Param("billId") Long billId);

    /**
     * 计算新品保护
     * @param channelIds 计算的渠道
     * @param limit 设置值
     * @return 新品保护
     */
    List<TaskChannelNewGoodsGuardDto> getOldGoodsList(@Param("channelIds") List<Long> channelIds,@Param("limit") Integer limit);
}
