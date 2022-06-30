package com.regent.rbp.task.yumei.config.yumei;

import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static java.lang.String.format;

@AllArgsConstructor
public class YumeiErrorDecoder extends YumeiDecoder implements ErrorDecoder {

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

}
