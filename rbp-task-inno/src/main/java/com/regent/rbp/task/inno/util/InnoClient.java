package com.regent.rbp.task.inno.util;

import com.regent.rbp.task.inno.config.InnoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author xuxing
 */
@Component
public class InnoClient {
    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    private RestTemplate restTemplate;


}
