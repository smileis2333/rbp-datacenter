package com.regent.rbp.task.yumei.config;

import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.task.yumei.config.YumeiConfig;
import com.regent.rbp.task.yumei.model.YumeiCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Configuration
public class AuthConfig {

    @Autowired
    private YumeiConfig yumeiConfig;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public YumeiCredential getCredential() {
        FetchWrapper body = new FetchWrapper(yumeiConfig.getAccount(), yumeiConfig.getPassword());

        try {
            String jsonBody = objectMapper.writeValueAsString(body);
            String returnBody = HttpUtil.createPost(yumeiConfig.getUrl()).contentType("application/json").body(jsonBody).execute().body();
            Map<String, Object> returnData = objectMapper.readValue(returnBody, Map.class);
            if ("00000".equals(returnData.get("code"))) {
                Map<String, Object> data = (Map<String, Object>) returnData.get("data");
                return new YumeiCredential((String) data.get("accessToken"), (String) data.get("refreshToken"), (Long) data.get("expiresIn"));
            } else {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "玉美配置信息错误");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Data
    @AllArgsConstructor
    class FetchWrapper {
        private String account;
        private String password;
    }

}
