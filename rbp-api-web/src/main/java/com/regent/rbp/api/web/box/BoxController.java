package com.regent.rbp.api.web.box;

import com.regent.rbp.api.dto.box.BoxQueryParam;
import com.regent.rbp.api.dto.box.BoxQueryResult;
import com.regent.rbp.api.dto.box.BoxSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.box.BoxService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_BOX)
@Api(tags = "箱档案")
public class BoxController {
    @Autowired
    private BoxService boxService;
    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<BoxQueryResult> query(@RequestBody BoxQueryParam param) {
        PageDataResponse<BoxQueryResult> result = boxService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid BoxSaveParam param) {
        DataResponse result = boxService.save(param);
        return result;
    }
}
