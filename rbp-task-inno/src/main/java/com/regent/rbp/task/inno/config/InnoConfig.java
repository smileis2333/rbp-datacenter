package com.regent.rbp.task.inno.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class InnoConfig {

    @Value("${inno.url}")
    private String url;

    @Value("${inno.appkey}")
    private String appkey;

    @Value("${inno.appsecret}")
    private String appsecret;
}
