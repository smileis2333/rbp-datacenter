package com.regent.rbp.app.config.dev;

import com.gitee.starblues.factory.process.pipe.extract.ExtractFactory;
import de.codecentric.boot.admin.client.config.SpringBootAdminClientAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.export.atlas.AtlasMetricsExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.reactive.WebSocketReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketMessagingAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.cloud.bus.BusAutoConfiguration;
import org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.sleuth.annotation.SleuthAnnotationAutoConfiguration;
import org.springframework.cloud.sleuth.zipkin2.ZipkinAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author huangjie
 * @date : 2022/04/01
 * @description
 */
@Profile("dev")
@Import(cn.hutool.extra.spring.SpringUtil.class)
@Configuration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        JvmMetricsAutoConfiguration.class,
        LogbackMetricsAutoConfiguration.class,
        MetricsAutoConfiguration.class,
        RabbitAutoConfiguration.class,
        ActiveMQAutoConfiguration.class,
        AtlasMetricsExportAutoConfiguration.class,
        AtlasMetricsExportAutoConfiguration.class,
        BusAutoConfiguration.class,
        CacheAutoConfiguration.class,
        ElasticsearchAutoConfiguration.class,
        MongoAutoConfiguration.class,
        EurekaClientAutoConfiguration.class,
        FeignAutoConfiguration.class,
        SleuthAnnotationAutoConfiguration.class,
        ZipkinAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SpringBootAdminClientAutoConfiguration.class,
        WebSocketServletAutoConfiguration.class,
        WebSocketMessagingAutoConfiguration.class,
        WebSocketReactiveAutoConfiguration.class,
        ArchaiusAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        SleuthAnnotationAutoConfiguration.class
})
public class DevConfig {
    @Bean
    public ExtractFactory extractFactory(){
        return ExtractFactory.getInstant();
    }
}
