package com.regent.rbp.api.service.bean;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.netflix.discovery.converters.Auto;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.goods.GoodsQueryResult;
import com.regent.rbp.api.dto.goods.GoodsSaveParam;
import com.regent.rbp.api.dto.goods.GoodsSaveResult;
import com.regent.rbp.api.service.goods.GoodsService;
import com.regent.rbp.api.service.goods.context.GoodsSaveContext;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.h2.api.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 货品档案服务
 * @author xuxing
 */
public class GoodsServiceBean implements GoodsService {

    @Autowired
    GoodsDao goodsDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    SeriesDao seriesDao;

    @Autowired
    PatternDao patternDao;

    @Autowired
    StyleDao styleDao;

    @Autowired
    SaleClassDao saleClassDao;

    @Autowired
    YearDao yearDao;

    @Autowired
    SeasonDao seasonDao;

    @Autowired
    BandDao bandDao;

    @Autowired
    MaterialDao materialDao;

    @Autowired
    SexDao sexDao;

    @Autowired
    ExchangeCategoryDao exchangeCategoryDao;

    @Autowired
    DiscountCategoryDao discountCategoryDao;

    @Autowired
    SupplierDao supplierDao;

    @Autowired
    SizeClassDao sizeClassDao;

    @Autowired
    ModelClassDao modelClassDao;

    @Autowired
    BrandDao brandDao;

    @Autowired
    AgeRangeDao ageRangeDao;

    @Override
    public GoodsQueryResult query(GoodsQueryParam param) {
        return null;
    }

    @Override
    public GoodsSaveResult save(GoodsSaveParam param) {
        boolean createFlag = true;
        GoodsSaveContext context = new GoodsSaveContext(param);
        //判断是新增还是更新
        Goods item = goodsDao.selectOne(new QueryWrapper<Goods>().eq("code", param.getGoodsCode()));
        if(item != null) {
            context.getGoods().setId(item.getId());
            createFlag = false;
        }

        //验证货品数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if(errorMsgList.size() > 0 ) {
            //throw new BusinessException(ErrorC, "");
        }
        //自动补充不存在的数据字典
        processAutoCompleteDictionary(param, context);
        //写入货品表
        saveGoods(createFlag, context.getGoods());
        saveGoodsColor(createFlag);
        saveGoodsLong(createFlag);
        saveCustomizeData(createFlag);
        saveDisableSizeData(createFlag);
        saveGoodsTagPrice(createFlag);
        saveGoodsBarcode(createFlag);

