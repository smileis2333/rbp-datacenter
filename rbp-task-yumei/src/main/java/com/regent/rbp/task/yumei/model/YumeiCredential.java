package com.regent.rbp.task.yumei.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Data
@AllArgsConstructor
public class YumeiCredential {
    private String accessToken;
    private String refreshToken;
    private Long expiresTime;
}
