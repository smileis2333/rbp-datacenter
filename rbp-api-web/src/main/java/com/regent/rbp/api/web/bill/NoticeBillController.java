package com.regent.rbp.api.web.bill;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.notice.NoticeBillQueryParam;
import com.regent.rbp.api.dto.notice.NoticeBillQueryResult;
import com.regent.rbp.api.dto.notice.NoticeBillSaveParam;
import com.regent.rbp.api.service.notice.NoticeBillService;
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
 * @program: rbp-datacenter
 * @description: 指令单
 * @author: chenchengui
 * @create: 2021-12-08
 */
@RestController
@RequestMapping(ApiConstants.API_NOTICE_BILL)
@Api(tags = "指令单")
public class NoticeBillController {

    @Autowired
    private NoticeBillService noticeBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<NoticeBillQueryResult> query(@RequestBody NoticeBillQueryParam param) {
        PageDataResponse<NoticeBillQueryResult> result = noticeBillService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping
    public DataResponse save(@RequestBody @Valid NoticeBillSaveParam param) {
        DataResponse result = noticeBillService.save(param);
        return result;
    }
}
