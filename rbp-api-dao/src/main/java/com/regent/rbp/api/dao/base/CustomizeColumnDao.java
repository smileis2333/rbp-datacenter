package com.regent.rbp.api.dao.base;

import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeColumnValueDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface CustomizeColumnDao {

    List<CustomizeColumnDto> selectCustomizeColumnByModuleId(@Param("moduleId") String moduleId);

    List<CustomizeColumnDto> selectCustomizeColumnList(@Param("moduleIdList") List<String> moduleIdList);

    List<CustomizeColumnValueDto> selectCustomizeColumnValueList(@Param("customizeColumnIdList") List<Long> customizeColumnIdList);

    List<String> selectCustomizeColumnCodeByModuleId(@Param("moduleId") String moduleId);
}
