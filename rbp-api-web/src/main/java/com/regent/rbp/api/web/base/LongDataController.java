package com.regent.rbp.api.web.base;

import com.regent.rbp.api.dto.base.LongDeleteParam;
import com.regent.rbp.api.dto.base.LongQueryParam;
import com.regent.rbp.api.dto.base.LongSaveParam;
import com.regent.rbp.api.dto.base.LongUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.LongService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenchungui
 * @date 2021-09-10
 */
@RestController
@RequestMapping(ApiConstants.API_LONG_DATA)
@Api(tags = "内长")
public class LongDataController {

    @Autowired
    private LongService longService;

    @PostMapping("/query")
    public PageDataResponse<String> query(@RequestBody LongQueryParam param) {
        PageDataResponse<String> result = longService.searchPageData(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody LongSaveParam param) {
        DataResponse result = longService.batchCreate(param);
        return result;
    }

    @PutMapping("/update")
    public DataResponse update(@RequestBody LongUpdateParam param) {
        DataResponse result = longService.batchUpdate(param);
        return result;
    }

    @DeleteMapping("/delete")
    public DataResponse delete(@RequestBody LongDeleteParam param) {
        DataResponse result = longService.batchDelete(param);
        return result;
    }
}
