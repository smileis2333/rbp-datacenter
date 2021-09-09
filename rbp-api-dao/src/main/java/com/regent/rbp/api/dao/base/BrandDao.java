package com.regent.rbp.api.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.dto.base.BaseData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface BrandDao extends BaseMapper<Brand> {

    IPage<BaseData> searchPageData(@Param("pageModel") Page<BaseData> pageModel,
                                   @Param("tableName") String tableName, @Param("keyword") String keyword);

    List<String> getExistBaseDataList(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("list") List<String> list);

    Integer batchInsert(@Param("tableName") String tableName, @Param("list") List<Brand> list);
}
