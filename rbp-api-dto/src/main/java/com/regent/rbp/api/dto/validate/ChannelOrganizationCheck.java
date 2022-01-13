package com.regent.rbp.api.dto.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangjie
 * @date : 2022/01/13
 * 校验渠道组织
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = { })
public @interface ChannelOrganizationCheck {
    String message() default "渠道组织架构输入错误(case1:不允许中间出现断层， case2:不允许出现查找不出的节点名)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
