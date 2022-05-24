package com.regent.rbp.task.yumei.config.yumei;

import lombok.Data;

/**
 * @author huangjie
 * @date : 2022/04/29
 * @description
 */
@Data
public class YumeiRes<T> {
    private String code;

    private boolean success;

    private T data;

    private String msg;
}
