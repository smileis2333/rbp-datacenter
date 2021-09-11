package com.regent.rbp.api.service.bean.stock;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeClass;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.stock.StockDetail;
import com.regent.rbp.api.core.warehouse.Warehouse;
import com.regent.rbp.api.core.warehouse.WarehouseChannelRange;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.stock.StockQueryDao;
import com.regent.rbp.api.dao.warehouse.WarehouseChannelRangeDao;
import com.regent.rbp.api.dao.warehouse.WarehouseDao;
import com.regent.rbp.api.dto.base.BarcodeDto;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.goods.DisableSizeDto;
import com.regent.rbp.api.dto.goods.GoodsColorDto;
import com.regent.rbp.api.dto.goods.GoodsLongDto;
import com.regent.rbp.api.dto.goods.GoodsPriceDto;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsTagPriceDto;
import com.regent.rbp.api.dto.stock.StockQueryParam;
import com.regent.rbp.api.dto.stock.StockQueryResult;
import com.regent.rbp.api.service.enums.StockTypeEnum;
import com.regent.rbp.api.service.goods.context.GoodsQueryContext;
import com.regent.rbp.api.service.stock.StockQueryService;
import com.regent.rbp.api.service.stock.context.StockQueryContext;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.MD5Util;
import com.regent.rbp.infrastructure.util.NumberUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2021-09-11
 * @Description
 */
public class StockQueryServiceBean implements StockQueryService {

    @Autowired
    private StockQueryDao stockQueryDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private ColorDao colorDao;

    @Autowired
    private LongDao longDao;

    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Autowired
    private BarcodeDao barcodeDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private WarehouseChannelRangeDao warehouseChannelRangeDao;

    @Override
    public PageDataResponse<StockQueryResult> query(StockQueryParam param) {
        //校验
        String errorMsg = this.validateStockQueryParam(param);
        if (StrUtil.isNotEmpty(errorMsg)) {
            return new PageDataResponse(ResponseCode.PARAMS_ERROR, errorMsg);
        }
        //查询数据
        PageDataResponse<StockQueryResult> response = this.searchStockPage(param);
        return response;
    }

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    private PageDataResponse<StockQueryResult> searchStockPage(StockQueryParam param) {
        PageDataResponse<StockQueryResult> response = new PageDataResponse<>(0L, null);
        if (null == param.getStockType()) {
            //默认查询库存
            param.setStockType(StockTypeEnum.STOCK.getType());
        }
        if (null == param.getMerge()) {
            //默认不合并
            int merge = 0;
            param.setMerge(merge);
        }
        //获取对应库存表名
        String tableName = StockTypeEnum.getTableName(param.getStockType());
        if (StrUtil.isNotEmpty(tableName)) {
            IPage<StockDetail> stockDetailIPage = stockQueryDao.searchPageData(
                    new Page<StockDetail>(param.getPageNo(), param.getPageSize()), tableName, param);
            //查询云仓数据
            List<Warehouse> warehouseList = new ArrayList<>();
            if (ArrayUtil.isNotEmpty(param.getWarehouseCodeList())) {
                warehouseList = warehouseDao.selectList(new LambdaQueryWrapper<Warehouse>()
                        .in(Warehouse::getCode, param.getWarehouseCodeList()));
            }

            response.setTotalCount(stockDetailIPage.getTotal());
            response.setData(this.convertStockQueryResult(stockDetailIPage.getRecords(), warehouseList));
        }
        return response;
    }

