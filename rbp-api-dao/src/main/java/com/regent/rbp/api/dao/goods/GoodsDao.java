package com.regent.rbp.api.dao.goods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
public interface GoodsDao extends BaseMapper<Goods> {

    List<Map> selectGoodsCustomByGoodsIds(@Param("goodsIds") List<Long> goodsIds, @Param("customizeColumns") List<String> customizeColumns);

}
