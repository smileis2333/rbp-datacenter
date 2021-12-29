package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2021/12/30
 * barcode存在情况下必须满足longId,sizeId,goodsId三者必须存在，
 * 该注解作为组合检验，作用域为普通Bean，而非单一类型，主要目的为表单的一次性注入处理后的第二次校验
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = { })
public @interface BarcodeRelationCheck {
    String message() default "单据状态非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
