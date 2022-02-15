package com.regent.rbp.api.dao.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.label.Label;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
public interface LabelDao extends BaseMapper<Label> {
    @Select("select * from rbp_label where code = #{code}")
    Label selectByCode(@Param("code") String code);
}
