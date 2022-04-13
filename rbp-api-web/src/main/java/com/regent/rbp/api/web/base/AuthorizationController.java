package com.regent.rbp.api.web.base;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dto.base.ApplyTokenDto;
import com.regent.rbp.api.dto.base.TokenResultDto;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.web.config.DataCenterConfig;
import com.regent.rbp.api.web.constants.ApiConstants;
import com.regent.rbp.api.web.constants.DataCenterRedisPrefix;
import com.regent.rbp.infrastructure.annotation.PassToken;
import com.regent.rbp.infrastructure.util.RedisUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xuxing
 */
@RestController
@RequestMapping(ApiConstants.API_ACCESSTOKEN)
@Api(tags = "授权")
public class AuthorizationController {
    //24小时
    private static final long TOKEN_EXPIRE_TIME = 24 * 60 * 60;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    DataCenterConfig dataCenterConfig;

    @GetMapping
    @PassToken
    public Map<String, String> getAccessToken(@RequestParam String AppKey, @RequestParam String AppSecret) {
        Map<String, String> response = new HashMap<>();
        if(dataCenterConfig.getAppkey().equals(AppKey)
            && dataCenterConfig.getAppsecret().equals(AppSecret)) {
            String token = generateToken(AppKey, AppSecret);

            response.put("Flag", "1");
            response.put("message", "成功");
            response.put("data", token);
        } else {
            response.put("Flag", "-1");
            response.put("data", "");
            response.put("message", "AppId 或者 AppSecret 不正确");
        }
        return response;
    }

    @PostMapping
    @PassToken
    public ModelDataResponse<String> postAccessToken(@RequestBody ApplyTokenDto req) {
        ModelDataResponse<String> response = new ModelDataResponse<String>();
        if(req == null) {
            return ModelDataResponse.errorParameter("请求参数不正确");
        }
        if(req.getAppKey() == null || req.getAppSecret() == null) {
            return ModelDataResponse.errorParameter("请求参数不正确");
        }
        if(dataCenterConfig.getAppkey().equals(req.getAppKey())
                && dataCenterConfig.getAppsecret().equals(req.getAppSecret())) {
            String token = generateToken(req.getAppKey(), req.getAppSecret());
            return new ModelDataResponse<String>(token);
        } else {
            return ModelDataResponse.errorParameter("AppKey 或者 AppSecret 不正确");
        }
    }

    private String generateToken(String appKey, String appSecret) {
        String token = UUID.fastUUID().toString();
        TokenResultDto tokenResult = new TokenResultDto();
        tokenResult.setAppKey(appKey);
        tokenResult.setAppSecret(appSecret);
        tokenResult.setToken(token);

        redisUtil.set(DataCenterRedisPrefix.DATACENTER_TOKEN_KEY + token, JSON.toJSONString(tokenResult), TOKEN_EXPIRE_TIME);
        return token;
    }
}
