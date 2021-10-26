package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
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

    @Autowired
    private OnlinePlatformService onlinePlatformService;

    /**
     * 拉取APP商品列表
     * 调用频率：180分钟1次
     * 入参格式：{ "onlinePlatformCode": "RBP" }
     * @return
     */
    @XxlJob(SystemConstants.DOWNLOAD_ONLINE_GOODS_LIST_JOB)
    public void downloadOnlineGoodsListJobHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            GoodsDownloadOnlineGoodsParam goodsDownloadOnlineGoodsParam = JSON.parseObject(param, GoodsDownloadOnlineGoodsParam.class);
            OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(goodsDownloadOnlineGoodsParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_ONLINEPLATFORMCODE);
                XxlJobHelper.handleFail(ERROR_ONLINEPLATFORMCODE);
                return;
            }
            //下载线上货品列表
            goodsService.downloadGoodsList(onlinePlatform);

        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }
}
