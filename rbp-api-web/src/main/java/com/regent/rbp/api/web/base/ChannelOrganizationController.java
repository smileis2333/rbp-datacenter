package com.regent.rbp.api.web.base;

import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.ChannelOrganizationService;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组织架构设置
 * @author hj
 * @date 2021-12-16
 */
@Api(tags = "组织架构")
@RestController
@RequestMapping(ApiConstants.CHANNEL_ORGANIZATION)
public class ChannelOrganizationController {

    @Autowired
    private ChannelOrganizationService channelOrganizationService;

    @PostMapping
    @ApiOperation(value = "组织架构新增")
    public DataResponse create(@RequestBody ChannelOrganizationSaveParam param) {
        return channelOrganizationService.create(param);
    }

    @PostMapping("/query")
    public PageDataResponse query() {
        PageDataResponse result = channelOrganizationService.query();
        return result;
    }

}
