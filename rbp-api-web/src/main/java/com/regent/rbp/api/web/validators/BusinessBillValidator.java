package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.purchase.AbstractBusinessBillSaveParam;
import com.regent.rbp.api.dto.validate.BusinessBill;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2022/04/06
 * @description
 */
@Component
public class BusinessBillValidator implements ConstraintValidator<BusinessBill, AbstractBusinessBillSaveParam> {
    private String baseModuleId;

    private String baseTable;

    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private CommonBillValidator commonBillValidator;

    @Override
    public void initialize(BusinessBill constraintAnnotation) {
        baseModuleId = constraintAnnotation.baseModuleId();
        baseTable = constraintAnnotation.baseTable();
    }

    @Override
    public boolean isValid(AbstractBusinessBillSaveParam param, ConstraintValidatorContext constraintValidatorContext) {
        if (!commonBillValidator.validate(param, constraintValidatorContext, baseTable, baseModuleId)) {
            return false;
        }

        if (StrUtil.isEmpty(param.getBusinessType())) {
            return true;
        }

        HibernateConstraintValidatorContext context = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class);
        boolean pass = true;

        if (!baseDbDao.isExist(String.format("select\n" +
                "\tcount(*) \n" +
                "from\n" +
                "\trbp_module_business_type rmbt\n" +
                "\tleft join rbp_business_type rbt on rmbt.business_type_id = rbt.id\n" +
                "where\n" +
                "\tmodule_id = '%s'\n" +
                "\tand rbt.name  = '%s'", param.getModuleId(), param.getBusinessType()))) {
            pass = false;
            context.addExpressionVariable("validatedValue", param.getBusinessType());
            context.buildConstraintViolationWithTemplate("{regent.validation.constraints.mapNotFound}").addPropertyNode("businessType").addConstraintViolation();
        }

        return pass;
    }
}
