package com.regent.rbp.api.dao.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.label.Label;
import com.regent.rbp.api.dto.label.LabelQueryParam;
import com.regent.rbp.api.dto.label.LabelQueryResult;
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

    IPage<LabelQueryResult> searchPageData(@Param("pageModel") Page<Label> pageModel, @Param("param") LabelQueryParam param);

}
