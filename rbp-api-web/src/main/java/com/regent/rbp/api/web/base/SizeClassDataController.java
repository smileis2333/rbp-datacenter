package com.regent.rbp.api.web.base;

import com.regent.rbp.api.dto.base.SizeClassData;
import com.regent.rbp.api.dto.base.SizeClassDeleteParam;
import com.regent.rbp.api.dto.base.SizeClassQueryParam;
import com.regent.rbp.api.dto.base.SizeClassSaveParam;
import com.regent.rbp.api.dto.base.SizeClassUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.SizeClassService;
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
@RequestMapping(ApiConstants.API_SIZE_CLASS_DATA)
@Api(tags = "尺码")
public class SizeClassDataController {

    @Autowired
    private SizeClassService sizeclassService;

    @PostMapping("/query")
    public PageDataResponse<SizeClassData> query(@RequestBody SizeClassQueryParam param) {
        PageDataResponse<SizeClassData> result = sizeclassService.searchPageData(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody SizeClassSaveParam param) {
        DataResponse result = sizeclassService.batchCreate(param);
        return result;
    }

    @PutMapping("/update")
    public DataResponse update(@RequestBody SizeClassUpdateParam param) {
        DataResponse result = sizeclassService.batchUpdate(param);
        return result;
    }

    @DeleteMapping("/delete")
    public DataResponse delete(@RequestBody SizeClassDeleteParam param) {
        DataResponse result = sizeclassService.batchDelete(param);
        return result;
    }
}
