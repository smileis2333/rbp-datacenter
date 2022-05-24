package com.regent.rbp.task.yumei.config.yumei;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.task.yumei.config.yumei.api.TokenResource;
import feign.FeignException;
import feign.Request;
import feign.RequestInterceptor;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;

import static java.lang.String.format;

/**
 * @author huangjie
 * @date : 2022/04/29
 * @description
 */
@Configuration
public class YumeiResouceClientConfiguration {
    public final static String LOG_REQUEST_TIME = "X-LOG-TIME";

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private YumeiConfig yumeiConfig;
    @Autowired
    private TokenResource tokenResource;
    @Autowired
    private BaseDbDao baseDbDao;

    @Bean
    public RequestInterceptor requestInterceptor(YumeiTokenManager tokenManager) {
        return requestTemplate -> {
            requestTemplate.header("X-AUTH-TOKEN", tokenManager.getAccessTokenString());
            requestTemplate.header(LOG_REQUEST_TIME, LocalDateTime.now().toString());
        };
    }

    @Bean
    public Decoder feignDecoder() {
        return new YumeiResponseDecoder(mapper, baseDbDao);
    }


    @Bean
    public YumeiTokenManager yumeiTokenManager() {
        YumeiTokenManager yumeiTokenManager = new YumeiTokenManager(yumeiConfig, tokenResource);
        return yumeiTokenManager;
    }

}


class YumeiResponseDecoder implements Decoder {

    private ObjectMapper mapper;
    private BaseDbDao baseDbDao;

    public YumeiResponseDecoder(ObjectMapper mapper, BaseDbDao baseDbDao) {
        this.mapper = mapper;
        this.baseDbDao = baseDbDao;
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

    private void log(Response response, String body) {
        Request request = response.request();
        Request.HttpMethod httpMethod = request.httpMethod();
        String provider = "yumei";
        Request.Body param = request.requestBody();
        String url = request.url();
        String startTime = request.headers().get(YumeiResouceClientConfiguration.LOG_REQUEST_TIME).stream().findFirst().orElse(null);
        String endTime = LocalDateTime.now().toString();
        HashMap<String, Object> item = new HashMap<>();
        item.put("method", httpMethod.name());
        item.put("provider", provider);
        if (httpMethod == Request.HttpMethod.POST) {
            item.put("param", new String(param.asBytes()));
        }
        item.put("response", body);
        item.put("url", url);
        item.put("request_time", startTime);
        item.put("response_time", endTime);
        baseDbDao.insertMap("rbp_third_party_invoke_log", item);
    }
}

