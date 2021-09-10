package com.regent.rbp.api.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.SizeDisable;
import com.regent.rbp.api.dto.goods.DisableSizeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface SizeDisableDao extends BaseMapper<SizeDisable> {
    List<DisableSizeDto> selectGoodsDisableSizeByGoodsIds(@Param("goodsIds") List<Long> goodsIds);
}
