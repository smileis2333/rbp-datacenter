package com.regent.rbp.api.service.bean.channel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.BranchCompany;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.core.base.SaleRange;
import com.regent.rbp.api.core.base.TagPriceType;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.channel.ChannelArea;
import com.regent.rbp.api.core.channel.ChannelBalanceType;
import com.regent.rbp.api.core.channel.ChannelBusinessFormat;
import com.regent.rbp.api.core.channel.ChannelBusinessNature;
import com.regent.rbp.api.core.channel.ChannelGrade;
import com.regent.rbp.api.core.channel.ChannelOrganization;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.dao.base.BranchCompanyDao;
import com.regent.rbp.api.dao.base.BrandDao;
import com.regent.rbp.api.dao.base.SaleRangeDao;
import com.regent.rbp.api.dao.base.TagPriceTypeDao;
import com.regent.rbp.api.dao.channel.ChannelAreaDao;
import com.regent.rbp.api.dao.channel.ChannelBalanceTypeDao;
import com.regent.rbp.api.dao.channel.ChannelBrandDao;
import com.regent.rbp.api.dao.channel.ChannelBusinessFormatDao;
import com.regent.rbp.api.dao.channel.ChannelBusinessNatureDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.channel.ChannelGradeDao;
import com.regent.rbp.api.dao.channel.ChannelOrganizationDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dto.channel.ChannelBarrio;
import com.regent.rbp.api.dto.channel.ChannelBrandDto;
import com.regent.rbp.api.dto.channel.ChannelQueryParam;
import com.regent.rbp.api.dto.channel.ChannelQueryResult;
import com.regent.rbp.api.dto.channel.Channelorganization;
import com.regent.rbp.api.dto.channel.PhysicalRegion;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.channel.ChannelService;
import com.regent.rbp.api.service.channel.context.ChannelQueryContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 渠道档案服务
 * @author: HaiFeng
 * @create: 2021-09-11 13:37
 */
