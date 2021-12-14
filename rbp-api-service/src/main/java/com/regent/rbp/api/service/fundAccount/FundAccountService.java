package com.regent.rbp.api.service.fundAccount;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryParam;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryResult;
import com.regent.rbp.api.dto.fundAccount.FundAccountSaveParam;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
public interface FundAccountService {
    /**
     * 查询
     * @param param
     * @return
     */
    PageDataResponse<FundAccountQueryResult> query(FundAccountQueryParam param);

    /**
     * 新增/修改
     * @param param
     * @return
     */
    DataResponse save(FundAccountSaveParam param);

}
