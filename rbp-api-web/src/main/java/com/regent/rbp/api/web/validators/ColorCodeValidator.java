package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dto.validate.ColorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Component
public class ColorCodeValidator implements ConstraintValidator<ColorCode, String> {
    @Autowired
    private ColorDao colorDao;

    @Override
    public boolean isValid(String colorCode, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(colorCode)) {
            return colorDao.selectOne(new QueryWrapper<Color>().eq("code", colorCode)) != null;
        }
        return true;
    }
}
