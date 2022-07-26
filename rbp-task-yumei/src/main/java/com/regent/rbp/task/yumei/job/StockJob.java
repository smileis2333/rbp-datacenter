package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.utils.StockUtils;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.wandian.sdk.Client;
import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.StockAPI;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto.StockSearchRequest;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto.UsableStockResponse;
import com.regent.rbp.task.yumei.wandian.sdk.impl.ApiFactory;
import com.regent.rbp.task.yumei.wandian.sdk.impl.DefaultClient;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
public class StockJob {

    @Autowired
    private StockService stockService;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private ChannelDao channelDao;

    @Value("${yumei.stock.originalChannels:}")
    private String originalChannelsStr;

    @Value("${yumei.stock.summaryChannel:}")
    private String summaryChannel;

    /**
     * 从旺店通拉取可用库存
     * 调用频率：30分钟1次
     *
     * @return
     */
    @XxlJob("yumei.onlineDownloadGoodsStockJobHandler")
    public void onlineDownloadGoodsStockJobHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        //读取参数
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log(param);

        WDTParam wdtParam = null;
        try {
            wdtParam = objectMapper.readValue(param, WDTParam.class);
        } catch (IOException e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
        Set<ConstraintViolation<WDTParam>> violations = validator.validate(wdtParam);
        // echo violations and set info to xxl job
        if (CollUtil.isNotEmpty(violations)) {
            String tip = violations.stream().map(e -> String.format("%s:%s", e.getPropertyPath(), e.getMessage())).collect(Collectors.joining("; "));
            log.error(tip);
            XxlJobHelper.handleFail(tip);
            return;
        }

        Set<String> originalChannels = Arrays.stream(originalChannelsStr.split(",")).map(String::trim).collect(Collectors.toSet());
        Long summaryChannelId = null;
        Channel channel;
        if ((channel = channelDao.selectOne(Wrappers.lambdaQuery(Channel.class).eq(Channel::getCode, summaryChannel))) != null) {
            summaryChannelId = channel.getId();
        }

        Client client = DefaultClient.get(wdtParam.getSid(), wdtParam.getUrl(), wdtParam.getKey(),
                wdtParam.getSecret());
        StockAPI stockAPI = ApiFactory.get(client, StockAPI.class);

        StockSearchRequest request = new StockSearchRequest();
        String currentDay = LocalDate.now().minusDays(29).toString();
        String tomorrowDay = LocalDate.now().plusDays(1).toString();
        request.setStartTime(currentDay);
        request.setEndTime(tomorrowDay);

        UsableStockResponse response = null;
        try {
            response = stockAPI.searchAvailable(request, new Pager(1000, 0, true)); // 一页最多1000
            XxlJobHelper.log(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            XxlJobHelper.handleFail("调用旺店通失败");
            e.printStackTrace();
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, e.getMessage());
        }

        int total = response.getTotalCount();
        List<String> failMsgs = new ArrayList<>();
        processStockWritting(response, failMsgs, originalChannels, summaryChannelId);

        if (total > 1000) {
            for (int i = 1; i < (total / 1000) + 1; i++) {
                StockSearchRequest leftRequest = new StockSearchRequest();
                leftRequest.setStartTime(currentDay);
                leftRequest.setEndTime(tomorrowDay);
                UsableStockResponse leftResponse = stockAPI.searchAvailable(leftRequest, new Pager(1000, i, false));
                processStockWritting(leftResponse, failMsgs, originalChannels, summaryChannelId);
            }
        }

        if (CollUtil.isNotEmpty(failMsgs)) {
            for (String failMsg : failMsgs) {
                XxlJobHelper.log(failMsg);
            }
            XxlJobHelper.handleFail();
        }
    }

