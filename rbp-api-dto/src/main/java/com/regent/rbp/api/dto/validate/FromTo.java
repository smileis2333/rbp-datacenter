package com.regent.rbp.api.dto.validate;

import com.regent.rbp.api.dto.validate.group.Complex;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author huangjie
 * @date : 2021/12/29
 * toField的值由用户根据具体情况进行注入，该注解主要处理List下的元素带各类非直接引用的校验处理
 * example:
 * 入参的List参数类型带goodsCode，使用者处理后对全部元素注入goodsId，然后进行第二次校验
 * @see Complex
 * @description
 */
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = { })
public @interface FromTo {
    String message() default "{regent.validation.FromTo}";

    Class<?>[] groups() default {Complex.class};

    Class<? extends Payload>[] payload() default {};

    /**
     * 入参字段
     *
     * @return
     */
    String fromField();

    /**
     * 转换的字段
     *
     * @return
     */
    String toField();

    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FromTo[] value();
    }
}
