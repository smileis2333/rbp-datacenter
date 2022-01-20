package com.regent.rbp.api.dao.box;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.box.BoxDetail;
import com.regent.rbp.api.dto.box.BoxDetailItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/18
 * @description
 */
public interface BoxDetailDao extends BaseMapper<BoxDetail> {
    List<BoxDetailItem> searchList(@Param("boxIds") List<Long> boxIds);
}
