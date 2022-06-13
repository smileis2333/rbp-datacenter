package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.purchase.AbstractBillSaveParam;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2022/04/06
 * @description
 */
@Component
public class CommonBillValidator {
    @Autowired
    private BaseDbDao baseDbDao;

    public boolean validate(AbstractBillSaveParam param, ConstraintValidatorContext constraintValidatorContext, String baseTable, String baseModuleId) {
        String moduleId = param.getModuleId();
        String manualId = param.getManualId();

        if (!StrUtil.isAllNotBlank(moduleId, manualId)) {
            return false;
        }

        HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        // close default constraint violation because we generate more detail violation
        context.disableDefaultConstraintViolation();

        boolean pass = true;

        if (!baseDbDao.isExist(String.format("select count(*) from rbp_module where base_module_id  = '%s' and id = '%s'", baseModuleId, param.getModuleId()))) {
            pass = false;
            context.addExpressionVariable("validatedValue", param.getModuleId());
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("moduleId").addConstraintViolation();
        }

        if (baseDbDao.isExist(String.format("select count(*) from %s where manual_id  = '%s'", baseTable, param.getManualId()))) {
            pass = false;
            context.addExpressionVariable("validatedValue", param.getManualId());
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.notAllowRepeat}").addPropertyNode("manualId").addConstraintViolation();
        }

        return pass;
    }

}
