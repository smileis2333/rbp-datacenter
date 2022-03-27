package com.regent.rbp.api.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.base.LongInfo;
import org.apache.ibatis.annotations.CacheNamespace;

/**
 * @author chenchungui
 * @date 2021-09-10
 */
@CacheNamespace
public interface LongDao extends BaseMapper<LongInfo> {
}
