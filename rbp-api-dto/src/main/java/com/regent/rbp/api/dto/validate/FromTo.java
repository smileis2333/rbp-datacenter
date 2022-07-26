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
 * fromField，toField用于反射读取数据，而前者还有一个目的用于处理报错路径的json 回显
 * 基于jsr303的数组Bean校验，只能到xx[index]，处理到具体的回显目前通过异常解析器拼进去，
 * 第二种做法可以通过
 * @see com.regent.rbp.api.dto.base.BarcodeDto#isSize()
 * 类似的做法，即is开头的xxxx方法做校验，也可能实现基于Bean的cross param validate的回显
 * @see Complex
 * @see 可读性，建议从hibernate context在校验时，生成具体信息，其可以自动导航
 * @description
 */
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = { })
@Deprecated
public @interface FromTo {
    String message() default
            "${fromField==\"goodsCode\"?validatedValue.goodsCode:''}"+
            "${fromField==\"colorCode\"?validatedValue.colorCode:''}"+
            "${fromField==\"longName\"?validatedValue.longName:''}"+
            "${fromField==\"size\"?validatedValue.size:''}"+
            "${fromField==\"retailPayTypeCode\"?validatedValue.retailPayTypeCode:''}"+
            "${fromField==\"employeeCode\"?validatedValue.employeeCode:''}"+
            "${fromField==\"barcode\"?validatedValue.barcode:''}"+
            "不存在";

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
