package com.regent.rbp.api.web.validators;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dto.validate.BarcodeRelationCheck;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@Component
public class BarcodeRelationCheckValidator implements ConstraintValidator<BarcodeRelationCheck, Object> {
    private static final String f1 = "longId";
    private static final String f2 = "sizeId";
    private static final String f3 = "goodsId";

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        String barcode = BeanUtil.getProperty(obj, "barcode");
        if (StrUtil.isBlank(barcode)) {
            return true;
        }
        Object v1 = BeanUtil.getProperty(obj, f1);
        Object v2 = BeanUtil.getProperty(obj, f2);
        Object v3 = BeanUtil.getProperty(obj, f3);
        return ObjectUtil.isAllNotEmpty(v1, v2, v3);
    }
}
