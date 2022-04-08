package com.regent.rbp.task.yumei.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.service.channel.ChannelService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.model.stock.entity.UsableStockDetail;
import com.regent.rbp.common.service.stock.UsableStockDetailService;
import com.regent.rbp.common.utils.StockUtils;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.wandian.sdk.Client;
import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.StockAPI;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto.StockSearchRequest;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto.StockSearchResponse;
import com.regent.rbp.task.yumei.wandian.sdk.impl.ApiFactory;
import com.regent.rbp.task.yumei.wandian.sdk.impl.DefaultClient;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
public class StockJob {

    @Autowired
    private UsableStockDetailService usableStockDetailService;
    @Autowired
    private StockService stockService;
    @Autowired
    private Validator validator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChannelDao channelDao;

    /**
     * todo 由于旺店通的接口限制和接口的查询压力，可能需要进行任务分片
     * 从旺店通拉取可用库存
     * 调用频率：30分钟1次
     *
     * @return
     */
    @XxlJob("yumei.onlineDownloadGoodsStockJobHandler")
    public void downloadAvailableStockJobHandler() {
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

        Client client = DefaultClient.get(wdtParam.getSid(), wdtParam.getUrl(), wdtParam.getKey(),
                wdtParam.getSecret());
        StockAPI stockAPI = ApiFactory.get(client, StockAPI.class);

        StockSearchRequest request = new StockSearchRequest();
        String currentDay = LocalDate.now().toString();
        String tomorrowDay = LocalDate.now().plusDays(1).toString();
        request.setStartTime(currentDay);
        request.setEndTime(tomorrowDay);
        StockSearchResponse response = stockAPI.search(request, new Pager(1000, 0, true)); // 一页最多1000
        int total = response.getTotal();
        Set<String> failMsgs = new HashSet<>();
        processStockWritting(response, failMsgs);

        if (total > 1000) {
            for (int i = 1; i < (total / 1000) + 1; i++) {
                StockSearchRequest leftRequest = new StockSearchRequest();
                request.setStartTime(currentDay);
                request.setEndTime(tomorrowDay);
                StockSearchResponse leftResponse = stockAPI.search(leftRequest, new Pager(1000, i, false));
                processStockWritting(leftResponse, failMsgs);
            }
        }

        if (CollUtil.isNotEmpty(failMsgs)) {
            XxlJobHelper.handleFail(failMsgs.stream().collect(Collectors.joining(",")));
        }
    }

    private void processStockWritting(StockSearchResponse response, Set<String> failMsgs) {

        List<StockSearchResponse.StockSearchDto> stockSearchDtos = response.getStockSearchDtos();
        List<String> barcodes = CollUtil.distinct(CollUtil.map(stockSearchDtos, StockSearchResponse.StockSearchDto::getSpecNo, true));
        Map<String, Barcode> barcodeMap = barcodeDao.selectList(Wrappers.lambdaQuery(Barcode.class).in(Barcode::getBarcode, barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));

        // tolerate error
        stockSearchDtos.stream().collect(Collectors.groupingBy(StockSearchResponse.StockSearchDto::getWarehouseNo)).forEach((channelCode, stocks) -> {
            try {
                Channel channel = channelDao.selectOne(Wrappers.lambdaQuery(Channel.class).eq(Channel::getCode, channelCode));
                if (channel == null) {
                    failMsgs.add(String.format("渠道%s不存在，对应库存写入失败", channelCode));
                    return;
                }
                boolean isMustPositive = !channelService.isAllowNegativeInventory(channel.getId());
                Set<UsableStockDetail> usableStockDetails = new HashSet<>();
                for (StockSearchResponse.StockSearchDto stockSearchDto : stockSearchDtos) {
                    // 商家编码对应丽晶条形码
                    String barcode = stockSearchDto.getSpecNo();
                    Barcode goodsData = barcodeMap.get(barcode);
                    UsableStockDetail usableStockDetail = new UsableStockDetail();
                    usableStockDetail.setChannelId(channel.getId());
                    usableStockDetail.setGoodsId(goodsData.getGoodsId());
                    usableStockDetail.setColorId(goodsData.getColorId());
                    usableStockDetail.setLongId(goodsData.getLongId());
                    usableStockDetail.setSizeId(goodsData.getSizeId());
                    usableStockDetail.setQuantity(stockSearchDto.getAvailableSendStock());
                    usableStockDetail.setReduceQuantity(stockSearchDto.getAvailableSendStock()); // 兼容性代码
                    usableStockDetail.setSkuHashCode(StockUtils.calculateSkuHashCode(goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    usableStockDetail.setHashCode(StockUtils.calculateHashCode(channel.getId(), goodsData.getGoodsId(), goodsData.getColorId(), goodsData.getLongId(), goodsData.getSizeId()));
                    usableStockDetails.add(usableStockDetail);
                }
                stockService.overwriteUsableStockDetail(usableStockDetails);
            } catch (Exception ex) {
                failMsgs.add(ex.getMessage());
            }
        });
    }

}

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
