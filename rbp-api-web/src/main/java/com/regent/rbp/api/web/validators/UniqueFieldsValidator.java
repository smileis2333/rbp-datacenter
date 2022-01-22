package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.validate.UniqueFields;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author huangjie
 * @date : 2022/01/20
 * @description
 */
public class UniqueFieldsValidator implements ConstraintValidator<UniqueFields, List<?>> {
    private Set<String> fieldNames;

    @Override
    public void initialize(UniqueFields constraintAnnotation) {
        if (constraintAnnotation.fields().length == 0) {
            throw new RuntimeException("illegal @UniqueFields config, fields must not be empty");
        }
        fieldNames = CollUtil.newHashSet(constraintAnnotation.fields());
        if (fieldNames.size() != constraintAnnotation.fields().length) {
            throw new RuntimeException("illegal @UniqueFields config, fields must be unique");
        }
    }

    @Override
    public boolean isValid(List<?> items, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext unwrapContext = context.unwrap(HibernateConstraintValidatorContext.class);
        if (CollUtil.isEmpty(items)) {
            return true;
        }
        List<Field> fields = new ArrayList<>();
        Object o = items.get(0);
        for (Field field : o.getClass().getDeclaredFields()) {
            if (fieldNames.contains(field.getName())) {
                fields.add(field);
                field.setAccessible(true);
            }
        }
        if (fields.size() != fieldNames.size()) {
            throw new RuntimeException("illegal @UniqueFields config, declare fields lack");
        }
        Map<String, Integer> keyHashMap = new HashMap<>();
        Map<Integer, Integer> hashCount = new HashMap<>();
        Object[] values = new Object[fields.size()];
        for (Object item : items) {
            for (int i = 0; i < fields.size(); i++) {
                try {
                    values[i] = fields.get(i).get(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            int hash = Arrays.hashCode(values);
            String key = Arrays.toString(values);
            keyHashMap.put(key, hash);
            hashCount.put(hash, hashCount.getOrDefault(hash, 0) + 1);
        }
        Set<String> msg = new HashSet<>();
        for (Object item : items) {
            for (int i = 0; i < fields.size(); i++) {
                try {
                    values[i] = fields.get(i).get(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            String key = Arrays.toString(values);
            if (hashCount.getOrDefault(keyHashMap.get(key), 0) > 1) {
                msg.add(key);
            }
        }
        if (msg.isEmpty()) {
            return true;
        }
        unwrapContext.addMessageParameter("dupVal", msg);
        unwrapContext.addMessageParameter("dumPair", Arrays.toString(fields.stream().map(Field::getName).toArray()));
        return false;
    }
}
