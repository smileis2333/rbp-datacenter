package com.regent.rbp.api.service.storedvaluecard;

import com.regent.rbp.api.core.storedvaluecard.StoredValueCardChangeRecord;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 14:15
 */
public interface StoredValueCardService {
    /**
     * 获取储值卡金额
     * @param memberCardNo
     * @return
     */
    ModelDataResponse<BigDecimal> get(String memberCardNo);

    /**
     * 查询
     * @param vip
     * @return
     */
    ListDataResponse<StoredValueCardChangeRecord> query(String vip);

    /**
     * 修改储值卡资产
     * @param memberCardNo
     * @param channelId
     * @param employeeId
     * @param billNo
     * @param createTime
     * @param postDate
     * @param payAmount
     * @param creditAmount
     * @param integral
     * @return
     */
    ModelDataResponse<String> updateStoredValueCardAssets(String memberCardNo, String channelId,
                                                          String employeeId, String billNo,
                                                          Date createTime, Date postDate,
                                                          BigDecimal payAmount,
                                                          BigDecimal creditAmount,BigDecimal integral);
}
