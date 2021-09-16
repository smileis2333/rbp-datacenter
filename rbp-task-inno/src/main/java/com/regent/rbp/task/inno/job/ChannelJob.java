package com.regent.rbp.task.inno.job;

import cn.hutool.json.JSONUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.ChannelDto;
import com.regent.rbp.task.inno.model.req.ChannelReqDto;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.micrometer.core.instrument.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
@Slf4j
@Component
public class ChannelJob {

    private static final String POST_CHANNEL = "api/BasicData/Get_Category_List";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    private RestTemplate restTemplate;

    @XxlJob("inno.uploadChannelListJobHandler")
    public ReturnT uploadChannelListJobHandler() {

//        ChannelReqDto channelReqDto = new ChannelReqDto();
//        channelReqDto.setApp_key(innoConfig.getAppkey());
//        channelReqDto.setApp_secrept(innoConfig.getAppsecret());
//        List<ChannelDto> channelList = new ArrayList<>();
//        channelReqDto.setData(channelList);
//
//        ChannelDto channelDto = new ChannelDto();
//        channelDto.setId("1674423999851008");
//        channelDto.setName("洛溪店");
//        channelDto.setPhone("");
//        channelDto.setContact("");
//        channelDto.setStore_code("C20210623001");
//        channelDto.setAddr("店铺地址");
//        channelDto.setIsCloseSelfGet("");
//        channelDto.setIsEnabled("");
//        channelDto.setAgent_code("");
//        channelList.add(channelDto);
//        log.info(innoConfig.getUrl());
//        log.info(POST_CHANNEL);
//        String api_url = String.format("%s%s", innoConfig.getUrl(), POST_CHANNEL);
//        ResponseEntity<ChannelRespDto> response = restTemplate.postForEntity(api_url, channelReqDto, ChannelRespDto.class);
//        log.info("上传结束");
//        log.info(JSONUtil.toJsonStr(response));

        return ReturnT.SUCCESS;
    }
}
