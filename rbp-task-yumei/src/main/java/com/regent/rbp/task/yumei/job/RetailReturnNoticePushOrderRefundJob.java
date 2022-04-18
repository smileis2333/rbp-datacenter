package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.model.YumeiOrderItems;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @createTime 2022-04-16
 * @Description 全渠道退货通知单推送订单退货退款接口
 */
@Slf4j
@Component
public class RetailReturnNoticePushOrderRefundJob {

    @Autowired
    private RetailReturnNoticeBillDao retailReturnNoticeBillDao;

    @Autowired
    private RetailReturnNoticeBillGoodsDao retailReturnNoticeBillGoodsDao;

    @Autowired
    private OnlinePlatformService onlinePlatformService;

    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private DbService dbService;

    /**
     * 全渠道退货通知单推送订单退货退款接口
     * 入参格式：{ "onlinePlatformCode": "INNO"}
     */
    @XxlJob(SystemConstants.RETAIL_RETURN_NOTICE_PUSH_ORDER_REFUND_JOB)
    public void RetailReturnNoticePushOrderRefundHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            Map<String, Object> map = JSON.parseObject(param, Map.class);
            // 参数验证
            if (null == map || "{}".equals(JSONUtil.toJsonStr(map)) || null == map.get("onlinePlatformCode")) {
                XxlJobHelper.handleFail("列表参数不能为空");
                return;
            }
            // 获取电商平台信息
            Long onlinePlatformById = onlinePlatformService.getOnlinePlatformById((String) map.get("onlinePlatformCode"));
            if (null == onlinePlatformById) {
                XxlJobHelper.handleFail("电商平台编号不存在");
                return;
            }
            // 查询所有未审核inno平台全渠道退货通知单
            List<RetailReturnNoticeBill> billList = retailReturnNoticeBillDao.selectList(new QueryWrapper<RetailReturnNoticeBill>().eq("status", StatusEnum.NONE.getStatus())
                    .exists(String.format("select 1 from rbp_retail_order_bill a where a.online_platform_id=%s and a.id=rbp_retail_return_notice_bill.retail_order_bill_id", onlinePlatformById)));
            if (CollUtil.isEmpty(billList)) {
                XxlJobHelper.log(String.format("拉取全渠道退货通知单数为0"));
                return;
            }
            XxlJobHelper.log(String.format("拉取全渠道退货通知单数量为：%s, 单号为[%s]", billList.size(), billList.stream().map(v -> v.getBillNo()).distinct().collect(Collectors.joining(StrUtil.COMMA))));
            // 查询货品明细
            List<RetailReturnNoticeBillGoods> billGoodsList = retailReturnNoticeBillGoodsDao.selectList(new QueryWrapper<RetailReturnNoticeBillGoods>().in("bill_id", StreamUtil.toSet(billList, v -> v.getId())));
            if (CollUtil.isEmpty(billGoodsList)) {
                XxlJobHelper.handleFail(String.format("拉取全渠道退货通知单货品数为0"));
                return;
            }
            Map<Long, List<RetailReturnNoticeBillGoods>> billGoodsMap = billGoodsList.stream().collect(Collectors.groupingBy(v -> v.getBillId()));
            // 渠道名称
            Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", StreamUtil.toSet(billList, v -> v.getSaleChannelId())), Channel.class, LanguageTableEnum.CHANNEL);
            // 循环调用玉美退货接口
            List<RetailReturnNoticeBill> updateList = new ArrayList<>();
            for (RetailReturnNoticeBill bill : billList) {
                List<RetailReturnNoticeBillGoods> goodsList = billGoodsMap.get(bill.getId());
                if (CollUtil.isEmpty(goodsList)) {
                    XxlJobHelper.handleFail(String.format("全渠道退货通知单:%s,货品数为0，跳过当前单据", bill.getBillNo()));
                    continue;
                }
                String channelCode = OptionalUtil.ofNullable(channelMap.get(bill.getSaleChannelId()), IdNameCodeDto::getCode);
                if (StringUtils.isEmpty(channelCode)) {
                    XxlJobHelper.handleFail(String.format("全渠道退货通知单:%s,渠道编号不存在，跳过当前单据", bill.getBillNo()));
                    continue;
                }
                List<YumeiOrderItems> goodsDataList = new ArrayList<>();
                goodsList.forEach(item -> {
                    YumeiOrderItems orderItems = new YumeiOrderItems();
                    goodsDataList.add(orderItems);

                    orderItems.setSkuCode(item.getBarcode());
                    orderItems.setOutRefundNo(bill.getOnlineReturnNoticeCode());
                    orderItems.setRefundAmount(item.getBalancePrice());
                    orderItems.setRefundType(1);
                    orderItems.setSkuQty(item.getQuantity());
                    orderItems.setRefundRemark(bill.getNotes());
                });
                try {
                    // 订单退货退款接口
                    Boolean success = saleOrderService.orderRefund(channelCode, 3, bill.getBillNo(), StrUtil.EMPTY, goodsDataList);
                    if (!success) {
                        XxlJobHelper.handleFail(String.format("全渠道退货通知单:%s,推送玉美退货退款接口失败", bill.getBillNo()));
                    } else {
                        updateList.add(bill);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    XxlJobHelper.handleFail(String.format("全渠道退货通知单:%s,推送玉美退货退款接口失败,错误信息：%s", bill.getBillNo(), e.getMessage()));
                }
            }
            if (CollUtil.isNotEmpty(updateList)) {
                // 更新订单状态为已审核
                dbService.update(String.format("update rbp_retail_return_notice_bill set check_by=%s,check_time=now(),status=1 where id in %s",
                        ThreadLocalGroup.getUserId(), AppendSqlUtil.getInSqlByLong(StreamUtil.toList(updateList, v -> v.getId()))));
                XxlJobHelper.log(String.format("更新全渠道退货通知单据状态为已审核，单号为[%s]", updateList.stream().map(v -> v.getBillNo()).distinct().collect(Collectors.joining(StrUtil.COMMA))));
            }

        } catch (Exception e) {
            String message = e.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
        }
    }
}
