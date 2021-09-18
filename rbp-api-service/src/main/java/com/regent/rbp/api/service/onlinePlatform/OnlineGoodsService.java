package com.regent.rbp.api.service.onlinePlatform;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rbp.api.core.omiChannel.OnlineGoods;

import java.util.List;

/**
 * @author xuxing
 */
public interface OnlineGoodsService extends IService<OnlineGoods> {
    void saveOrUpdateList(List<OnlineGoods> updateOnlineGoodsList, List<OnlineGoods> insertOnlineGoodsList);
}
