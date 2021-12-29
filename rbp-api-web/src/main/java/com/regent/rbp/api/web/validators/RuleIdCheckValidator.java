package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dto.validate.RuleIdCheck;
import com.regent.rbp.common.constants.InformationConstants;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Component
public class RuleIdCheckValidator implements ConstraintValidator<RuleIdCheck, String> {

    @Override
    public boolean isValid(String ruleId, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(ruleId)){
            Field[] declaredFields = InformationConstants.BarcodeRuleConstants.class.getDeclaredFields();

        }
        return true;
    }
}
