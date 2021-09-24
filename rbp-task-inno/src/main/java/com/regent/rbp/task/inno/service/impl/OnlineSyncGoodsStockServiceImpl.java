package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.omiChannel.OnlineSyncGoodsStock;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.onlinePlatform.OnlineSyncGoodsStockDao;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.stock.StockQueryParam;
import com.regent.rbp.api.dto.stock.StockQueryResult;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.stock.StockQueryService;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.OnlineSyncGoodsStockDto;
import com.regent.rbp.task.inno.model.req.OnlineSyncGoodsStockReqDto;
import com.regent.rbp.task.inno.model.resp.OnlineSyncGoodsStockRespDto;
import com.regent.rbp.task.inno.service.OnlineSyncGoodsStockService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/23
 * @description
 */
@Service
public class OnlineSyncGoodsStockServiceImpl extends ServiceImpl<OnlineSyncGoodsStockDao, OnlineSyncGoodsStock> implements OnlineSyncGoodsStockService {

    private static final String POST_WAREHOUSE_INVERTORY = "api/Inventory/Post_Warehouse_Invertory";

    @Autowired
    private InnoConfig innoConfig;
    @Autowired
    private OnlineSyncGoodsStockDao onlineSyncGoodsStockDao;
    @Autowired
    private StockQueryService stockQueryService;

