package com.regent.rbp.api.dao.onlinePlatform;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.omiChannel.OnlineGoods;

import java.util.List;

/**
 * @author xuxing
 */
public interface OnlineGoodsDao extends BaseMapper<OnlineGoods> {
    void insertBatch(List<OnlineGoods> list);
    void updateBatch(List<OnlineGoods> list);
}
