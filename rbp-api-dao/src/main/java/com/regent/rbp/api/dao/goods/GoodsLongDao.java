package com.regent.rbp.api.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.GoodsLong;
import com.regent.rbp.api.dto.goods.GoodsLongDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
public interface GoodsLongDao extends BaseMapper<GoodsLong> {

    List<GoodsLongDto> selectGoodsLongByGoodsIds(@Param("goodsIds") List<Long> goodsIds);
}
