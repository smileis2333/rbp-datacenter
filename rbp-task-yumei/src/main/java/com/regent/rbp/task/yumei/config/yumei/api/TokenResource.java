package com.regent.rbp.task.yumei.config.yumei.api;

import com.regent.rbp.task.yumei.config.yumei.RawDecoderConfig;
import com.regent.rbp.task.yumei.config.yumei.YumeiRes;
import com.regent.rbp.task.yumei.config.yumei.YumeiToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author huangjie
 * @date : 2022/05/04
 * @description
 */
@FeignClient(value = "yumeiTokenResource", url = "${yumei.url:undefined}", configuration = RawDecoderConfig.class)
public interface TokenResource {
    @RequestMapping(method = RequestMethod.POST, value = "/auth/accessToken")
    YumeiRes<YumeiToken> getYumeiToken(@RequestBody FetchTokenParam param);

    @Data
    @AllArgsConstructor
    public static class FetchTokenParam{
        private String account;
        private String password;
    }
}
