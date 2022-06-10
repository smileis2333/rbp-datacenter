package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.Dictionary;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * todo support status check
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class DictionaryValidatorForCollection implements ConstraintValidator<Dictionary, Set<String>> {
    private String targetTable;
    private String targetField;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(Dictionary constraintAnnotation) {
        targetTable =  constraintAnnotation.targetTable();
        targetField = constraintAnnotation.targetField();
    }

    @Override
    public boolean isValid(Set<String> materials, ConstraintValidatorContext constraintValidatorContext) {
        if (CollUtil.isEmpty(materials)){
            return true;
        }
        HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        context.disableDefaultConstraintViolation();
        boolean pass = true;
        List<Map<Object, Object>> items = baseDbDao.getListMap(String.format("select id,%s from %s where %s in %s", targetField, targetTable, targetField, materials.stream().map(e->String.format("'%s'",e)).collect(Collectors.joining(",", "(", ")"))));
        Map<String, Long> checkMap = items.stream().collect(Collectors.toMap(e -> (String) e.get(targetField), e -> (Long) e.get("id")));
        List<String>fails = new ArrayList<>();

        for (String material : materials) {
            if (!checkMap.containsKey(material)) {
                pass = false;
                fails.add(material);
            }
        }
        if (!pass) {
            context.addExpressionVariable("validatedValue", fails.stream().collect(Collectors.joining(",","[","]")));
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addConstraintViolation();
        }
        return pass;
    }


}
