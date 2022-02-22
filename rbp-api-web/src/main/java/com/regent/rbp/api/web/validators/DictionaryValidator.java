package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class DictionaryValidator implements ConstraintValidator<Dictionary, String> {
    private String targetTable;
    private String targetField;
    private String op;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(Dictionary constraintAnnotation) {
        targetTable =  constraintAnnotation.targetTable();
        targetField = constraintAnnotation.targetField();
        op = constraintAnnotation.operator();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)){
            return true;
        }
        Long id = baseDbDao.getLongDataBySql(String.format("select id from %s where %s = '%s'", targetTable, targetField, value));
        return id!=null;
    }
}
