package com.regent.rbp.api.dto.base;

import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class TokenResultDto {
    private String appKey;
    private String appSecret;
    private String token;
}
