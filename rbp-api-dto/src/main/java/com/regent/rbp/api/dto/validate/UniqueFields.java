package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检测传参列表List的参数禁止重复
 * warn!!! 如果允许重复，则建议用Set，并在对应的Type上处理hashCode和equal问题
 * @author huangjie
 * @date : 2022/01/20
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = {})
public @interface UniqueFields {
    String message() default "{dumPair}重复-->{dupVal}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields() default {};
}
