package com.regent.rbp.task.yumei.config.yumei;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangjie
 * @date : 2022/05/04
 * @description
 */
@Data
@Configuration
public class YumeiConfig {
    @Value("${yumei.url:}")
    private String url;

    @Value("${yumei.account:}")
    private String account;

    @Value("${yumei.password:}")
    private String password;
}
