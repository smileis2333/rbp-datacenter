package com.regent.rbp.api.web.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author xuxing
 */
@Data
@Configuration
public class DataCenterConfig {

    @Value("${datacenter.appkey}")
    private String appkey;

    @Value("${datacenter.appsecret}")
    private String appsecret;
}
