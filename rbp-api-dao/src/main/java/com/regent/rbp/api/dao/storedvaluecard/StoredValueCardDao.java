package com.regent.rbp.api.dao.storedvaluecard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCard;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardAssets;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardChangeRecord;

import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 10:13
 */
public interface StoredValueCardDao extends BaseMapper<StoredValueCard> {
    /**
     * 通过会员查找储值卡编码
     * @param memberCardId
     * @return
     */
    Long getByMemberCardId(Long memberCardId);

    /**
     * 查询储值卡金额
     * @param storedValueCardId
     * @return
     */
    StoredValueCardAssets selectAssetsById(Long storedValueCardId);

    /**
     * 查询
     * @param storedValueCardId
     * @return
     */
    List<StoredValueCardChangeRecord> query(Long storedValueCardId);
}
