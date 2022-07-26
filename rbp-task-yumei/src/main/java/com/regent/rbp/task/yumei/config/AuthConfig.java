package com.regent.rbp.task.yumei.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.task.yumei.constants.YumeiApiUrl;
import com.regent.rbp.task.yumei.model.YumeiCredential;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Slf4j
@Configuration
public class AuthConfig {

    @Value("${yumei.url:}")
    private String url;

    @Value("${yumei.account:}")
    private String account;

    @Value("${yumei.password:}")
    private String password;
    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public YumeiCredential getCredential() {
        if (StrUtil.isAllNotBlank(url, account, password)) {
            FetchWrapper body = new FetchWrapper(account, password);
            try {
                String jsonBody = objectMapper.writeValueAsString(body);
                HttpResponse response = HttpUtil.createPost(url + YumeiApiUrl.ACCESS_TOKEN).contentType("application/json").body(jsonBody).execute();
                if (response.isOk()) {
                    String returnBody = response.body();
                    log.info("玉美获取token结果：" + returnBody);
                    Map<String, Object> returnData = objectMapper.readValue(returnBody, Map.class);
                    if ("00000".equals(returnData.get("code"))) {
                        Map<String, Object> data = (Map<String, Object>) returnData.get("data");
                        Integer expiresIn = (Integer)data.getOrDefault("expiresIn", 0);

                        return new YumeiCredential((String) data.get("accessToken"), (String) data.get("refreshToken"), expiresIn.longValue());
                    } else {
                        throw new BusinessException(ResponseCode.PARAMS_ERROR, "玉美配置信息错误");
                    }
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                log.error("玉美获取token异常");
                e.printStackTrace();
            }
        }
        return YumeiCredential.credential;
    }

    @Data
    @AllArgsConstructor
    class FetchWrapper {
        private String account;
        private String password;
    }

}
