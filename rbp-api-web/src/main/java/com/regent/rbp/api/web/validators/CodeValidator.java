package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.Code;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/02/22
 * @description
 */
@Component
public class CodeValidator implements ConstraintValidator<Code, String> {
    private String tableName;
    private String field;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(Code constraintAnnotation) {
        field = constraintAnnotation.targetField();
        if (StrUtil.isBlank(constraintAnnotation.targetTable())) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
        this.tableName = constraintAnnotation.targetTable();
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (StrUtil.isEmpty(code)) {
            return true;
        }
        List<Long> ids = baseDbDao.getLongListDataBySql(String.format("select id from %s  where %s in ('%s')", tableName, field, code));
        return CollUtil.isNotEmpty(ids);
    }
}
