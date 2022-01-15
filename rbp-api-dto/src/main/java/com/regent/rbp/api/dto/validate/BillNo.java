package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 给定入参，设置 targetTable 用于自动检测目标表是否不存在对应 bill_no 的该记录
 * @author huangjie
 * @date : 2022/01/08
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface BillNo {
    String message() default "{regent.validation.constraints.mapNotFound}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String targetTable();

}
