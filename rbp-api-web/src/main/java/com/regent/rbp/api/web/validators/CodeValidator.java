package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.Code;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;

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
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        if (StrUtil.isEmpty(code)) {
            return true;
        }
        List<Map<Object, Object>> res = baseDbDao.getListMap(String.format("select * from %s  where %s ='%s'", tableName, field, code));
        if (CollUtil.isEmpty(res)) {
            return false;
        }

        if (res.size() == 1) {
            Integer status = (Integer) res.get(0).get("status");
            if (status != null) {
                boolean flag = CheckEnum.CHECK.getStatus().equals(status);
                if (flag) {
                    return true;
                } else {
                    HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
                    context.disableDefaultConstraintViolation();
                    context.addExpressionVariable("validatedValue", code);
                    context.buildConstraintViolationWithTemplate("{regent.validation.constraints.noCheck}").addConstraintViolation();
                }
            }
        }

        return false;
    }
}
