package com.regent.rbp.app;

import com.regent.rbp.app.adapter.CustomWebMvcConfigurer;
import com.regent.rbp.app.config.MyBatisPlusConfig;
import com.regent.rbp.infrastructure.interceptor.JwtAuthInterceptor;
import com.regent.rbp.infrastructure.response.config.RegentPlatformExceptionResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@ServletComponentScan
@EnableDiscoveryClient
@ComponentScan(value = "com.regent.rbp.**.**", excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CustomWebMvcConfigurer.class, RegentPlatformExceptionResolver.class, JwtAuthInterceptor.class, MyBatisPlusConfig.class})})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@EnableFeignClients(value = "com.regent.rbp.**.**")
public class RbpAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbpAppApplication.class, args);
    }

}
