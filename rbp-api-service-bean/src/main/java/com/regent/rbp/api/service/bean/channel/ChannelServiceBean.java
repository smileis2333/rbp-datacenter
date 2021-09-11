package com.regent.rbp.api.service.bean.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.*;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.dao.base.BranchCompanyDao;
import com.regent.rbp.api.dao.base.BrandDao;
import com.regent.rbp.api.dao.base.SaleRangeDao;
import com.regent.rbp.api.dao.base.TagPriceTypeDao;
import com.regent.rbp.api.dao.channel.*;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dto.channel.ChannelQueryParam;
import com.regent.rbp.api.dto.channel.ChannelQueryResult;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.channel.ChannelService;
import com.regent.rbp.api.service.channel.context.ChannelQueryContext;
import com.regent.rbp.api.service.goods.context.GoodsQueryContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 渠道档案服务
 * @author: HaiFeng
 * @create: 2021-09-11 13:37
 */
public class ChannelServiceBean implements ChannelService {

    @Autowired
    BrandDao brandDao;
    @Autowired
    BranchCompanyDao branchCompanyDao;
    @Autowired
    ChannelGradeDao channelGradeDao;
    @Autowired
    ChannelBusinessFormatDao channelBusinessFormatDao;
    @Autowired
    ChannelBusinessNatureDao channelBusinessNatureDao;
    @Autowired
    ChannelBalanceTypeDao channelBalanceTypeDao;
    @Autowired
    TagPriceTypeDao tagPriceTypeDao;
    @Autowired
    SaleRangeDao saleRangeDao;
    @Autowired
    FundAccountDao fundAccountDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    ChannelBrandDao channelBrandDao;


    @Override
    public PageDataResponse<ChannelQueryResult> query(ChannelQueryParam param) {
        ChannelQueryContext context = new ChannelQueryContext();
        //将入参转换成查询的上下文对象
        convertChannelQueryContext(param, context);
        //查询数据
        PageDataResponse<ChannelQueryResult> response = searchPage(context);
        return response;
    }

