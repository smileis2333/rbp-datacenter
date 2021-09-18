package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.omiChannel.OnlineGoods;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlineGoodsDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.service.onlinePlatform.OnlineGoodsService;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.GoodsDto;
import com.regent.rbp.task.inno.model.dto.GoodsSearchDto;
import com.regent.rbp.task.inno.model.dto.SkuDto;
import com.regent.rbp.task.inno.model.param.GoodsDownloadOnlineGoodsParam;
import com.regent.rbp.task.inno.model.req.GoodsSearchReqDto;
import com.regent.rbp.task.inno.model.resp.GoodsSearchRespDto;
import com.regent.rbp.task.inno.service.GoodsService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
