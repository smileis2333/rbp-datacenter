package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.Name;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/01/19
 * @description
 */
@Component
public class NameValidatorForQueryList implements ConstraintValidator<Name, List<String>> {
    private String tableName;
    private String field;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(Name constraintAnnotation) {
        if (StrUtil.isBlank(constraintAnnotation.targetTable())) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR);
        }
        this.tableName = constraintAnnotation.targetTable();
    }

    @Override
    public boolean isValid(List<String> codes, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(codes)) {
            return true;
        }
        List<Long> ids = baseDbDao.getLongListDataBySql(String.format("select id from %s  where %s REGEXP  '%s'", field, tableName, codes.stream().collect(Collectors.joining("|"))));
        return CollUtil.isNotEmpty(ids);
    }
}
