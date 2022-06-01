package com.regent.rbp.task.yumei.config.yumei;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.dao.base.BaseDbDao;
import feign.Request;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

import static java.lang.String.format;

@AllArgsConstructor
public class YumeiErrorDecoder implements ErrorDecoder {
    private ObjectMapper mapper;
    private BaseDbDao baseDbDao;

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String body = Util.toString(response.body().asReader(Util.UTF_8));
            HttpStatus responseStatus = HttpStatus.valueOf(response.status());
            if (responseStatus.is5xxServerError()) {
                log(response, body);
            } else if (responseStatus.is4xxClientError()) {
                log(response, body);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new DecodeException(response.status(),
                format("third party request(yumei) invoke fail"));
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
