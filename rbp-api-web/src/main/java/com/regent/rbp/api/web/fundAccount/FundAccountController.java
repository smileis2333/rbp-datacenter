package com.regent.rbp.api.web.fundAccount;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryParam;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryResult;
import com.regent.rbp.api.dto.fundAccount.FundAccountSaveParam;
import com.regent.rbp.api.service.fundAccount.FundAccountService;
import com.regent.rbp.api.web.constants.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 资金号
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@RestController
@RequestMapping(ApiConstants.API_FUND_ACCOUNT)
public class FundAccountController {
    @Autowired
    FundAccountService fundAccountService;

    @PostMapping("/query")
    public PageDataResponse<FundAccountQueryResult> query(@RequestBody FundAccountQueryParam param) {
        PageDataResponse<FundAccountQueryResult> result = fundAccountService.query(param);
        return result;
    }

    @PostMapping
    public DataResponse save(@RequestBody FundAccountSaveParam param) {
        DataResponse result = fundAccountService.save(param);
        return result;
    }
}
