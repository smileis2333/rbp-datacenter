package com.regent.rbp.api.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.base.SizeClass;
import com.regent.rbp.api.core.base.SizeDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface SizeClassDao extends BaseMapper<SizeClass> {

    Integer batchInsertSizeDetail(@Param("list") List<SizeDetail> list);

}
