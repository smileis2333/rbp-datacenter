package com.regent.rbp.api.web.channel;

import com.regent.rbp.api.dto.channel.ChannelQueryParam;
import com.regent.rbp.api.dto.channel.ChannelQueryResult;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.channel.ChannelService;
import com.regent.rbp.api.web.constants.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: rbp-datacenter
 * @description: 渠道资料
 * @author: HaiFeng
 * @create: 2021-09-11 11:31
 */
@RestController
@RequestMapping(ApiConstants.API_CHANNEL)
public class ChannelController {

    @Autowired
    ChannelService channelService;

    @PostMapping("/query")
    public PageDataResponse<ChannelQueryResult> query(@RequestBody ChannelQueryParam param) {
        PageDataResponse<ChannelQueryResult> result = channelService.query(param);
        return result;
    }

    @PostMapping("/save")
    public DataResponse save(@RequestBody ChannelSaveParam param) {
        DataResponse result = channelService.save(param);
        return result;
    }

}
