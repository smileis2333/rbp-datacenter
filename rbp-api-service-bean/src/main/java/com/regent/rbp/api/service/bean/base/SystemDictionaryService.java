package com.regent.rbp.api.service.bean.base;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.regent.rbp.api.core.base.Color;
import com.regent.rbp.api.core.base.LongInfo;
import com.regent.rbp.api.core.base.SizeDetail;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2022/02/14
 * @description
 */
@Service
@Validated
public class SystemDictionaryService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ColorDao colorDao;
    @Autowired
    private SizeDetailDao sizeDetailDao;
    @Autowired
    private LongDao longDao;
    @Autowired
    private BaseDbDao baseDbDao;

    public Optional<Color> getColorFromCode(@NotBlank String code) {
        List<Color> colors = colorDao.selectList(Wrappers.<Color>lambdaQuery().eq(Color::getCode, code));
        if (CollUtil.isNotEmpty(colors)) {
            return Optional.of(colors.get(0));
        }
        return Optional.ofNullable(null);
    }

    public List<Color> getColorFromCodes(@NotNull List<String> codes) {
        if (CollUtil.isEmpty(codes)) {
            return Collections.emptyList();
        }
        List<Color> colors = colorDao.selectList(Wrappers.<Color>lambdaQuery().in(Color::getCode, codes));
        return colors;
    }

    public <K, V> Map<K, V> getColorMapFromCodes(@NotNull List<String> codes, @NotNull Function<Color, K> keyMapper, @NotNull Function<Color, V> valueMapper) {
        return getColorFromCodes(codes).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public List<Color> getColorFromIds(@NotNull List<Long> colorIds) {
        if (CollUtil.isEmpty(colorIds)) {
            return Collections.emptyList();
        }
        return colorDao.selectBatchIds(colorIds);
    }

    public <K, V> Map<K, V> getColorMapFromIds(@NotNull List<Long> colorIds,@NotNull Function<Color, K> keyMapper, @NotNull Function<Color, V> valueMapper) {
        return getColorFromIds(colorIds).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public Optional<SizeDetail> getSizeDetailFromSizeNameAndGoodsCode(@NotBlank String sizeName, @NotBlank String goodsCode) {
        if (goodsCode == null) {
            return Optional.ofNullable(null);
        }
        List<Goods> goods = goodsDao.selectList(Wrappers.<Goods>lambdaQuery().eq(Goods::getCode, goodsCode));
        if (CollUtil.isNotEmpty(goods)) {
            List<SizeDetail> sizeDetails = sizeDetailDao.selectList(Wrappers.<SizeDetail>lambdaQuery().eq(SizeDetail::getName, sizeName).eq(SizeDetail::getSizeClassId, goods.get(0).getSizeClassId()));
            if (CollUtil.isNotEmpty(sizeDetails)) {
                return Optional.of(sizeDetails.get(0));
            }
        }
        return Optional.ofNullable(null);
    }

    // todo size must be equal
    public List<SizeDetail> getSizeDetailFromSizeNamesAndGoodsCodes(@NotNull List<String> sizeNames, @NotBlank List<String> goodsCodes) {
        if (CollUtil.isEmpty(sizeNames) | CollUtil.isEmpty(goodsCodes)) {
            return Collections.emptyList();
        }
        List<Goods> goodsList = goodsDao.selectList(Wrappers.<Goods>lambdaQuery().in(Goods::getCode, goodsCodes));
        return baseDbDao.getSizeNameList(CollUtil.map(goodsList, Goods::getId, true), sizeNames);
    }

    public List<SizeDetail>getSizeDetailFromIds(@NotNull List<Long>sizeIds){
        return sizeDetailDao.selectBatchIds(sizeIds);
    }

    public <K, V> Map<K, V> getSizeDetailMapFromIds(@NotNull List<Long>sizeIds,@NotNull Function<SizeDetail, K> keyMapper, @NotNull Function<SizeDetail, V> valueMapper){
        return getSizeDetailFromIds(sizeIds).stream().collect(Collectors.toMap(keyMapper,valueMapper));
    }

    public <P, K, V> Map<P, Map<K, V>> getSizeDetailPartitionMapFromSizeNamesAndGoodsCodes(@NotNull List<String> sizeNames, @NotNull List<String> goodsCodes, @NotNull Function<SizeDetail, P> partition, @NotNull Function<SizeDetail, K> keyMapper, @NotNull Function<SizeDetail, V> valueMapper) {
        return getSizeDetailFromSizeNamesAndGoodsCodes(sizeNames, goodsCodes).stream().collect(Collectors.groupingBy(partition, Collectors.collectingAndThen(Collectors.toMap(keyMapper, valueMapper), Collections::unmodifiableMap)));
    }

    public Optional<LongInfo> getLongFromName(@NotBlank String longName) {
        List<LongInfo> longInfos = longDao.selectList(Wrappers.<LongInfo>lambdaQuery().eq(LongInfo::getName, longName));
        if (CollUtil.isNotEmpty(longInfos)) {
            return Optional.of(longInfos.get(0));
        }
        return Optional.ofNullable(null);
    }

    public List<LongInfo> getLongFromNames(@NotNull List<String> longNames) {
        if (CollUtil.isEmpty(longNames)) {
            return Collections.emptyList();
        }
        List<LongInfo> longInfos = longDao.selectList(Wrappers.<LongInfo>lambdaQuery().in(LongInfo::getName, longNames));
        return longInfos;
    }

    public <K, V> Map<K, V> getLongMapFromNames(@NotNull List<String> longNames, @NotNull Function<LongInfo, K> keyMapper, @NotNull Function<LongInfo, V> valueMapper) {
        return getLongFromNames(longNames).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

    public List<LongInfo> getLongFromIds(@NotNull List<Long> longIds) {
        if (CollUtil.isEmpty(longIds)) {
            return Collections.emptyList();
        }
        List<LongInfo> longInfos = longDao.selectList(Wrappers.<LongInfo>lambdaQuery().in(LongInfo::getId, longIds));
        return longInfos;
    }

    public <K, V> Map<K, V> getLongMapFromIds(@NotNull List<Long> longIds, @NotNull Function<LongInfo, K> keyMapper, @NotNull Function<LongInfo, V> valueMapper) {
        return getLongFromIds(longIds).stream().collect(Collectors.toMap(keyMapper, valueMapper));
    }

}
