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
 * @date : 2022/01/19
 * @description
 */
@Component
public class CodeValidatorForQueryList implements ConstraintValidator<Code, List<String>> {
    private String tableName;
    private String field;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(Code constraintAnnotation) {
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
        List<Long> ids = baseDbDao.getLongListDataBySql(String.format("select id from %s  where %s in ('%s')", field, tableName, codes));
        return CollUtil.isNotEmpty(ids);
    }
}
