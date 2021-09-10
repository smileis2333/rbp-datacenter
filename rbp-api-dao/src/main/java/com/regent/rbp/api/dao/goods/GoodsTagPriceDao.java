package com.regent.rbp.api.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.GoodsTagPrice;
import com.regent.rbp.api.dto.goods.GoodsTagPriceDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface GoodsTagPriceDao extends BaseMapper<GoodsTagPrice> {

    List<GoodsTagPriceDto> selectGoodsTagPriceByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

}
