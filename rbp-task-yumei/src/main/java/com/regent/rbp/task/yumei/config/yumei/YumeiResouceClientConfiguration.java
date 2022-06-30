package com.regent.rbp.task.yumei.config.yumei;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.task.yumei.config.yumei.api.TokenResource;
import feign.FeignException;
import feign.RequestInterceptor;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

import static java.lang.String.format;

/**
 * @author huangjie
 * @date : 2022/04/29
 * @description
 */
@Configuration
public class YumeiResouceClientConfiguration {
    public final static String LOG_REQUEST_TIME = "X-LOG-TIME";

    @Autowired(required = false)
    private JavaMailSender mailSender;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private YumeiConfig yumeiConfig;
    @Autowired
    private TokenResource tokenResource;
    @Autowired
    private BaseDbDao baseDbDao;
    @Value("${yumei.notify.email:''}")
    private String notifyMail;
    @Value("${spring.mail.username:''}")
    private String fromMail;

    @Bean
    public RequestInterceptor requestInterceptor(YumeiTokenManager tokenManager) {
        return requestTemplate -> {
            requestTemplate.header("X-AUTH-TOKEN", tokenManager.getAccessTokenString());
            requestTemplate.header(LOG_REQUEST_TIME, LocalDateTime.now().toString());
        };
    }

    @Bean
    public Decoder feignDecoder() {
        YumeiResponseDecoder decoder = new YumeiResponseDecoder(mapper);
        decoder.setBaseDbDao(baseDbDao);
        decoder.setMailSender(mailSender);
        decoder.setFromMail(fromMail);
        decoder.setNotifyMail(notifyMail);
        return decoder;
    }


    @Bean
    public YumeiTokenManager yumeiTokenManager() {
        YumeiTokenManager yumeiTokenManager = new YumeiTokenManager(yumeiConfig, tokenResource);
        return yumeiTokenManager;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        YumeiErrorDecoder errorDecoder = new YumeiErrorDecoder();
        errorDecoder.setBaseDbDao(baseDbDao);
        errorDecoder.setMailSender(mailSender);
        errorDecoder.setFromMail(fromMail);
        errorDecoder.setNotifyMail(notifyMail);
        return errorDecoder;
    }

}


@Slf4j
class YumeiResponseDecoder extends YumeiDecoder implements Decoder {

    private ObjectMapper mapper;

    public YumeiResponseDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object decode(Response response, Type type) throws IOException, FeignException {
        String body = Util.toString(response.body().asReader(Util.UTF_8));
        if (body == null) {
            return null;
        }
        final JavaType javaType = mapper.getTypeFactory().constructType(type);
        final JavaType targetType = mapper.getTypeFactory().constructParametricType(YumeiRes.class, javaType);

        final YumeiRes yumeiRes;
        try {
            yumeiRes = (YumeiRes) mapper.readValue(body, targetType);
            if (yumeiRes.isSuccess()) {
                return yumeiRes.getData();
            } else {
                log(response, body);
            }
        } catch (IOException e) {
            // decode fail, must be error
            log(response, body);
            throw new DecodeException(response.status(),
                    format("%s is not a type supported by this decoder. response body", type, body));
        }

        throw new DecodeException(response.status(),
                format("%s is not a type supported by this decoder.", type));
    }

}

