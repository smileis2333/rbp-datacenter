package com.regent.rbp.task.inno.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.model.param.RetailReturnNoticeParam;
import com.regent.rbp.task.inno.service.RetailReturnNoticeService;
import com.regent.rbp.task.yumei.job.RetailReturnNoticePushOrderRefundJob;
import com.regent.rbp.task.yumei.model.YumeiRefund;
import com.regent.rbp.task.yumei.model.YumeiRefundItems;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知 Job
 * @author: HaiFeng
 * @create: 2021-10-13 18:29
 */
@Slf4j
@Component
public class RetailReturnNoticeJob {

    private static final String ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST = "电商平台编号不存在";

    @Autowired
    RetailReturnNoticeService retailReturnNoticeService;
    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private RetailReturnNoticePushOrderRefundJob retailReturnNoticePushOrderRefundJob;

    @Value("${yumei.tradeCreate:false}")
    private boolean tradeCreate;

    /**
     * 拉取全渠道退货通知单列表
     * 入参格式：{ "onlinePlatformCode": "INNO", "orderSn": "", "returnSn": "THD000532110141327194170111" }
     * 入参格式：{ "onlinePlatformCode": "INNO", "returnSn": "THD000532110141327194170111" }
     * onlinePlatformCode：平台编号
     * orderSn：订单号
     * returnSn：退货单号
     */
    @XxlJob(SystemConstants.GET_APP_RETURN_ORDER)
    public void downloadRetailReturnNoticeListJobHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            RetailReturnNoticeParam retailReturnNoticeParam = JSON.parseObject(param, RetailReturnNoticeParam.class);
            OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(retailReturnNoticeParam.getOnlinePlatformCode());

            if (onlinePlatform == null) {
                XxlJobHelper.log(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            retailReturnNoticeService.downloadRetailReturnNoticeList(retailReturnNoticeParam);
        } catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            ex.printStackTrace();
        } finally {
            if (tradeCreate) {
                this.pushYumeiOrderRefund();
            }
        }
    }

    private void pushYumeiOrderRefund() {
        try {
            retailReturnNoticePushOrderRefundJob.RetailReturnNoticePushOrderRefundHandler();
        } catch (Exception e) {
            XxlJobHelper.handleFail(String.format("全渠道退货通知单推送玉美订单退货退款接口失败，详情：%s", e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * 拉取退换货单的物流单号
     * 入参格式：{ "onlinePlatformCode": "INNO" }
     * 入参格式：{ "onlinePlatformCode": "INNO", "orderSn": "" }
     * 入参格式：{ "onlinePlatformCode": "INNO", "returnSn": "" }
     */
    @XxlJob(SystemConstants.POST_RETURN_ORDER_SHIPPING_NO)
    public void returnOrderShippingNo() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            RetailReturnNoticeParam retailReturnNoticeParam = JSON.parseObject(param, RetailReturnNoticeParam.class);
            OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(retailReturnNoticeParam.getOnlinePlatformCode());

            if (onlinePlatform == null) {
                XxlJobHelper.log(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                XxlJobHelper.handleFail(ERROR_ONLINE_PLATFORM_CODE_NOT_EXIST);
                return;
            }
            retailReturnNoticeService.returnOrderShippingNo(retailReturnNoticeParam);
        } catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            ex.printStackTrace();
        } finally {
            if (tradeCreate) {
                this.pushReturnOrderShippingNo();
            }
        }
    }

    // 推送玉美
    private void pushReturnOrderShippingNo() {
        Object orderSnList = ThreadLocalGroup.get("yumei_logistics_list");
        Set<String> orderSnList2 = (Set<String>) orderSnList;
        if (CollUtil.isNotEmpty(orderSnList2)) {
            for (String orderSn : orderSnList2) {
                YumeiRefund refund = retailReturnNoticeService.getYumeiRefund(orderSn);
                String errorMsg = saleOrderService.pushRefundLogistics(refund.getStoreNo(), "3", refund.getOutOrderNo(), refund.getData());
                if (StrUtil.isNotEmpty(errorMsg)) {
                    XxlJobHelper.log(errorMsg);
                    XxlJobHelper.handleFail(errorMsg);
                }
            }


        }
    }

}
