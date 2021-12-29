package com.regent.rbp.api.dto.validate;

import javax.validation.Payload;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @description
 */
public @interface BarcodeCheck {
    String message() default "条形码不存在";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
