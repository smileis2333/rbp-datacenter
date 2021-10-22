package com.regent.rbp.api.service.integral;

import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.integral.MemberIntegralChangeRecord;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 14:04
 */
public interface IntegralService {
    /**
     * 通过卡号获取积分
     * @param code
     * @return
     */
    ModelDataResponse<BigDecimal> get(String code);

    /**
     * 会员积分修改（增加/扣减）
     * @param memberIntegral
     * @return
     */
    ModelDataResponse updateMemberIntegral(MemberIntegral memberIntegral);

    /**
     * 会员积分流水明细（分页）
     * @param memberCardNo
     * @param beginDate
     * @param endDate
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PageDataResponse<MemberIntegralChangeRecord> query(String memberCardNo, Date beginDate, Date endDate, String sort, int page, int pageSize);
}
