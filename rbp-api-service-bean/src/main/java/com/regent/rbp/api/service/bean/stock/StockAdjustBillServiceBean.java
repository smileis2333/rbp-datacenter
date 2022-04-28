package com.regent.rbp.api.service.bean.stock;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.stock.StockAdjustBill;
import com.regent.rbp.api.core.stock.StockAdjustBillGoods;
import com.regent.rbp.api.core.stock.StockAdjustBillSize;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.stock.StockAdjustBillDao;
import com.regent.rbp.api.dao.stock.StockAdjustBillGoodsDao;
import com.regent.rbp.api.dao.stock.StockAdjustBillSizeDao;
import com.regent.rbp.api.dto.base.BaseData;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.base.GoodsDetailData;
import com.regent.rbp.api.dto.base.GoodsDetailDataWithQuantity;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.stock.StockAdjustBillGoodsDetailData;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryParam;
import com.regent.rbp.api.dto.stock.StockAdjustBillQueryResult;
import com.regent.rbp.api.dto.stock.StockAdjustBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.bean.base.SystemDictionaryService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.stock.StockAdjustBillService;
import com.regent.rbp.api.service.stock.context.StockAdjustBillSaveContext;
import com.regent.rbp.common.model.stock.entity.StockDetail;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.common.service.stock.StockDetailService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Service
public class StockAdjustBillServiceBean implements StockAdjustBillService {

    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private StockDetailService stockDetailService;
    @Autowired
    private StockAdjustBillDao stockAdjustBillDao;
    @Autowired
    private StockAdjustBillSizeDao stockAdjustBillSizeDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private StockAdjustBillGoodsDao stockAdjustBillGoodsDao;
    @Autowired
    private SystemDictionaryService systemDictionaryService;

