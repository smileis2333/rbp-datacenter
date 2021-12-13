package com.regent.rbp.api.web.user;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.user.UserQueryParam;
import com.regent.rbp.api.dto.user.UserQueryResult;
import com.regent.rbp.api.dto.user.UserSaveParam;
import com.regent.rbp.api.service.user.UserService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: rbp-datacenter
 * @description: 用户档案
 * @author: chenchengui
 * @create: 2021-12-08
 */
@RestController
@RequestMapping(ApiConstants.API_USER)
@Api(tags = "用户档案")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<UserQueryResult> query(@RequestBody UserQueryParam param) {
        PageDataResponse<UserQueryResult> result = userService.query(param);
        return result;
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public DataResponse save(@RequestBody UserSaveParam param) {
        DataResponse result = userService.save(param);
        return result;
    }
}