    /**
     * 将查询参数转换成 查询的上下文
     * @param param
     * @param context
     */
    private void convertChannelQueryContext(ChannelQueryParam param, ChannelQueryContext context) {
        context.setPageNo(param.getPageNo());
        context.setPageSize(param.getPageSize());

        context.setChannelCode(param.getChannelCode());
        context.setChannelName(param.getChannelName());
        context.setChannelFullName(param.getChannelFullName());
        context.setChannelAddress(param.getChannelAddress());
        context.setLinkMan(param.getLinkMan());
        context.setLinkManMobile(param.getLinkManMobile());
        context.setStatus(param.getStatus());
        context.setFields(param.getFields());

        context.setPhysicalRegion(param.getPhysicalRegion());
        context.setChannelBarrio(param.getChannelBarrio());
        context.setChannelorganization(param.getChannelorganization());

        // 品牌
        if (param.getBrand() != null && param.getBrand().length > 0) {
            List<Brand> list = brandDao.selectList(new QueryWrapper<Brand>().in("name", param.getBrand()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setBrand(ids);
            }
        }
        // 分公司
        if (param.getBranchCompany() != null && param.getBranchCompany().length > 0) {
            List<BranchCompany> list = branchCompanyDao.selectList(new QueryWrapper<BranchCompany>().in("name", param.getBranchCompany()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setBranchCompany(ids);
            }
        }
        // 渠道等级
        if (param.getGrade() != null && param.getGrade().length > 0) {
            List<ChannelGrade> list = channelGradeDao.selectList(new QueryWrapper<ChannelGrade>().in("name", param.getGrade()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setGrade(ids);
            }
        }
        // 渠道业态
        if (param.getBusinessFormat() != null && param.getBusinessFormat().length > 0) {
            List<ChannelBusinessFormat> list = channelBusinessFormatDao.selectList(new QueryWrapper<ChannelBusinessFormat>().in("name", param.getBusinessFormat()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setBusinessFormat(ids);
            }
        }
        // 经营性质
        if (param.getBusinessNature() != null && param.getBusinessNature().length > 0) {
            List<ChannelBusinessNature> list = channelBusinessNatureDao.selectList(new QueryWrapper<ChannelBusinessNature>().in("name", param.getBusinessNature()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setBusinessNature(ids);
            }
        }
        // 结算方式
        if (param.getBalanceType() != null && param.getBalanceType().length > 0) {
            List<ChannelBalanceType> list = channelBalanceTypeDao.selectList(new QueryWrapper<ChannelBalanceType>().in("name", param.getBalanceType()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setBalanceType(ids);
            }
        }
        // 零售吊牌价类型
        if (param.getRetailTagPriceType() != null && param.getRetailTagPriceType().length > 0) {
            List<TagPriceType> list = tagPriceTypeDao.selectList(new QueryWrapper<TagPriceType>().in("name", param.getRetailTagPriceType()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setRetailTagPriceType(ids);
            }
        }
        // 零售吊牌价类型
        if (param.getSaleTagPriceType() != null && param.getSaleTagPriceType().length > 0) {
            List<TagPriceType> list = tagPriceTypeDao.selectList(new QueryWrapper<TagPriceType>().in("name", param.getSaleTagPriceType()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setSaleTagPriceType(ids);
            }
        }
        // 销售范围
        if (param.getSaleRange() != null && param.getSaleRange().length > 0) {
            List<SaleRange> list = saleRangeDao.selectList(new QueryWrapper<SaleRange>().in("name", param.getSaleRange()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setSaleRange(ids);
            }
        }
        // 资金号
        if (param.getFundAccount() != null && param.getFundAccount().length > 0) {
            List<FundAccount> list = fundAccountDao.selectList(new QueryWrapper<FundAccount>().in("name", param.getFundAccount()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setFundAccount(ids);
            }
        }

        if(StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if(StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        if(StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if(StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateEnd);
        }




    }

    /**
     * 查询渠道数据
     * @param context
     * @return
     */
    private PageDataResponse<ChannelQueryResult> searchPage(GoodsQueryContext context) {

        PageDataResponse<ChannelQueryResult> result = new PageDataResponse<ChannelQueryResult>();

        Page<Channel> pageModel = new Page<Channel>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = new QueryWrapper<Channel>();

        IPage<Channel> channelPageData = channelDao.selectPage(pageModel, queryWrapper);
        List<ChannelQueryResult> list = convertGoodsQueryResult(channelPageData.getRecords());

        result.setTotalCount(channelPageData.getTotal());
        result.setData(list);

        return result;
    }

    /**
     * 处理查询结果的属性
     * 1.读取相同的属性
     * 2.将内部编码id转换成名称name
     */
    private List<ChannelQueryResult> convertGoodsQueryResult(List<Channel> channelList) {
        List<ChannelQueryResult> queryResults = new ArrayList<>(channelList.size());
        List<Long> channelIds = channelList.stream().map(Channel::getId).collect(Collectors.toList());

        // 加载所有渠道的品牌列表
        List<Long> brandIds = channelBrandDao.selectList(new QueryWrapper<ChannelBrand>().in("channel_id", channelIds))
                .stream().map(ChannelBrand::getBrandId).collect(Collectors.toList());
        brandDao.selectBatchIds(brandIds);

        for (Channel channel : channelList) {
            ChannelQueryResult queryResult = new ChannelQueryResult();
            queryResult.setChannelId(channel.getId());
            queryResult.setChannelCode(channel.getCode());
            queryResult.setChannelName(channel.getName());
            queryResult.setChannelFullName(channel.getFullName());
            queryResult.setChannelBuildDate(channel.getBuildDate());
            queryResult.setChannelAddress(channel.getAddress());
            queryResult.getBrand()



        }

        return queryResults;
    }

}
