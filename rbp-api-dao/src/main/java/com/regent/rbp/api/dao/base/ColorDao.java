package com.regent.rbp.api.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.ColorGroup;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-09
 */
@CacheNamespace
public interface ColorDao extends BaseMapper<Color> {

    Integer batchInsertColorGroup(@Param("list") List<ColorGroup> list);

}
