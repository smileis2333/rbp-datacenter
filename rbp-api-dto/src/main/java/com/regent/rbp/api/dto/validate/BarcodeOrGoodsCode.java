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
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = { })
public @interface BarcodeOrGoodsCode {
    String message() default "条形码(barcode)与其他相关货品信息(goodsCode+ size + longName + ... )只能且必须选其中一种方式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
