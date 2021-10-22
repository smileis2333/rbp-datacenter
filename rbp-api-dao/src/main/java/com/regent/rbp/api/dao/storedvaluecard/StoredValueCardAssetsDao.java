package com.regent.rbp.api.dao.storedvaluecard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardAssets;

import java.math.BigDecimal;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 17:14
 */
public interface StoredValueCardAssetsDao extends BaseMapper<StoredValueCardAssets> {
    /**
     * 修改储值卡资产
     * @param id
     * @param payAmount
     * @param creditAmount
     */
    void updateAssetsById(Long id, BigDecimal payAmount, BigDecimal creditAmount);
}
