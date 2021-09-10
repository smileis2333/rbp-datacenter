package com.regent.rbp.api.service.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/10
 * @description 字段过滤工具类
 */
public class FieldFilterTool<T> {

    /**
     * 过滤属性字段
     *
     * @param list
     * @param filterFields
     * @param clazz
     * @return
     */
    public List<T> getFieldFilterList(List<T> list, String filterFields, Class<T> clazz) {
        if (list.size() <= 0 || StringUtils.isEmpty(filterFields)) {
            return list;
        }
        List<T> tempList = new ArrayList<>();
        try {
            //获取字段数组
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < list.size(); i++) {
                //创建对象
                T newInstance = clazz.newInstance();
                for (Field field : fields) {
                    //取消每个属性的安全检查 ,否則无法获取private字段值
                    field.setAccessible(true);
                    //存在即保留（确定需要筛选出来的字段）
                    if (filterFields.indexOf(field.getName()) < 0) {
                        continue;
                    }
                    //获取字段的属性
                    String type = field.getGenericType().toString();
                    Class typeTemp = getFieldType(type);
                    //获取属性名称
                    String name = field.getName();
                    String fieldName = name.substring(0, 1).toUpperCase() + name.substring(1);
                    //设置字段值
                    Method methodSet = newInstance.getClass().getMethod("set" + fieldName, typeTemp);
                    methodSet.invoke(newInstance, field.get(list.get(i)));
                }
                tempList.add(newInstance);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return list;
        }
        return tempList;
    }

    /**
     * 返回字段类型
     */
    public static Class getFieldType(String type) {
        Class typeTemp = null;
        switch (type) {
            case "class java.lang.String": {
                typeTemp = String.class;
                break;
            }
            case "class java.lang.Integer": {
                typeTemp = Integer.class;
                break;
            }
            case "class java.lang.Boolean": {
                typeTemp = Boolean.class;
                break;
            }
            case "class java.lang.Date": {
                typeTemp = Date.class;
                break;
            }
            case "class java.lang.Double": {
                typeTemp = Double.class;
                break;
            }
            case "class java.lang.Long": {
                typeTemp = Long.class;
                break;
            }
            default:
                break;
        }

        return typeTemp;
    }

}
