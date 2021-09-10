package com.regent.rbp.api.web.base;

import com.regent.rbp.api.dto.base.ColorData;
import com.regent.rbp.api.dto.base.ColorDeleteParam;
import com.regent.rbp.api.dto.base.ColorQueryParam;
import com.regent.rbp.api.dto.base.ColorSaveParam;
import com.regent.rbp.api.dto.base.ColorUpdateParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.ColorService;
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
@RequestMapping(ApiConstants.API_COLOR_DATA)
@Api(tags = "颜色")
public class ColorDataController {

    @Autowired
    private ColorService colorService;

    @PostMapping("/query")
    public PageDataResponse<ColorData> query(@RequestBody ColorQueryParam param) {
        PageDataResponse<ColorData> result = colorService.searchPageData(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody ColorSaveParam param) {
        DataResponse result = colorService.batchCreate(param);
        return result;
    }

    @PutMapping("/update")
    public DataResponse update(@RequestBody ColorUpdateParam param) {
        DataResponse result = colorService.batchUpdate(param);
        return result;
    }

    @DeleteMapping("/delete")
    public DataResponse delete(@RequestBody ColorDeleteParam param) {
        DataResponse result = colorService.batchDelete(param);
        return result;
    }
}
