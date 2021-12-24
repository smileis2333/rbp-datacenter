package com.regent.rbp.api.web.bill.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2021/12/23
 * @description
 */
@Constraint(validatedBy = CurrencyTypeCheckValidator.class )
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CurrencyTypeCheck {
    String message() default "币种类型非法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
