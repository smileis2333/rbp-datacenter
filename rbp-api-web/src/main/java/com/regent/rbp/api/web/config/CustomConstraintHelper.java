package com.regent.rbp.api.web.config;

import com.regent.rbp.api.dto.validate.*;
import com.regent.rbp.api.web.validators.*;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorDescriptor;
import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
import org.hibernate.validator.internal.util.CollectionHelper;

import javax.validation.ConstraintValidator;
import javax.validation.spi.ValidationProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huangjie
 * @date : 2021/12/29
 * @description
 * @see ValidationProvider
 * for unreasonable/excess package arrangement(split the transport object from context),
 * so we need to split validate annotations and concrete validator,
 * the implment of this class is copy all config item from hibenrate default value,
 * and then inject regent custom validator(using reflect), and write back.
 * the key point is the jsr303 SPI.
 *
 * META-INF/services/javax.validation.spi.ValidationProvider
 */
@Log4j2
public class CustomConstraintHelper extends ConstraintHelper {

    public CustomConstraintHelper() {
        super();
    }

    void modify() {
        Field field = null;
        try {
            field = this.getClass().getSuperclass().getDeclaredField("builtinConstraints");
            field.setAccessible(true);

            Object o = field.get(this);

            // 因为field被定义为了private final，且实际类型为
            // this.builtinConstraints = Collections.unmodifiableMap( tmpConstraints );
            // 因为不能修改，所以我这里只能拷贝到一个新的hashmap，再反射设置回去
            Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>> tmpConstraints = new HashMap<>();
            tmpConstraints.putAll((Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>>) o);
            // 在这里注册我们自定义的注解和注解处理器
            putConstraint(tmpConstraints, BarcodeCheck.class, BarcodeValidatorForQueryList.class);
            putConstraints(tmpConstraints, BillStatus.class, BillStatusValidator.class, BillStatusValidatorForQueryList.class);
            putConstraints(tmpConstraints, BusinessTypeCheck.class, BusinessTypeValidatorForSingle.class, BusinessTypeValidatorForQueryList.class);
            putConstraints(tmpConstraints, ChannelCodeCheck.class, ChannelCodeValidatorForSingle.class, ChannelCodeValidatorForQueryList.class);
            putConstraint(tmpConstraints, ColorCode.class, ColorCodeValidator.class);
            putConstraint(tmpConstraints, ConflictManualIdCheck.class, ConflictManualIdValidator.class);
            putConstraints(tmpConstraints, CurrencyTypeCheck.class, CurrencyTypeCheckValidatorForQueryList.class,CurrencyTypeCheckValidatorForSingle.class);
            putConstraint(tmpConstraints, FromTo.class, FromToValidator.class);
            putConstraint(tmpConstraints, BillNo.class, BillNoValidator.class);
            putConstraints(tmpConstraints, SupplierCodeCheck.class, SupplierCodeValidatorForSingle.class, SupplierCodeValidatorForQueryList.class);
            putConstraint(tmpConstraints, ChannelOrganizationCheck.class, ChannelOrganizationCheckValidator.class);
            putConstraint(tmpConstraints, DiscreteRange.class, DiscreteRangeValidator.class);
            putConstraints(tmpConstraints, Code.class, CodeValidatorForQueryList.class,CodeValidator.class);
            putConstraint(tmpConstraints, UniqueFields.class, UniqueFieldsValidator.class);
            putConstraint(tmpConstraints, Dictionary.class, DictionaryValidator.class);
            putConstraint(tmpConstraints, GoodsInfo.class, GoodsInfoValidator.class);
            /**
             * 设置回field
             */
            field.set(this, tmpConstraints);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("{}", e);
        }

    }

    private static <A extends Annotation> void putConstraint(Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>> validators,
                                                             Class<A> constraintType, Class<? extends ConstraintValidator<A, ?>> validatorType) {
        validators.put(constraintType, Collections.singletonList(ConstraintValidatorDescriptor.forClass(validatorType, constraintType)));
    }

    private static <A extends Annotation> void putConstraints(Map<Class<? extends Annotation>, List<ConstraintValidatorDescriptor<?>>> validators,
                                                              Class<A> constraintType, Class<? extends ConstraintValidator<A, ?>> validatorType1, Class<? extends ConstraintValidator<A, ?>> validatorType2) {
        List<ConstraintValidatorDescriptor<?>> descriptors = Stream.of(validatorType1, validatorType2)
                .map(vt -> ConstraintValidatorDescriptor.forClass(vt, constraintType))
                .collect(Collectors.toList());

        validators.put(constraintType, CollectionHelper.toImmutableList(descriptors));
    }

}