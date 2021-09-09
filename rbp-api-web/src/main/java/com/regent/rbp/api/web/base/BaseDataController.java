package com.regent.rbp.api.web.base;

import com.regent.rbp.api.dto.base.BaseData;
import com.regent.rbp.api.dto.base.BaseQueryParam;
import com.regent.rbp.api.dto.base.BaseSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.BaseService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenchungui
 * @date 2021-09-09
 */
@RestController
@RequestMapping(ApiConstants.API_BASIC_DATA)
@Api(tags = "基础资料")
public class BaseDataController {

    @Autowired
    private BaseService baseService;

    @PostMapping("/query")
    public PageDataResponse<BaseData> query(@RequestBody BaseQueryParam param) {
        PageDataResponse<BaseData> result = baseService.searchPageData(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody BaseSaveParam param) {
        DataResponse result = baseService.batchCreate(param);
        return result;
    }
}