        return null;
    }

    /**
     * 保存货品数据
     * @param createFlag
     * @param goods
     */
    private void saveGoods(boolean createFlag, Goods goods) {
        if(createFlag) {
            goodsDao.insert(goods);
        } else {
            goodsDao.updateById(goods);
        }
    }

    /**
     * 保存货品颜色
     * @param createFlag
     */
    private void saveGoodsColor(boolean createFlag) {

    }

    /**
     * 保存货品内长
     * @param createFlag
     */
    private void saveGoodsLong(boolean createFlag) {

    }

    /**
     * 保存货品自定义字段
     * @param createFlag
     */
    private void saveCustomizeData(boolean createFlag) {

    }

    /**
     * 保存停用尺码
     * @param createFlag
     */
    private void saveDisableSizeData(boolean createFlag) {

    }

    /**
     * 保存货品条形码
     * @param createFlag
     */
    private void saveGoodsBarcode(boolean createFlag) {

    }

    /**
     * 保存货品吊牌价
     * @param createFlag
     */
    private void saveGoodsTagPrice(boolean createFlag) {

    }

    /**
     * 验证货品属性
     * @param param
     */
    private List<String> verificationProperty(GoodsSaveParam param, GoodsSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        Goods goods = context.getGoods();

        if(StringUtils.isBlank(param.getGoodsCode())) {
            errorMsgList.add("货号(GoodsCode)不能为空");
        } else {
            Goods item = goodsDao.selectOne(new QueryWrapper<Goods>().eq("code", param.getGoodsCode()));
            if(item != null) {
                goods.setId(item.getId());
            }
        }

        //验证尺码列表
        if (StringUtils.isBlank(param.getSizeClassName())) {
            errorMsgList.add("尺码类别(SizeClassName)不能为空");
        } else {
            SizeClass sizeClass = sizeClassDao.selectOne(new QueryWrapper<SizeClass>().eq("name", param.getSizeClassName()));
            if(sizeClass == null) {
                //尺码类别不存在，给予提示
                errorMsgList.add("尺码类别(SizeClassName)不存在");
            } else {
                goods.setSizeClassId(sizeClass.getId());
            }
        }
        //验证号型
        if (StringUtils.isNotBlank(param.getModelClass())) {
            ModelClass modelClass = modelClassDao.selectOne(new QueryWrapper<ModelClass>().eq("name", param.getModelClass()));
            if(modelClass == null) {
                //号型不存在，给予提示
                errorMsgList.add("号型(ModelClass)不存在");
            } else {
                goods.setModelClassId(modelClass.getId());
            }
        }
        //验证品牌
        if (StringUtils.isNotBlank(param.getBrand())) {
            Brand brand = brandDao.selectOne(new QueryWrapper<Brand>().eq("name", param.getBrand()));
            if(brand == null) {
                //品牌不存在，给予提示
                errorMsgList.add("品牌(Brand)不存在");
            } else {
                goods.setSupplierId(brand.getId());
            }
        }

        //验证供应商
        if (StringUtils.isNotBlank(param.getSupplierCode())) {
            Supplier supplier = supplierDao.selectOne(new QueryWrapper<Supplier>().eq("code", param.getSupplierCode()));
            if(supplier == null) {
                //供应商不存在，给予提示
                errorMsgList.add("供应商(SupplierCode)不存在");
            } else {
                goods.setSupplierId(supplier.getId());
            }
        }

        //验证颜色列表

        //验证内长列表

        //验证条码列表

        //验证尺码停用列表



        return errorMsgList;
    }

    /**
     * 自动补充不存在的数据字典
     * 比如 param.getCategory() 类别不存在，自动生成数据字典。
     * @param param
     */
    private void processAutoCompleteDictionary(GoodsSaveParam param, GoodsSaveContext context) {
        Goods goods = context.getGoods();
        //类别
        if (StringUtils.isNotBlank(param.getCategory())) {
            Category category = categoryDao.selectOne(new QueryWrapper<Category>().eq("name", param.getCategory()));
            if(category == null) {
                category = Category.build(param.getCategory());
                categoryDao.insert(category);
            }
            goods.setCategoryId(category.getId());
        }
        //系列
        if (StringUtils.isNotBlank(param.getSeries())) {
            Series series = seriesDao.selectOne(new QueryWrapper<Series>().eq("name", param.getSeries()));
            if(series == null) {
                series = Series.build(param.getSeries());
                seriesDao.insert(series);
            }
            goods.setSeriesId(series.getId());
        }
        //款型
        if (StringUtils.isNotBlank(param.getPattern())) {
            Pattern pattern = patternDao.selectOne(new QueryWrapper<Pattern>().eq("name", param.getPattern()));
            if(pattern == null) {
                pattern = Pattern.build(param.getPattern());
                patternDao.insert(pattern);
            }
            goods.setPatternId(pattern.getId());
        }
        //风格
        if (StringUtils.isNotBlank(param.getStyle())) {
            Style style = styleDao.selectOne(new QueryWrapper<Style>().eq("name", param.getStyle()));
            if(style == null) {
                style = Style.build(param.getStyle());
                styleDao.insert(style);
            }
            goods.setStyleId(style.getId());
        }
        //销售分类
        if (StringUtils.isNotBlank(param.getSaleClass())) {
            SaleClass saleClass = saleClassDao.selectOne(new QueryWrapper<SaleClass>().eq("name", param.getSaleClass()));
            if(saleClass == null) {
                saleClass = SaleClass.build(param.getSaleClass());
                saleClassDao.insert(saleClass);
            }
            goods.setSaleClassId(saleClass.getId());
        }
        //年份
        if (StringUtils.isNotBlank(param.getYear())) {
            Year year = yearDao.selectOne(new QueryWrapper<Year>().eq("name", param.getYear()));
            if(year == null) {
                year = Year.build(param.getYear());
                yearDao.insert(year);
            }
            goods.setYearId(year.getId());
        }
        //季节
        if (StringUtils.isNotBlank(param.getSeason())) {
            Season season = seasonDao.selectOne(new QueryWrapper<Season>().eq("name", param.getSeason()));
            if(season == null) {
                season = Season.build(param.getSeason());
                seasonDao.insert(season);
            }
            goods.setSeasonId(season.getId());
        }
        //波段
        if (StringUtils.isNotBlank(param.getBand())) {
            Band band = bandDao.selectOne(new QueryWrapper<Band>().eq("name", param.getBand()));
            if(band == null) {
                band = Band.build(param.getSeason());
                bandDao.insert(band);
            }
            goods.setBandId(band.getId());
        }
        //物料
        if (StringUtils.isNotBlank(param.getMaterial())) {
            Material material = materialDao.selectOne(new QueryWrapper<Material>().eq("name", param.getMaterial()));
            if(material == null) {
                material = Material.build(param.getMaterial());
                materialDao.insert(material);
            }
            goods.setMaterialId(material.getId());
        }
        //性别
        if (StringUtils.isNotBlank(param.getSex())) {
            Sex sex = sexDao.selectOne(new QueryWrapper<Sex>().eq("name", param.getSex()));
            if(sex == null) {
                sex = Sex.build(param.getSex());
                sexDao.insert(sex);
            }
            goods.setSexId(sex.getId());
        }
        //换货类别
        if (StringUtils.isNotBlank(param.getExchangeCategory())) {
            ExchangeCategory exchangeCategory = exchangeCategoryDao.selectOne(new QueryWrapper<ExchangeCategory>().eq("name", param.getExchangeCategory()));
            if(exchangeCategory == null) {
                exchangeCategory = ExchangeCategory.build(param.getExchangeCategory());
                exchangeCategoryDao.insert(exchangeCategory);
            }
            goods.setExchangeCategoryId(exchangeCategory.getId());
        }
        //折扣类别
        if (StringUtils.isNotBlank(param.getDiscountCategory())) {
            DiscountCategory discountCategory = discountCategoryDao.selectOne(new QueryWrapper<DiscountCategory>().eq("name", param.getDiscountCategory()));
            if(discountCategory == null) {
                discountCategory = DiscountCategory.build(param.getDiscountCategory());
                discountCategoryDao.insert(discountCategory);
            }
            goods.setDiscountCategoryId(discountCategory.getId());
        }
        //年龄段
        if(param.getMinAge() != null && param.getMaxAge() != null) {
            AgeRange ageRange = ageRangeDao.selectOne(new QueryWrapper<AgeRange>()
                    .eq("min_age", param.getMinAge())
                            .eq("max_age", param.getMinAge()));
            if(ageRange == null) {
                ageRangeDao.insert(ageRange);
            }
            goods.setAgeRangeId(ageRange.getId());
        }
    }
}