    private void processStockWritting(UsableStockResponse response, List<String> failMsgs, Set<String> passChannels, Long summaryChannelId) {
        if (CollUtil.isEmpty(response.getStocks())) {
            return;
        }
        List<UsableStockResponse.UsableStock> stockSearchDtos = response.getStocks().stream().filter(e -> passChannels.contains(e.getWarehouseNo())).collect(Collectors.toList());
        List<String> barcodes = response.getStocks().stream().map(UsableStockResponse.UsableStock::getSpecNo).distinct().collect(Collectors.toList());
        Map<String, Barcode> barcodeMap = barcodeDao.selectList(Wrappers.lambdaQuery(Barcode.class).in(Barcode::getBarcode, barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));

        // tolerate error
        stockSearchDtos.stream().collect(Collectors.groupingBy(UsableStockResponse.UsableStock::getWarehouseNo)).forEach((channelCode, stocks) -> {
            try {

                Long channelId;
                if (summaryChannelId != null) {
                    channelId = summaryChannelId;
                } else {
                    Channel channel = channelDao.selectOne(Wrappers.lambdaQuery(Channel.class).eq(Channel::getCode, channelCode));
                    if (channel == null) {
                        failMsgs.add(String.format("渠道%s不存在，对应库存写入失败", channelCode));
                        return;
                    }
                    channelId = channel.getId();
                }

                Set<UsableStockDetail> usableStockDetails = new HashSet<>();
                Set<StockDetail> stockDetails = new HashSet<>();
                Set<String> errorBarcode = new HashSet<>();
                for (UsableStockResponse.UsableStock stockSearchDto : stocks) {
                    // 商家编码对应丽晶条形码
                    String barcode = stockSearchDto.getSpecNo();
                    Barcode goodsData = barcodeMap.get(barcode);
                    if (goodsData == null) {
                        errorBarcode.add(barcode);
                        continue;
                    }
                    UsableStockDetail usableStockDetail = new UsableStockDetail();
                    usableStockDetail.setChannelId(channelId);
                    usableStockDetail.setGoodsId(goodsData.getGoodsId());
                    usableStockDetail.setColorId(goodsData.getColorId());
                    usableStockDetail.setLongId(goodsData.getLongId());
                    usableStockDetail.setSizeId(goodsData.getSizeId());
                    usableStockDetail.setQuantity(stockSearchDto.getNum());
                    usableStockDetail.setReduceQuantity(stockSearchDto.getNum()); // 兼容性代码
                    usableStockDetail.setSkuHashCode(StockUtils.calculateSkuHashCode(goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    usableStockDetail.setHashCode(StockUtils.calculateHashCode(channelId, goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    usableStockDetails.add(usableStockDetail);

                    StockDetail stockDetail = new StockDetail();
                    stockDetail.setChannelId(channelId);
                    stockDetail.setGoodsId(goodsData.getGoodsId());
                    stockDetail.setColorId(goodsData.getColorId());
                    stockDetail.setLongId(goodsData.getLongId());
                    stockDetail.setSizeId(goodsData.getSizeId());
                    stockDetail.setQuantity(stockSearchDto.getNum());
                    stockDetail.setReduceQuantity(stockSearchDto.getNum()); // 兼容性代码
                    stockDetail.setSkuHashCode(StockUtils.calculateSkuHashCode(goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    stockDetail.setHashCode(StockUtils.calculateHashCode(channelId, goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    stockDetails.add(stockDetail);

                }

                stockService.settingStock(usableStockDetails, stockDetails);
                XxlJobHelper.log("库存录入成功");
                XxlJobHelper.log(String.format("渠道编号: %s", channelCode));
                for (UsableStockResponse.UsableStock e : stocks) {
                    XxlJobHelper.log(String.format("条形码:%s，库存:%s", e.getSpecNo(), e.getNum()));
                }

                if (CollUtil.isNotEmpty(errorBarcode)) {
                    String errors = errorBarcode.stream().collect(Collectors.joining(","));
                    throw new RuntimeException(String.format("\r\n条码对应失败: %s; ",errors));
                }

            } catch (Exception ex) {
                failMsgs.add(String.format("渠道编号: %s录入库存失败", channelCode));
                failMsgs.add(ex.getMessage());
            }
        });
    }

}

/**
 * 旺店通客户端构造参数
 * sid, key, secret可在旺店通面板上 -> 接口环境
 * 以测试环境为例，在
 * https://open.wangdian.cn/qjb/open/abut/apply_test下
 *
 * url区分调用前缀，主要是区分测试环境和正式环境
 *
 * eg.
 * 测试环境
 * {
 *     "sid": "wdtapi3",
 *     "key": "mrj3-test",
 *     "secret": "2602a0f71:133987450537485811ec9059884eb04c",
 *     "url": "http://47.92.239.46"
 * }
 */
@Data
class WDTParam {
    @NotBlank
    private String sid;
    @NotBlank
    private String key;
    @NotBlank
    private String secret;
    @NotBlank
    @URL
    private String url;
}