    @Override
    public PageDataResponse<StockAdjustBillQueryResult> query(StockAdjustBillQueryParam param) {
        Page<BaseData> pageModel = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<StockAdjustBillQueryResult> pages = stockAdjustBillDao.searchPageData(pageModel, param);
        PageDataResponse<StockAdjustBillQueryResult> ans = new PageDataResponse<>();
        List<StockAdjustBillQueryResult> data = pages.getRecords();
        if (CollUtil.isEmpty(data)) {
            return ans;
        }
        List<Long> billIds = CollUtil.map(data, StockAdjustBillQueryResult::getId, true);
        List<StockAdjustBillGoods> billGoods = stockAdjustBillGoodsDao.selectList(new LambdaQueryWrapper<StockAdjustBillGoods>().in(StockAdjustBillGoods::getBillId, billIds));
        Map<Long, List<StockAdjustBillGoods>> billGoodsListMap = billGoods.stream().collect(Collectors.groupingBy(StockAdjustBillGoods::getBillId));
        List<StockAdjustBillSize> billSizes = stockAdjustBillSizeDao.selectList(new LambdaQueryWrapper<StockAdjustBillSize>().in(StockAdjustBillSize::getBillId, billIds));
        Map<Long, List<StockAdjustBillSize>> billSizeListMap = billSizes.stream().collect(Collectors.groupingBy(StockAdjustBillSize::getBillId));
        Map<Long, String> longIdNameMap = systemDictionaryService.getLongMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, StockAdjustBillSize::getLongId, true)), LongInfo::getId, LongInfo::getName);
        Map<Long, String> colorIdCodeMap = systemDictionaryService.getColorMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, StockAdjustBillSize::getColorId, true)), Color::getId, Color::getCode);
        Map<Long, String> sizeIdNameMap = systemDictionaryService.getSizeDetailMapFromIds(CollUtil.distinct(CollUtil.map(billSizes, StockAdjustBillSize::getSizeId, true)), SizeDetail::getId, SizeDetail::getName);
        Map<Long, String> goodsIdCodeMap = goodsDao.selectBatchIds(CollUtil.distinct(CollUtil.map(billGoods, StockAdjustBillGoods::getGoodsId, true))).stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.STOCK_ADJUST_BILL, billIds);
        Map<Long, List<CustomizeDataDto>> goodsCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.STOCK_ADJUST_BILL_GOODS, CollUtil.map(billGoods, StockAdjustBillGoods::getId, true));
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(data, StockAdjustBillQueryResult::getModuleId, true));
        for (StockAdjustBillQueryResult bill : data) {
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(bill.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            bill.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(bill.getId())));
            // 货品列表
            List<StockAdjustBillGoodsDetailData> goodsQueryResultList = new ArrayList<>();
            bill.setGoodsDetailData(goodsQueryResultList);
            // 货品明细
            List<StockAdjustBillGoods> billGoodsList = billGoodsListMap.get(bill.getId());
            Map<Long, StockAdjustBillGoods> currentGoodsMap = billGoodsList.stream().collect(Collectors.toMap(StockAdjustBillGoods::getId, Function.identity()));
            // 尺码明细
            List<StockAdjustBillSize> billSizeList = billSizeListMap.get(bill.getId());
            for (StockAdjustBillSize size : billSizeList) {
                StockAdjustBillGoods bgs = currentGoodsMap.get(size.getBillGoodsId());
                StockAdjustBillGoodsDetailData detailData = new StockAdjustBillGoodsDetailData();
                // 货品自定义字段，格式化单选类型字段
                detailData.setGoodsCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, goodsCustomMap.get(bgs.getId())));
                goodsQueryResultList.add(detailData);
                detailData.setRemark(bgs.getRemark());
                detailData.setQuantity(size.getQuantity());
                detailData.setLongName(longIdNameMap.get(size.getLongId()));
                detailData.setColorCode(colorIdCodeMap.get(size.getColorId()));
                detailData.setSize(sizeIdNameMap.get(size.getSizeId()));
                detailData.setGoodsCode(goodsIdCodeMap.get(size.getGoodsId()));
            }

        }
        ans.setData(data);
        ans.setTotalCount(pages.getTotal());
        return ans;
    }

    @Override
    @Transactional
    public DataResponse save(StockAdjustBillSaveParam param) {
        StockAdjustBillSaveContext context = new StockAdjustBillSaveContext();
        // 校验并转换
        List<String> messages = new ArrayList<>();
        convertSaveContext(context, param, messages);
        if (CollUtil.isNotEmpty(messages)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, messages.stream().collect(Collectors.joining(",")));
        }
        StockAdjustBill bill = context.getBill();
        stockAdjustBillDao.insert(bill);
        if (CollUtil.isNotEmpty(context.getBillGoods())) {
            context.getBillGoods().forEach(stockAdjustBillGoodsDao::insert);
        }
        if (CollUtil.isNotEmpty(context.getBillSizes())) {
            context.getBillSizes().forEach(stockAdjustBillSizeDao::insert);
        }
        baseDbService.saveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.STOCK_ADJUST_BILL, context.getBill().getId(), context.getBillCustomizeData());
        baseDbService.batchSaveOrUpdateCustomFieldData(param.getModuleId(), TableConstants.STOCK_ADJUST_BILL_GOODS, context.getGoodsCustomizeData());
        if (bill.getStatus() == CheckEnum.CHECK.getStatus()) {
            checkModifyStock(bill.getId());
        }
        return DataResponse.success();
    }

    private void convertSaveContext(StockAdjustBillSaveContext context, StockAdjustBillSaveParam param, List<String> messageList) {
        StockAdjustBill bill = new StockAdjustBill();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.preInsert();
        // 初始化状态
        bill.setModuleId(param.getModuleId());
        bill.setStatus(param.getStatus());
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setNotes(param.getNotes());
        bill.setBillNo(systemCommonService.getBillNo(param.getModuleId()));
        bill.setProcessStatus(0);
        bill.setBusinessTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_business_type where name = '%s'", param.getBusinessType())));
        bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
        bill.setCheckTime(param.getStatus() == CheckEnum.CHECK.getStatus() ? new Date() : null);
        context.setBill(bill);
        extractedGoodsAndSizes(context, param, messageList);
        context.setBillCustomizeData(param.getCustomizeData());
    }

    private void extractedGoodsAndSizes(StockAdjustBillSaveContext context, StockAdjustBillSaveParam param, List<String> messageList) {
        List<StockAdjustBillGoodsDetailData> goodsDetailData = param.getGoodsDetailData();
        List<String> barcodes = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getBarcode()) ? e.getBarcode() : null, true);
        List<String> goodsCode = CollUtil.map(goodsDetailData, e -> StrUtil.isNotBlank(e.getGoodsCode()) ? e.getGoodsCode() : null, true);
        List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsCode));
        Integer type = goodsList.stream().filter(e -> e.getCode().equals(param.getGoodsDetailData().get(0).getGoodsCode())).findFirst().get().getType();
        List<Long> goodsIdsList = CollUtil.map(goodsList, Goods::getId, true);
        List<String> colorCodes = type == 2 ? Collections.emptyList() : CollUtil.distinct(CollUtil.map(goodsDetailData, GoodsDetailData::getColorCode, true));
        List<String> longNames = type == 2 ? Collections.emptyList() : CollUtil.distinct(CollUtil.map(goodsDetailData, GoodsDetailData::getLongName, true));
        List<String> sizeNames = type == 2 ? Collections.emptyList() : CollUtil.map(goodsDetailData, GoodsDetailData::getSize, true); // don't distinct, must align

        Map<String, Barcode> barcodeMap = barcodes.isEmpty() ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        Map<String, Long> goodsMap = goodsIdsList.isEmpty() ? Collections.emptyMap() : goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
        Map<String, Long> colorMap = colorCodes.isEmpty() ? Collections.emptyMap() : colorDao.selectList(new QueryWrapper<Color>().in("code", colorCodes)).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
        Map<String, Long> longMap = longNames.isEmpty() ? Collections.emptyMap() : longDao.selectList(new QueryWrapper<LongInfo>().in("name", longNames)).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
        Map<Long, Map<String, Long>> goodsSizeNameMap = type == 2 ? Collections.emptyMap() : baseDbDao.getGoodsIdSizeNameIdMap(goodsCode, sizeNames, goodsList.stream().collect(Collectors.toMap(Goods::getCode, Goods::getId)));

        List<GoodsDetailDataWithQuantity> wrappers = new ArrayList<>();
        goodsDetailData.forEach(e -> {
            GoodsDetailDataWithQuantity w = BeanUtil.copyProperties(wrappers, GoodsDetailDataWithQuantity.class);
            wrappers.add(w);
            w.setGoodsCustomizeData(e.getGoodsCustomizeData());
            w.setQuantity(e.getQuantity());
            if (StrUtil.isNotBlank(e.getBarcode())) {
                Barcode barcode = null;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    w.setGoodsId(barcode.getGoodsId());
                    w.setColorId(barcode.getColorId());
                    w.setLongId(barcode.getLongId());
                    w.setSizeId(barcode.getSizeId());
                    w.setBarcodeId(barcode.getId());
                }
            } else {
                w.setGoodsId(goodsMap.get(e.getGoodsCode()));
                w.setColorId(colorMap.getOrDefault(e.getColorCode(), 1200000000000002L));
                w.setLongId(longMap.getOrDefault(e.getLongName(), 1200000000000003L));
                w.setSizeId(goodsSizeNameMap.getOrDefault(w.getGoodsId(), Collections.emptyMap()).getOrDefault(e.getSize(), 1200000000000005L));
            }
        });
        wrappers.forEach(w -> {
            StockAdjustBillGoods billGoods = BeanUtil.copyProperties(w, StockAdjustBillGoods.class);
            billGoods.setQuantity(w.getQuantity());
            context.addBillGoods(billGoods);
            context.addGoodsDetailCustomData(w.getGoodsCustomizeData(), billGoods.getId());
            StockAdjustBillSize billSize = BeanUtil.copyProperties(w, StockAdjustBillSize.class);
            billSize.setBillGoodsId(billGoods.getId());
            context.addBillSize(billSize);
        });

    }

    private void checkModifyStock(Long id) {
        StockAdjustBill stockAdjustBill = stockAdjustBillDao.selectById(id);
        List<StockAdjustBillSize> stockAdjustBillSizes = stockAdjustBillSizeDao.selectList(Wrappers.lambdaQuery(StockAdjustBillSize.class).eq(StockAdjustBillSize::getBillId, id));
        boolean isMustPositive = !systemCommonService.isAllowNegativeInventory(stockAdjustBill.getChannelId());
        //修改渠道库存，更新可用库存
        Set<StockDetail> stockDetails = new HashSet<>();
        for (StockAdjustBillSize stockAdjustBillSize : stockAdjustBillSizes) {
            StockDetail stockDetail = new StockDetail();
            BeanUtils.copyProperties(stockAdjustBillSize, stockDetail);
            stockDetail.setChannelId(stockAdjustBill.getChannelId());
            stockDetail.setQuantity(BigDecimal.ZERO);
            stockDetail.setReduceQuantity(stockAdjustBillSize.getQuantity());
            stockDetails.add(stockDetail);
        }
        stockDetailService.insertStockAndUpdateUsableStock(stockDetails, isMustPositive);
    }

}
