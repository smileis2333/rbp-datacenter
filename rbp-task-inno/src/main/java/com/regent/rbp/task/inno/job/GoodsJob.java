package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.task.inno.model.param.GoodsDownloadOnlineGoodsParam;
import com.regent.rbp.task.inno.service.GoodsService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xuxing
 */
@Component
public class GoodsJob {

    private static final String ERROR_ONLINEPLATFORMCODE = "[inno拉取APP商品列表]:onlinePlatformCode电商平台编号参数值不存在";

    @Autowired
    private GoodsService goodsService;

    /**
     * 拉取APP商品列表
     * 调用频率：180分钟1次
     * 入参格式：{ "onlinePlatformCode": "RBP" }
     * @return
     */
    @XxlJob("inno.downloadOnlineGoodsListJobHandler")
    public void downloadOnlineGoodsListJobHandler() {

        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            GoodsDownloadOnlineGoodsParam goodsDownloadOnlineGoodsParam = JSON.parseObject(param, GoodsDownloadOnlineGoodsParam.class);
            Long onlinePlatformId = goodsService.getOnlinePlatformId(goodsDownloadOnlineGoodsParam);

            if(onlinePlatformId == null) {
                XxlJobHelper.log(ERROR_ONLINEPLATFORMCODE);
                XxlJobHelper.handleFail(ERROR_ONLINEPLATFORMCODE);
                return;
            }
            //下载线上货品列表
            goodsService.downloadGoodsList(onlinePlatformId);

        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }
}