@Service
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
    @Autowired
    ChannelAreaDao channelAreaDao;
    @Autowired
    ChannelOrganizationDao channelOrganizationDao;


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
    private PageDataResponse<ChannelQueryResult> searchPage(ChannelQueryContext context) {

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

        // 加载所有 渠道品牌
        List<ChannelBrandDto> channelBrandList = channelBrandDao.selectChannelBrandDtoByChannelIds(channelIds);
        Map<Long, List<ChannelBrandDto>> hashMapChannelBrand = channelBrandList.stream().collect(Collectors.groupingBy(ChannelBrandDto::getChannelId));

        // 加载所有 渠道分公司
        List<Long> branchCompanyIds = channelList.stream().map(Channel::getBranchCompanyId).distinct().collect(Collectors.toList());
        List<BranchCompany> branchCompanyList = branchCompanyDao.selectBatchIds(branchCompanyIds);
        Map<Long, String> mapBranchCompany = branchCompanyList.stream().collect(Collectors.toMap(BranchCompany::getId, BranchCompany::getName));
        
        // 加载所有 渠道等级
        List<Long> channelGradeIds = channelList.stream().map(Channel::getGradeId).distinct().collect(Collectors.toList());
        List<ChannelGrade> channelGradeList = channelGradeDao.selectBatchIds(channelGradeIds);
        Map<Long, String> mapChannelGrade = channelGradeList.stream().collect(Collectors.toMap(ChannelGrade::getId, ChannelGrade::getName));

        // 加载所有 渠道业态
        List<Long> businessFormatIds = channelList.stream().map(Channel::getBusinessFormatId).distinct().collect(Collectors.toList());
        List<ChannelBusinessFormat> businessFormatList = channelBusinessFormatDao.selectBatchIds(businessFormatIds);
        Map<Long, String> mapBusinessFormat = businessFormatList.stream().collect(Collectors.toMap(ChannelBusinessFormat::getId, ChannelBusinessFormat::getName));

        // 加载所有 渠道经营性质
        List<Long> businessNatureIds = channelList.stream().map(Channel::getBusinessNatureId).distinct().collect(Collectors.toList());
        List<ChannelBusinessNature> businessNatureList = channelBusinessNatureDao.selectBatchIds(businessNatureIds);
        Map<Long, String> mapBusinessNature = businessNatureList.stream().collect(Collectors.toMap(ChannelBusinessNature::getId, ChannelBusinessNature::getName));

        // 加载所有 结算方式
        List<Long> balanceTypeIds = channelList.stream().map(Channel::getBalanceTypeId).distinct().collect(Collectors.toList());
        List<ChannelBalanceType> balanceTypeList = channelBalanceTypeDao.selectBatchIds(balanceTypeIds);
        Map<Long, String> mapBalanceType = balanceTypeList.stream().collect(Collectors.toMap(ChannelBalanceType::getId, ChannelBalanceType::getName));

        // 加载所有 吊牌价类型
        List<Long> tagPriceTypeIds = channelList.stream().map(Channel::getRetailTagPriceTypeId).distinct().collect(Collectors.toList());
        tagPriceTypeIds.addAll(channelList.stream().map(Channel::getSaleTagPriceTypeId).distinct().collect(Collectors.toList()));
        List<TagPriceType> tagPriceTypeList = tagPriceTypeDao.selectBatchIds(tagPriceTypeIds.stream().distinct().collect(Collectors.toList()));
        Map<Long, String> mapTagPriceType = tagPriceTypeList.stream().collect(Collectors.toMap(TagPriceType::getId, TagPriceType::getName));

        // 加载所有 销售范围
        List<Long> saleRangeIds = channelList.stream().map(Channel::getSaleRangeId).distinct().collect(Collectors.toList());
        List<SaleRange> saleRangeList = saleRangeDao.selectBatchIds(saleRangeIds);
        Map<Long, String> mapSaleRange = saleRangeList.stream().collect(Collectors.toMap(SaleRange::getId, SaleRange::getName));

        // 加载所有 资金号
        List<Long> fundAccountIds = channelList.stream().map(Channel::getFundAccountId).distinct().collect(Collectors.toList());
        List<FundAccount> fundAccountList = fundAccountDao.selectBatchIds(fundAccountIds);
        Map<Long, String> mapFundAccount = fundAccountList.stream().collect(Collectors.toMap(FundAccount::getId, FundAccount::getName));

        // 加载所有 渠道区域
        List<ChannelArea> channelAreaList = channelAreaDao.selectList(new QueryWrapper<ChannelArea>().select("id", "name"));
        Map<Long, String> mapChannelArea = channelAreaList.stream().collect(Collectors.toMap(ChannelArea::getId, ChannelArea::getName));

        // 加载所有 渠道组织架构
        List<ChannelOrganization> organizationList = channelOrganizationDao.selectList(new QueryWrapper<ChannelOrganization>().select("id", "name"));
        Map<Long, String> mapOrganization = organizationList.stream().collect(Collectors.toMap(ChannelOrganization::getId, ChannelOrganization::getName));

        for (Channel channel : channelList) {
            ChannelQueryResult queryResult = new ChannelQueryResult();
            queryResult.setChannelId(channel.getId());
            queryResult.setChannelCode(channel.getCode());
            queryResult.setChannelName(channel.getName());
            queryResult.setChannelFullName(channel.getFullName());
            queryResult.setChannelBuildDate(channel.getBuildDate());
            queryResult.setChannelAddress(channel.getAddress());
            queryResult.setLinkMan(channel.getLinkMan());
            queryResult.setLinkManMobile(channel.getLinkManMobile());
            queryResult.setMinPrice(channel.getMinPrice());
            queryResult.setMaxPrice(channel.getMaxPrice());

            PhysicalRegion physicalRegion = new PhysicalRegion(channel.getNation(), channel.getRegion(), channel.getProvince(), channel.getCity(), channel.getCounty());
            queryResult.setPhysicalRegion(physicalRegion);

            // 行政区域
            ChannelBarrio channelBarrio = new ChannelBarrio();
            if (channel.getBarrio1() != null && mapChannelArea.containsKey(channel.getBarrio1())) {
                channelBarrio.setBarrio1(mapChannelArea.get(channel.getBarrio1()));
            }
            if (channel.getBarrio2() != null && mapChannelArea.containsKey(channel.getBarrio2())) {
                channelBarrio.setBarrio2(mapChannelArea.get(channel.getBarrio2()));
            }
            if (channel.getBarrio3() != null && mapChannelArea.containsKey(channel.getBarrio3())) {
                channelBarrio.setBarrio3(mapChannelArea.get(channel.getBarrio3()));
            }
            if (channel.getBarrio4() != null && mapChannelArea.containsKey(channel.getBarrio4())) {
                channelBarrio.setBarrio4(mapChannelArea.get(channel.getBarrio4()));
            }
            if (channel.getBarrio5() != null && mapChannelArea.containsKey(channel.getBarrio5())) {
                channelBarrio.setBarrio5(mapChannelArea.get(channel.getBarrio5()));
            }
            queryResult.setChannelBarrio(channelBarrio);

            // 组织架构
            Channelorganization channelorganization = new Channelorganization();
            if (channel.getOrganization1() != null && mapOrganization.containsKey(channel.getOrganization1())) {
                channelorganization.setOrganization1(mapOrganization.get(channel.getOrganization1()));
            }
            if (channel.getOrganization2() != null && mapOrganization.containsKey(channel.getOrganization2())) {
                channelorganization.setOrganization2(mapOrganization.get(channel.getOrganization2()));
            }
            if (channel.getOrganization3() != null && mapOrganization.containsKey(channel.getOrganization3())) {
                channelorganization.setOrganization3(mapOrganization.get(channel.getOrganization3()));
            }
            if (channel.getOrganization4() != null && mapOrganization.containsKey(channel.getOrganization4())) {
                channelorganization.setOrganization4(mapOrganization.get(channel.getOrganization4()));
            }
            if (channel.getOrganization5() != null && mapOrganization.containsKey(channel.getOrganization5())) {
                channelorganization.setOrganization5(mapOrganization.get(channel.getOrganization5()));
            }
            queryResult.setChannelorganization(channelorganization);

            // 渠道收货信息

            // 品牌
            if(hashMapChannelBrand.containsKey(channel.getId())) {
                List<String> brandList = hashMapChannelBrand.get(channel.getId()).stream().map(ChannelBrandDto::getBrandName).collect(Collectors.toList());
                if(brandList.size() > 0) {
                    queryResult.setBrand(brandList.toArray(new String[brandList.size()]));
                }
            }
            // 分公司
            if (channel.getBranchCompanyId() != null && mapBranchCompany.containsKey(channel.getBranchCompanyId())) {
                queryResult.setBranchCompany(mapBranchCompany.get(channel.getBranchCompanyId()));
            }
            // 渠道等级
            if (channel.getGradeId() != null && mapChannelGrade.containsKey(channel.getGradeId())) {
                queryResult.setGrade(mapChannelGrade.get(channel.getGradeId()));
            }
            // 渠道业态
            if (channel.getBusinessFormatId() != null && mapBusinessFormat.containsKey(channel.getBusinessFormatId())) {
                queryResult.setBusinessFormat(mapBusinessFormat.get(channel.getBusinessFormatId()));
            }
            // 渠道经营性质
            if (channel.getBusinessNatureId() != null && mapBusinessNature.containsKey(channel.getBusinessNatureId())) {
                queryResult.setBusinessNature(mapBusinessNature.get(channel.getBusinessNatureId()));
            }
            // 结算方式
            if (channel.getBalanceTypeId() != null && mapBalanceType.containsKey(channel.getBalanceTypeId())) {
                queryResult.setBalanceType(mapBalanceType.get(channel.getBalanceTypeId()));
            }
            // 零售吊牌价类型
            if (channel.getRetailTagPriceTypeId() != null && mapTagPriceType.containsKey(channel.getRetailTagPriceTypeId())) {
                queryResult.setRetailTagPriceType(mapTagPriceType.get(channel.getRetailTagPriceTypeId()));
            }
            // 分销吊牌价类型
            if (channel.getSaleTagPriceTypeId() != null && mapTagPriceType.containsKey(channel.getSaleTagPriceTypeId())) {
                queryResult.setSaleTagPriceType(mapTagPriceType.get(channel.getSaleTagPriceTypeId()));
            }
            // 销售范围
            if (channel.getSaleRangeId() != null && mapSaleRange.containsKey(channel.getSaleRangeId())) {
                queryResult.setSaleRange(mapSaleRange.get(channel.getSaleRangeId()));
            }
            // 资金号
            if (channel.getFundAccountId() != null && mapFundAccount.containsKey(channel.getFundAccountId())) {
                queryResult.setFundAccount(mapFundAccount.get(channel.getFundAccountId()));
            }
        }

        return queryResults;
    }

}
