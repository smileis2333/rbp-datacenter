package com.regent.rbp.api.dao.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.dto.base.BarcodeDto;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
@CacheNamespace
public interface BarcodeDao extends BaseMapper<Barcode> {
    List<BarcodeDto> selectBarcodeByGoodsIds(@Param("goodsIds") List<Long> goodsIds);
}
