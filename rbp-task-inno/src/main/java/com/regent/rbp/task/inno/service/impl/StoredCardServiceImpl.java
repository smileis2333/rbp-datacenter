package com.regent.rbp.task.inno.service.impl;

import com.regent.rbp.api.core.storedvaluecard.StoredValueCardChangeRecord;
import com.regent.rbp.api.dto.constants.ApiResponseCode;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.storedvaluecard.AddAmount;
import com.regent.rbp.api.dto.storedvaluecard.AddVipValueParam;
import com.regent.rbp.api.service.storedvaluecard.StoredValueCardService;
import com.regent.rbp.task.inno.context.storedcard.StoredValueCardChangeRecordContext;
import com.regent.rbp.task.inno.service.StoredCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 13:47
 */
@Service
public class StoredCardServiceImpl implements StoredCardService {
    @Autowired
    private StoredValueCardService storedValueCardService;
    @Override
    public Map<String, Object> get(String vip) {
        Map<String, Object> response = new HashMap<>(16);
        ModelDataResponse<BigDecimal> result = storedValueCardService.get(vip);
        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            response.put("Flag", "0");
            response.put("Message", "成功");
            response.put("data", result.getData());
            return response;
        }
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> addVipValue(AddVipValueParam vipValueParam) {
        Map<String, String> response = new HashMap<>(16);
        String memberCardNo = vipValueParam.getVip();
        String channelId = vipValueParam.getCheckOrderID();
        String employeeId = vipValueParam.getBuisnessManID();
        String billNo = vipValueParam.getCheckOrderID();
        Date createTime = vipValueParam.getVipAddValue_Date();
        Date postDate = vipValueParam.getPostedDate();
        //实付金额
        BigDecimal integral = new BigDecimal(0);
        //实付金额
        BigDecimal payAmount = new BigDecimal(0);
        //赠送金额
        BigDecimal creditAmount = new BigDecimal(0);
        for (AddAmount addAmount : vipValueParam.getAmountList()) {
            payAmount = payAmount.add(addAmount.getFactAmount().subtract(addAmount.getAmount()));
            creditAmount = creditAmount.add(addAmount.getAmount());
            integral = integral.add(addAmount.getIntegral());
        }
        ModelDataResponse<String> result = storedValueCardService.updateStoredValueCardAssets(memberCardNo,
                channelId,employeeId,billNo,createTime,postDate,payAmount,creditAmount,integral);

        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            response.put("Flag", "1");
            response.put("Message", "添加储值成功");
            return response;
        }
        return response;
    }

    @Override
    public Map<String, Object> query(String vip) {
        Map<String, Object> response = new HashMap<>(16);
        ListDataResponse<StoredValueCardChangeRecord> result = storedValueCardService.query(vip);
        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            StoredValueCardChangeRecordContext context = new StoredValueCardChangeRecordContext(result.getData());
            response.put("Flag", "1");
            response.put("Message", "查询成功");
            response.put("data", context.getResultList());
            return response;
        }
        return response;
    }
}
