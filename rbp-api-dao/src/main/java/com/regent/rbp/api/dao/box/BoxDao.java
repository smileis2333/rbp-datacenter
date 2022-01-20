package com.regent.rbp.api.dao.box;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.box.Box;
import com.regent.rbp.api.dto.box.BoxQueryParam;
import com.regent.rbp.api.dto.box.BoxQueryResult;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
public interface BoxDao extends BaseMapper<Box> {
    IPage<BoxQueryResult> searchPageData(@Param("pageModel") Page<Box> pageModel, @Param("param") BoxQueryParam param);
}
