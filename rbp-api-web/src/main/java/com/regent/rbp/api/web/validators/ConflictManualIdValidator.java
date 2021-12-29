package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dto.validate.ConflictManualIdCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class ConflictManualIdValidator implements ConstraintValidator<ConflictManualIdCheck, String> {
    protected String tableName;
    @Autowired
    private BaseDbDao baseDbDao;

    @Override
    public void initialize(ConflictManualIdCheck constraintAnnotation) {
        String className = constraintAnnotation.targetClass();
        try {
            Class<?> entityClass = Class.forName(className);
            String tableName = (String) TableName.class.getDeclaredMethod("value").invoke(entityClass.getAnnotation(TableName.class));
            this.tableName = tableName;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(value) && StrUtil.isNotEmpty(tableName)) {
            String existRow = baseDbDao.getStringDataBySql(String.format("select manual_id from %s where manual_id = '%s' limit 1", tableName, value));
            return StrUtil.isEmpty(existRow);
        }
        return true;
    }
}
