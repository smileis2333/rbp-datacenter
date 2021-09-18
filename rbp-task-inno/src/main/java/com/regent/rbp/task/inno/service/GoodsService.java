package com.regent.rbp.task.inno.service;

import com.regent.rbp.task.inno.model.param.GoodsDownloadOnlineGoodsParam;

/**
 * @author xuxing
 */
public interface GoodsService {

    /**
     * 拉取APP商品列表
     * @param onlinePlatformId
     */
    void downloadGoodsList(Long onlinePlatformId);

    /**
     * 转换参数(电商平台编号)
     * @param goodsDownloadOnlineGoodsParam
     * @return
     */
    Long getOnlinePlatformId(GoodsDownloadOnlineGoodsParam goodsDownloadOnlineGoodsParam);

}
