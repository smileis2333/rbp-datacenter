package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.param.ChannelUploadingParam;
import com.regent.rbp.task.inno.model.param.GoodsDownloadOnlineGoodsParam;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.service.ChannelService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxing
 */
@Slf4j
@Component
public class ChannelJob {

    private static final String ERROR_CHANNEL_ONLINEPLATFORMCODE = "[inno推送店铺信息]:onlinePlatformCode电商平台编号参数值不存在";
    private static final String ERROR_CHANNEL_List = "[inno推送店铺信息]:当前无店铺信息需要同步";
    private static final String ERROR_WAREHOUSE_ONLINEPLATFORMCODE = "[inno推送仓库信息]:onlinePlatformCode电商平台编号参数值不存在";
    private static final String ERROR_WAREHOUSE_LIST = "[inno推送仓库信息]:当前无仓库信息需要同步";

    @Autowired
    ChannelService channelService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 同步渠道
     * 请求Json：{ "onlinePlatformCode": "RBP" }
     */
    @XxlJob(SystemConstants.POST_ERP_STORE)
    public void uploadingChannel() {
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            ChannelUploadingParam channelUploadingParam = JSON.parseObject(param, ChannelUploadingParam.class);
            OnlinePlatform onlinePlatform = channelService.getOnlinePlatform(channelUploadingParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_CHANNEL_ONLINEPLATFORMCODE);
                XxlJobHelper.handleFail(ERROR_CHANNEL_ONLINEPLATFORMCODE);
                return;
            }
            //开始推送云仓
            if (onlinePlatform.getWarehouseId() != null) {
                ChannelRespDto resp = channelService.uploadingChannel(onlinePlatform.getId(), onlinePlatform.getChannelId());
                if (resp == null) {
                    XxlJobHelper.log(ERROR_CHANNEL_List);
                }
                else if (resp.getCode().equals("-1")) {
                    new Exception(resp.getMsg());
                }
                XxlJobHelper.log("请求成功：" + JSON.toJSONString(resp));
            } else {
                XxlJobHelper.log(ERROR_CHANNEL_List);
            }
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

    /**
     * 同步渠道
     * 请求Json：{ "onlinePlatformCode": "RBP" }
     */
    @XxlJob(SystemConstants.POST_ERP_WAREHOUSE)
    public void uploadingWarehouse() {
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            ChannelUploadingParam channelUploadingParam = JSON.parseObject(param, ChannelUploadingParam.class);
            OnlinePlatform onlinePlatform = channelService.getOnlinePlatform(channelUploadingParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_WAREHOUSE_ONLINEPLATFORMCODE);
                XxlJobHelper.handleFail(ERROR_WAREHOUSE_ONLINEPLATFORMCODE);
                return;
            }
            //开始推送门店
            if (onlinePlatform.getWarehouseId() != null) {
                ChannelRespDto resp = channelService.uploadingWarehouse(onlinePlatform.getId(), onlinePlatform.getWarehouseId());
                if (resp == null) {
                    XxlJobHelper.log(ERROR_WAREHOUSE_LIST);
                }
                else if (resp.getCode().equals("-1")) {
                    new Exception(resp.getMsg());
                }
                XxlJobHelper.log("请求成功：" + JSON.toJSONString(resp));
            } else {
                XxlJobHelper.log(ERROR_WAREHOUSE_LIST);
            }
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }
}
