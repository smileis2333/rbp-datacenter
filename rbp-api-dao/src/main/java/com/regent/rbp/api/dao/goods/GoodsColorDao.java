package com.regent.rbp.api.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.GoodsColor;
import com.regent.rbp.api.dto.goods.GoodsColorDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface GoodsColorDao extends BaseMapper<GoodsColor> {
    List<GoodsColorDto> selectGoodsColorByGoodsIds(@Param("goodsIds") List<Long> goodsIds);
}
