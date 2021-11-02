package com.regent.rbp.task.inno.service.impl;

import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.integral.MemberIntegralChangeRecord;
import com.regent.rbp.api.dto.constants.ApiResponseCode;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.integral.IntegralService;
import com.regent.rbp.task.inno.context.integral.IntegralRecordContext;
import com.regent.rbp.task.inno.model.param.IntegralQueryParam;
import com.regent.rbp.task.inno.model.param.VipAddIntegralParam;
import com.regent.rbp.task.inno.service.VipIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 13:44
 */
@Service
public class VipIntegralServiceImpl implements VipIntegralService {
    @Autowired
    private IntegralService integralService;
    @Override
    public Map<String, Object> get(String vip) {
        Map<String, Object> response = new HashMap<>(16);
        ModelDataResponse<BigDecimal> result = integralService.get(vip);
        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            response.put("Flag", "1");
            response.put("Message", "查询成功");
            response.put("data", result.getData());
        }
        return response;
    }

    @Override
    public Map<String, String> vipAddIntegral(VipAddIntegralParam vipAddIntegralParam) {
        Map<String, String> response = new HashMap<>(16);
        MemberIntegral memberIntegral = new MemberIntegral();
        memberIntegral.setIntegral(vipAddIntegralParam.getIntegral());
        memberIntegral.setCardNo(vipAddIntegralParam.getVip());
        memberIntegral.setCreatedTime(vipAddIntegralParam.getVipAddIntegralDate());
        memberIntegral.setNotes(vipAddIntegralParam.getRemark());
        ModelDataResponse result = integralService.updateMemberIntegral(memberIntegral);
        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            response.put("Flag", "1");
            response.put("Message", "修改积分成功");
        }
        return response;
    }

    @Override
    public Map<String, Object> query(IntegralQueryParam param) {
        Map<String, Object> response = new HashMap<>(16);
        PageDataResponse<MemberIntegralChangeRecord> result = integralService.query(param.getVip(), param.getStartDate(), param.getEndDate(),
                param.getSort(), param.getPage(), param.getPageSize());
        if (result.getCode().equals(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR)) {
            response.put("Flag", "-1");
            response.put("Message", result.getMessage());
        } else {
            //转化成inno响应数据
            IntegralRecordContext integralRecordContext = new IntegralRecordContext(result.getData());
            response.put("Flag", "1");
            response.put("pageCount", result.getTotalCount() + "");
            response.put("Message", "查询成功");
            response.put("dataList", integralRecordContext.getResultList());

        }
        return response;
    }
}