    /**
     * 整理返回结果
     *
     * @param stockDetailList
     * @param warehouseList
     * @return
     */
    private List<StockQueryResult> convertStockQueryResult(List<StockDetail> stockDetailList, List<Warehouse> warehouseList) {
        if (CollUtil.isEmpty(stockDetailList)) {
            return new ArrayList<>();
        }
        List<StockQueryResult> queryResults = new ArrayList<>(stockDetailList.size());

        List<Long> goodsIds = StreamUtil.toNoNullDistinctList(stockDetailList, StockDetail::getGoodsId);
        //货品
        List<Goods> goodsList = goodsDao.selectList(new QueryWrapper<Goods>().select("id", "code") .in("id", goodsIds));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode, (v1, v2) -> v1));
        //颜色
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>()
                .in(Color::getId, StreamUtil.toNoNullDistinctList(stockDetailList, StockDetail::getColorId)));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getCode, (v1, v2) -> v1));
        //内长
        List<LongInfo> longInfoList = longDao.selectList(new LambdaQueryWrapper<LongInfo>()
                .in(LongInfo::getId, StreamUtil.toNoNullDistinctList(stockDetailList, StockDetail::getLongId)));
        Map<Long, String> longInfoMap = longInfoList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName, (v1, v2) -> v1));
        //尺码
        List<SizeDetail> sizeDetailList = sizeDetailDao.selectList(new LambdaQueryWrapper<SizeDetail>()
                .in(SizeDetail::getId, StreamUtil.toNoNullDistinctList(stockDetailList, StockDetail::getSizeId)));
        Map<Long, String> sizeDetailMap = sizeDetailList.stream().collect(Collectors.toMap(SizeDetail::getId, SizeDetail::getName, (v1, v2) -> v1));
        //条形码
        List<Barcode> barcodeList = barcodeDao.selectList(new LambdaQueryWrapper<Barcode>() .in(Barcode::getGoodsId, goodsIds));
        Map<String, String> barcodeMap = barcodeList.stream().collect(Collectors.toMap(
                t -> MD5Util.shortenKeyString(t.getGoodsId(), t.getColorId(), t.getLongId(), t.getSizeId()), Barcode::getBarcode, (v1, v2) -> v1));

        List<Long> channelIdList = StreamUtil.toNoNullDistinctList(stockDetailList, StockDetail::getChannelId);
        Map<Long, String> channelMap = new HashMap<>();
        //云仓
        Map<Long, String> warehouseMap = new HashMap<>();
        Map<Long, List<WarehouseChannelRange>> warehouseChannelRangeMap = new HashMap<>();
        if (CollUtil.isNotEmpty(channelIdList)) {
            if (CollUtil.isNotEmpty(warehouseList)) {
                warehouseMap = warehouseList.stream().collect(Collectors.toMap(Warehouse::getId, Warehouse::getCode, (v1, v2) -> v1));
                List<WarehouseChannelRange> warehouseChannelRangeList = warehouseChannelRangeDao.selectList(new LambdaQueryWrapper<WarehouseChannelRange>()
                        .in(WarehouseChannelRange::getChannelId, channelIdList)
                        .in(WarehouseChannelRange::getWarehouseId, StreamUtil.toNoNullDistinctList(warehouseList, Warehouse::getId)));
                warehouseChannelRangeMap = warehouseChannelRangeList.stream().collect(Collectors.groupingBy(WarehouseChannelRange::getChannelId));
            }
        }

        for(StockDetail stockDetail : stockDetailList) {
            String skuKey = MD5Util.shortenKeyString(stockDetail.getGoodsId(), stockDetail.getColorId(), stockDetail.getLongId(), stockDetail.getLongId());
            List<WarehouseChannelRange> warehouseChannelRangeList = warehouseChannelRangeMap.get(stockDetail.getChannelId());
            if (CollUtil.isNotEmpty(warehouseChannelRangeList)) {
                //有云仓，计算比例
                for (WarehouseChannelRange warehouseChannelRange : warehouseChannelRangeList) {
                    StockQueryResult queryResult = new StockQueryResult();
                    //TODO 计量商品保留小数
                    queryResult.setQuantity(NumberUtil.mul(stockDetail.getQuantity(), warehouseChannelRange.getUseRate(), BigDecimal.ROUND_HALF_UP));
                    queryResult.setGoodsCode(goodsMap.get(stockDetail.getGoodsId()));
                    queryResult.setColorCode(colorMap.get(stockDetail.getColorId()));
                    queryResult.setLongName(longInfoMap.get(stockDetail.getLongId()));
                    queryResult.setSize(sizeDetailMap.get(stockDetail.getSizeId()));
                    queryResult.setBarcode(barcodeMap.get(skuKey));
                    queryResult.setWarehouseCode(warehouseMap.get(warehouseChannelRange.getWarehouseId()));

                    queryResults.add(queryResult);
                }
            } else {
                StockQueryResult queryResult = new StockQueryResult();
                queryResult.setQuantity(stockDetail.getQuantity());
                queryResult.setGoodsCode(goodsMap.get(stockDetail.getGoodsId()));
                queryResult.setColorCode(colorMap.get(stockDetail.getColorId()));
                queryResult.setLongName(longInfoMap.get(stockDetail.getLongId()));
                queryResult.setSize(sizeDetailMap.get(stockDetail.getSizeId()));
                queryResult.setBarcode(barcodeMap.get(skuKey));

                queryResults.add(queryResult);
            }
        }
        return queryResults;
    }


    /**
     * 校验查询参数
     *
     * @param param
     * @return
     */
    private String validateStockQueryParam(StockQueryParam param) {
        //TODO 多语言
        String errorMsg = null;
        if (ArrayUtil.isEmpty(param.getChannelCodeList()) && ArrayUtil.isEmpty(param.getWarehouseCodeList())) {
            errorMsg = "渠道编号和云仓编号参数不能同时为空";
        }
        if (null != param.getGoodsCodeList() && param.getGoodsCodeList().length > 50) {
            errorMsg = "货号参数不能超过50";
        }
        if (null != param.getBarcodeList() && param.getBarcodeList().length > 50) {
            errorMsg = "条形码参数不能超过50";
        }
        return errorMsg;
    }
}
