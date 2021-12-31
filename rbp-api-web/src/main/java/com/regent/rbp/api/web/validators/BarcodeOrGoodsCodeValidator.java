package com.regent.rbp.api.web.validators;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dto.validate.BarcodeOrGoodsCode;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/30
 * @description
 */
@Component
public class BarcodeOrGoodsCodeValidator implements ConstraintValidator<BarcodeOrGoodsCode, Object> {
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        String barcode = BeanUtil.getProperty(obj, "barcode");
        String goodsCode = BeanUtil.getProperty(obj, "goodsCode");
        return (StrUtil.isNotBlank(barcode) && StrUtil.isEmpty(goodsCode)) || (StrUtil.isNotBlank(goodsCode) && StrUtil.isEmpty(barcode));
    }
}
