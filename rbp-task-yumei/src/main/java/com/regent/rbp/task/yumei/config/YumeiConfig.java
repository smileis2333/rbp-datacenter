package com.regent.rbp.task.yumei.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "yumei")
public class YumeiConfig {

    private String url;

    private String account;

    private String password;
}
