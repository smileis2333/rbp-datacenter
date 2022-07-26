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
 * <p>
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
            putConstraint(tmpConstraints, BusinessTypeCheck.class, BusinessTypeValidatorForSingle.class);
            putConstraint(tmpConstraints, ChannelCodeCheck.class, ChannelCodeValidatorForSingle.class);
            putConstraint(tmpConstraints, ConflictManualIdCheck.class, ConflictManualIdValidator.class);
            putConstraint(tmpConstraints, FromTo.class, FromToValidator.class);
            putConstraint(tmpConstraints, BillNo.class, BillNoValidator.class);
            putConstraint(tmpConstraints, SupplierCodeCheck.class, SupplierCodeValidatorForSingle.class);
            putConstraint(tmpConstraints, ChannelOrganizationCheck.class, ChannelOrganizationCheckValidator.class);
            putConstraint(tmpConstraints, DiscreteRange.class, DiscreteRangeValidator.class);
            putConstraint(tmpConstraints, Code.class, CodeValidator.class);
            putConstraint(tmpConstraints, UniqueFields.class, UniqueFieldsValidator.class);
            putConstraints(tmpConstraints, Dictionary.class, DictionaryValidator.class,DictionaryValidatorForCollection.class);
            putConstraint(tmpConstraints, GoodsInfo.class, GoodsInfoValidator.class);
            putConstraint(tmpConstraints, Bill.class, BillValidator.class);
            putConstraint(tmpConstraints, BusinessBill.class, BusinessBillValidator.class);
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