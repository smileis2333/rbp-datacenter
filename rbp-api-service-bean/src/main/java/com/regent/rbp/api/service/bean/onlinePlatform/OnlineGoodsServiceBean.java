package com.regent.rbp.api.service.bean.onlinePlatform;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.omiChannel.OnlineGoods;
import com.regent.rbp.api.dao.onlinePlatform.OnlineGoodsDao;
import com.regent.rbp.api.service.onlinePlatform.OnlineGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author xuxing
 */
@Service
public class OnlineGoodsServiceBean extends ServiceImpl<OnlineGoodsDao, OnlineGoods> implements OnlineGoodsService {

    @Autowired
    private OnlineGoodsDao onlineGoodsDao;

    @Override
    @Transactional
    public void saveOrUpdateList(List<OnlineGoods> updateOnlineGoodsList, List<OnlineGoods> insertOnlineGoodsList) {
        onlineGoodsDao.insertBatch(insertOnlineGoodsList);
        for(OnlineGoods item : updateOnlineGoodsList) {
            onlineGoodsDao.updateById(item);
        }
    }
}