    /**
     * 全量更新上传inno仓库库存
     */
    @Override
    public void fullSyncGoodsStockEvent(OnlinePlatform onlinePlatform) {
        try {
            OnlineSyncGoodsStockReqDto stockReqDto = new OnlineSyncGoodsStockReqDto();
            stockReqDto.setApp_key(innoConfig.getAppkey());
            stockReqDto.setApp_secrept(innoConfig.getAppsecret());
            List<OnlineSyncGoodsStockDto> goodsStockDtoList = new ArrayList<>();
            stockReqDto.setData(goodsStockDtoList);
            // 成功数量
            Long successTotal = 0L;
            // 云仓编号
            int pageNo = 1;
            // 查询实际库存
            StockQueryParam queryParam = new StockQueryParam();
            queryParam.setPageSize(SystemConstants.PAGE_SIZE);
            queryParam.setStockType(1);
            queryParam.setMerge(1);
            queryParam.setWarehouseCodeList(new String[]{onlinePlatform.getWarehouseCode()});
            while (true) {
                queryParam.setPageNo(pageNo);
                PageDataResponse<StockQueryResult> stockPages = stockQueryService.query(queryParam);
                if (CollUtil.isNotEmpty(stockPages.getData())) {
                    // 转换
                    for (StockQueryResult stock : stockPages.getData()) {
                        if (StringUtil.isEmpty(stock.getBarcode())) {
                            continue;
                        }
                        goodsStockDtoList.add(OnlineSyncGoodsStockDto.build(stock.getBarcode(), stock.getQuantity().intValue(), onlinePlatform.getChannelCode()));
                        // 批量上传实际库存
                        if (goodsStockDtoList.size() >= SystemConstants.BATCH_SIZE) {
                            successTotal += this.batchUploadStock(stockReqDto);
                            // 清除数据
                            goodsStockDtoList.clear();
                        }
                    }
                }
                // 判断几时跳出循环
                if (CollUtil.isEmpty(stockPages.getData())) {
                    // 批量上传剩余数据
                    if (CollUtil.isNotEmpty(goodsStockDtoList)) {
                        successTotal += this.batchUploadStock(stockReqDto);
                        // 清除数据
                        goodsStockDtoList.clear();
                    }
                    break;
                }
                pageNo++;
            }
            XxlJobHelper.handleSuccess(String.format("[上传ERP仓库库存] 成功同步 %s 条库存", successTotal));
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 更新上传inno仓库库存
     */
    @Override
    public void syncGoodsStockEvent(OnlinePlatform onlinePlatform) {
        try {
            OnlineSyncGoodsStockReqDto stockReqDto = new OnlineSyncGoodsStockReqDto();
            stockReqDto.setApp_key(innoConfig.getAppkey());
            stockReqDto.setApp_secrept(innoConfig.getAppsecret());
            List<OnlineSyncGoodsStockDto> goodsStockDtoList = new ArrayList<>();
            stockReqDto.setData(goodsStockDtoList);
            // 成功数量
            Long successTotal = 0L;
            // 云仓编号
            int pageNo = 1;
            // 查询实际库存
            StockQueryParam queryParam = new StockQueryParam();
            queryParam.setPageNo(1);
            queryParam.setPageSize(SystemConstants.PAGE_SIZE);
            queryParam.setStockType(1);
            queryParam.setMerge(1);
            queryParam.setWarehouseCodeList(new String[]{onlinePlatform.getWarehouseCode()});
            while (true) {
                // 获取同步库存数据
                Page<OnlineSyncGoodsStock> goodsPages = onlineSyncGoodsStockDao.selectPage(new Page<>(pageNo, SystemConstants.PAGE_SIZE), new QueryWrapper<OnlineSyncGoodsStock>().orderByAsc("updated_time"));
                if (CollUtil.isNotEmpty(goodsPages.getRecords())) {
                    List<OnlineSyncGoodsStock> stockList = goodsPages.getRecords();
                    // 获取条形码
                    queryParam.setBarcodeList(stockList.stream().map(OnlineSyncGoodsStock::getBarcode).toArray(String[]::new));
                    // 库存查询
                    PageDataResponse<StockQueryResult> stockPages = stockQueryService.query(queryParam);
                    if (CollUtil.isNotEmpty(stockPages.getData())) {
                        Map<String, OnlineSyncGoodsStock> stockMap = stockList.stream().collect(Collectors.toMap(OnlineSyncGoodsStock::getBarcode, Function.identity()));
                        // 转换
                        for (StockQueryResult stock : stockPages.getData()) {
                            OnlineSyncGoodsStock goodsStock = stockMap.get(stock.getBarcode());
                            // 不存在或者数量相同则跳过
                            if (null == goodsStock || stock.getQuantity().equals(goodsStock.getQuantity())) {
                                continue;
                            }
                            goodsStockDtoList.add(OnlineSyncGoodsStockDto.build(stock.getBarcode(), stock.getQuantity().intValue(), onlinePlatform.getChannelCode()));
                            // 批量上传实际库存
                            if (goodsStockDtoList.size() >= SystemConstants.BATCH_SIZE) {
                                successTotal += this.batchUploadStock(stockReqDto);
                                // 清除数据
                                goodsStockDtoList.clear();
                            }
                        }
                    }
                }
                // 判断几时跳出循环
                if (pageNo >= goodsPages.getPages()) {
                    // 批量上传剩余数据
                    if (CollUtil.isNotEmpty(goodsStockDtoList)) {
                        successTotal += this.batchUploadStock(stockReqDto);
                        // 清除数据
                        goodsStockDtoList.clear();
                    }
                    break;
                }
                pageNo++;
            }
            XxlJobHelper.handleSuccess(String.format("[上传ERP仓库库存] 成功同步 %s 条库存", successTotal));
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 批量上传仓库库存
     */
    private Long batchUploadStock(OnlineSyncGoodsStockReqDto stockReqDto) {
        String api_url = String.format("%s%s", innoConfig.getUrl(), POST_WAREHOUSE_INVERTORY);
        String result = HttpUtil.post(api_url, JSON.toJSONString(stockReqDto));
        // 成功数量
        Long successTotal = 0L;
        // 转换
        OnlineSyncGoodsStockRespDto responseDto = JSON.parseObject(result, OnlineSyncGoodsStockRespDto.class);
        if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
            XxlJobHelper.log(responseDto.getMsg());
        }
        // 记录错误日志
        if (CollUtil.isNotEmpty(responseDto.getData())) {
            successTotal = responseDto.getData().stream().filter(f -> SystemConstants.SUCCESS_CODE.equals(f.getCode())).count();
            responseDto.getData().stream().filter(f -> !SystemConstants.SUCCESS_CODE.equals(f.getCode())).forEach(log -> XxlJobHelper.log(JSON.toJSONString(log)));
        }

        return successTotal;
    }
}
