package com.regent.rbp.task.yumei.config.yumei;

import com.regent.rbp.task.yumei.config.yumei.api.TokenResource;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author huangjie
 * @date : 2022/05/03
 * @description
 */
@Slf4j
public class YumeiTokenManager {
    private static final long DEFAULT_MIN_VALIDITY = 30;

    private YumeiToken currentToken;
    private long expirationTime;
    private long minTokenValidity = DEFAULT_MIN_VALIDITY;
    private final YumeiConfig config;
    private final TokenResource tokenService;

    public YumeiTokenManager(YumeiConfig config, TokenResource tokenService) {
        this.config = config;
        this.tokenService =tokenService;
    }

    public String getAccessTokenString() {
        return getAccessToken().getAccessToken();
    }

    public synchronized YumeiToken getAccessToken() {
        if (currentToken == null) {
            grantToken();
        }
        return currentToken;
    }

    public YumeiToken grantToken() {
        TokenResource.FetchTokenParam param = new TokenResource.FetchTokenParam(config.getAccount(), config.getPassword());

        synchronized (this) {
            YumeiRes<YumeiToken> response = tokenService.getYumeiToken(param);
            if (response.isSuccess()){
                currentToken = response.getData();
                // set token
            }else {
                log.error("玉美账号信息错误");
                // log to db
            }
        }
        return currentToken;
    }

    public synchronized void invalidate(String token) {
        if (currentToken == null) {
            return; // There's nothing to invalidate.
        }
        if (token.equals(currentToken.getAccessToken())) {
            // When used next, this cause a refresh attempt, that in turn will cause a grant attempt if refreshing fails.
            expirationTime = -1;
        }
    }

    public void invalidateToken() {
        synchronized(this){
            this.currentToken = null;
            log.warn("invalidate yumei token in {}", LocalDateTime.now());
        }
    }
}
