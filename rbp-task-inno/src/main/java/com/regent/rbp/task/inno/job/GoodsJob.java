package com.regent.rbp.task.inno.job;

import com.regent.rbp.task.inno.config.InnoConfig;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author xuxing
 */
@Slf4j
@Component
public class GoodsJob {

    private static final String POST_CHANNEL = "api/BasicData/Get_Category_List";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    private RestTemplate restTemplate;

    @XxlJob("inno.downloadOnlineGoodsListJobHandler")
    public ReturnT uploadChannelListJobHandler() {
        return null;
    }
}
