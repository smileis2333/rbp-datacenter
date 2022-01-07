package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.ConflictManualIdCheck;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
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
public class ConflictManualIdValidator implements ConstraintValidator<ConflictManualIdCheck, String> {
    protected String tableName;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(ConflictManualIdCheck constraintAnnotation) {
        if (StrUtil.isBlank(constraintAnnotation.targetTable())){
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
        this.tableName = constraintAnnotation.targetTable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(value) && StrUtil.isNotEmpty(tableName)) {
            String existRow = baseDbDao.getStringDataBySql(String.format("select manual_id from %s where manual_id = '%s' limit 1", tableName, value));
            return StrUtil.isEmpty(existRow);
        }
        return true;
    }
}
