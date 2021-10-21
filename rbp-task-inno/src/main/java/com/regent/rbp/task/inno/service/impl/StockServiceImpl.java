package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeClass;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.stock.StockDetail;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeClassDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.stock.StockQueryDao;
import com.regent.rbp.api.dto.stock.StockDataDto;
import com.regent.rbp.api.service.enums.StockTypeEnum;
import com.regent.rbp.api.service.stock.StockQueryService;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.dto.StockDto;
import com.regent.rbp.task.inno.model.resp.StockRespDto;
import com.regent.rbp.task.inno.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 库存 Impl
 * @author: HaiFeng
 * @create: 2021-10-21 10:09
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    StockQueryService stockQueryService;
    @Autowired
    StockQueryDao stockQueryDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    LongDao longDao;
    @Autowired
    SizeClassDao sizeClassDao;
    @Autowired
    ChannelDao channelDao;

    @Override
    public StockRespDto stockQuery(StockDto dto) {
        StockRespDto resp = null;

        List<Long> goodsIds = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();
        List<Long> channelIds = new ArrayList<>();

        List<String> errorMsgList = this.validationIsNotEmpty(dto);

        if (dto.getSku() != null && dto.getSku().size() > 0) {
            barcodeList = dto.getSku().stream().distinct().collect(Collectors.toList());
        }
        else if (dto.getGoodsNo() != null && dto.getGoodsNo().size() > 0) {
            List<String> goodsNos = dto.getGoodsNo().stream().distinct().collect(Collectors.toList());
            goodsIds = goodsDao.selectList(new QueryWrapper<Goods>().in("code", goodsNos)).stream().map(Goods::getId).collect(Collectors.toList());
            if (goodsIds == null || goodsIds.size() == 0) {
                errorMsgList.add("货号不存在");
            }
        }

        List<String> channelList = dto.getUnitNo().stream().distinct().collect(Collectors.toList());
        List<Channel> channels =channelDao.selectList(new QueryWrapper<Channel>().in("code", channelList));
        if (channels == null || channels.size() == 0) {
            errorMsgList.add("仓库编号或渠道编号不存在");
        } else {
            channelIds = channels.stream().map(Channel::getId).collect(Collectors.toList());
        }

        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            return StockRespDto.errorParameter(message);
        }

        //获取对应库存表名
        String tableName = StockTypeEnum.getTableName(dto.getStockType());
        if (StrUtil.isNotEmpty(tableName)) {
            IPage<StockDataDto> stockDataIPage = stockQueryDao.searchStockList(
                    new Page<StockDataDto>(dto.getPageNo(), dto.getPageSize()), tableName, goodsIds, barcodeList, channelIds);

            resp = StockRespDto.success(Integer.parseInt(String.valueOf(stockDataIPage.getTotal())), this.convertQueryResult(stockDataIPage.getRecords()));
        }

        return resp;
    }

    private List<String> validationIsNotEmpty(StockDto dto) {
        List<String> errorMsgList = new ArrayList<>();
        if (dto.getType() == null) {
            errorMsgList.add("类型不能为空");
        }
        if (dto.getUnitNo() == null || dto.getUnitNo().size() == 0) {
            errorMsgList.add("仓库编号或渠道编号 不能为空");
        }
        if (dto.getStockType() == null) {
            dto.setStockType(1);
        }
        if (dto.getPageNo() == null) {
            dto.setPageNo(1);
        }
        if (dto.getPageSize() == null) {
            dto.setPageSize(100);
        }
        return errorMsgList;
    }

    private List<StockDataDto> convertQueryResult(List<StockDataDto> data) {

        List<Long> channelIds = data.stream().map(StockDataDto::getChannelId).distinct().collect(Collectors.toList());
        List<Channel> channelList = channelDao.selectBatchIds(channelIds);
        Map<Long, Channel> channelMap = channelList.stream().collect(Collectors.toMap(Channel::getId, t -> t));

        List<Long> goodsIds = data.stream().map(StockDataDto::getGoodsId).distinct().collect(Collectors.toList());
        List<Goods> goodsList = goodsDao.selectBatchIds(goodsIds);
        Map<Long, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, t -> t));

        List<Long> colorIds = data.stream().map(StockDataDto::getColorId).distinct().collect(Collectors.toList());
        List<Color> colorList = colorDao.selectBatchIds(colorIds);
        Map<Long, Color> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, t -> t));

        List<Long> longIds = data.stream().map(StockDataDto::getLongId).distinct().collect(Collectors.toList());
        List<LongInfo> longList = longDao.selectBatchIds(longIds);
        Map<Long, LongInfo> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, t -> t));

        List<Long> sizeIds = data.stream().map(StockDataDto::getSizeId).distinct().collect(Collectors.toList());
        List<SizeClass> sizeList = sizeClassDao.selectBatchIds(sizeIds);
        Map<Long, SizeClass> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeClass::getId, t -> t));

        for (StockDataDto stock : data) {
            if (channelMap.containsKey(stock.getChannelId())) {
                stock.setUnitNo(channelMap.get(stock.getChannelId()).getCode());
            }
            if (goodsMap.containsKey(stock.getGoodsId())) {
                stock.setGoodsNo(goodsMap.get(stock.getGoodsId()).getCode());
            }
            if (colorMap.containsKey(stock.getColorId())) {
                stock.setColorNo(colorMap.get(stock.getColorId()).getCode());
            }
            if (longMap.containsKey(stock.getLongId())) {
                stock.setLongNo(longMap.get(stock.getLongId()).getName());
            }
            if (sizeMap.containsKey(stock.getSizeId())) {
                stock.setSize(sizeMap.get(stock.getSizeId()).getName());
            }
        }
        return data;
    }
}
