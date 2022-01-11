package com.regent.rbp.api.service.bean.channel;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.BranchCompany;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.core.base.SaleRange;
import com.regent.rbp.api.core.base.TagPriceType;
import com.regent.rbp.api.core.channel.*;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.dao.base.BranchCompanyDao;
import com.regent.rbp.api.dao.base.BrandDao;
import com.regent.rbp.api.dao.base.SaleRangeDao;
import com.regent.rbp.api.dao.base.TagPriceTypeDao;
import com.regent.rbp.api.dao.channel.*;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.*;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.channel.ChannelService;
import com.regent.rbp.api.service.channel.context.ChannelQueryContext;
import com.regent.rbp.api.service.channel.context.ChannelSaveContext;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    @Autowired
    ChannelReceiveInfoDao channelReceiveInfoDao;
    @Autowired
    BaseDbService baseDbService;

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
     *
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

        if (StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if (StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        if (StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if (StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateEnd);
        }
    }

    /**
     * 查询渠道数据
     *
     * @param context
     * @return
     */
    private PageDataResponse<ChannelQueryResult> searchPage(ChannelQueryContext context) {

        PageDataResponse<ChannelQueryResult> result = new PageDataResponse<ChannelQueryResult>();

        Page<Channel> pageModel = new Page<Channel>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);

        IPage<Channel> channelPageData = channelDao.selectPage(pageModel, queryWrapper);
        List<ChannelQueryResult> list = convertQueryResult(channelPageData.getRecords());

        result.setTotalCount(channelPageData.getTotal());
        result.setData(list);

        return result;
    }

    /**
     * 处理查询结果的属性
     * 1.读取相同的属性
     * 2.将内部编码id转换成名称name
     */
    private List<ChannelQueryResult> convertQueryResult(List<Channel> channelList) {
        List<ChannelQueryResult> queryResults = new ArrayList<>(channelList.size());
        List<Long> channelIds = channelList.stream().map(Channel::getId).collect(Collectors.toList());

        // 加载所有 渠道品牌
        List<ChannelBrandDto> channelBrandList = channelBrandDao.selectChannelBrandDtoByChannelIds(channelIds);
        Map<Long, List<ChannelBrandDto>> hashMapChannelBrand = channelBrandList.stream().collect(Collectors.groupingBy(ChannelBrandDto::getChannelId));

        // 加载所有 渠道收货信息
        List<AddressData> addressDataList = channelReceiveInfoDao.selectChannelReceiveInfoByChannelIds(channelIds);
        Map<Long, List<AddressData>> hashMapAddress = addressDataList.stream().collect(Collectors.groupingBy(AddressData::getChannelId));

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

        // 自定义字段
        Map<Long, List<CustomizeDataDto>> customizeColumnMap = baseDbService.getCustomizeColumnMap(TableConstants.CHANNEL, channelIds);

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
            if (hashMapAddress.containsKey(channel.getId())) {
                List<AddressData> addressList = hashMapAddress.get(channel.getId());
                if (addressList.size() > 0) {
                    queryResult.setAddressData(addressList);
                }
            }

            // 自定义字段
            if (customizeColumnMap.containsKey(channel.getId())) {
                queryResult.setCustomizeData(customizeColumnMap.get(channel.getId()));
            }

            // 品牌
            if (hashMapChannelBrand.containsKey(channel.getId())) {
                List<String> brandList = hashMapChannelBrand.get(channel.getId()).stream().map(ChannelBrandDto::getBrandName).collect(Collectors.toList());
                if (brandList.size() > 0) {
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
            queryResults.add(queryResult);
        }
        return queryResults;
    }

    /**
     * 整理查询条件构造器
     *
     * @return
     */
    private QueryWrapper processQueryWrapper(ChannelQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<Channel>();
        if (context.getChannelCode() != null && context.getChannelCode().length > 0)
            queryWrapper.in("code", context.getChannelCode());
        if (StringUtils.isNotBlank(context.getChannelName()))
            queryWrapper.eq("name", context.getChannelName());
        if (StringUtils.isNotBlank(context.getChannelFullName()))
            queryWrapper.eq("full_name", context.getChannelFullName());
        if (context.getChannelAddress() != null && context.getChannelAddress().length > 0)
            queryWrapper.in("address", context.getChannelAddress());
        if (context.getBrand() != null && context.getBrand().length > 0) {
            List<ChannelBrand> channelBrandList = channelBrandDao.selectList(new QueryWrapper<ChannelBrand>().in("brand_id", context.getBrand()));
            List<Long> channelIds = channelBrandList.stream().map(ChannelBrand::getChannelId).collect(Collectors.toList());
            queryWrapper.in("id", channelIds);
        }
        if (context.getBranchCompany() != null && context.getBranchCompany().length > 0)
            queryWrapper.in("branch_company_id", context.getBranchCompany());
        if (context.getGrade() != null && context.getGrade().length > 0)
            queryWrapper.in("grade_id", context.getGrade());
        if (context.getBusinessFormat() != null && context.getBusinessFormat().length > 0)
            queryWrapper.in("business_format_id", context.getBusinessFormat());
        if (context.getBusinessNature() != null && context.getBusinessNature().length > 0)
            queryWrapper.in("business_nature_id", context.getBusinessNature());
        if (context.getBalanceType() != null && context.getBalanceType().length > 0)
            queryWrapper.in("balance_type_id", context.getBalanceType());
        if (context.getRetailTagPriceType() != null && context.getRetailTagPriceType().length > 0)
            queryWrapper.in("retail_tag_price_type_id", context.getRetailTagPriceType());
        if (context.getSaleTagPriceType() != null && context.getSaleTagPriceType().length > 0)
            queryWrapper.in("sale_tag_price_type_id", context.getSaleTagPriceType());
        if (context.getSaleRange() != null && context.getSaleRange().length > 0)
            queryWrapper.in("sale_range_id", context.getSaleRange());
        if (context.getLinkMan() != null && context.getLinkMan().length > 0)
            queryWrapper.in("link_man", context.getLinkMan());
        if (context.getLinkManMobile() != null && context.getLinkManMobile().length > 0)
            queryWrapper.in("link_man_mobile", context.getLinkManMobile());
        if (context.getFundAccount() != null && context.getFundAccount().length > 0)
            queryWrapper.in("fund_account_id", context.getFundAccount());
        if (context.getStatus() != null && context.getStatus().length > 0)
            queryWrapper.in("status", context.getStatus());

        if (context.getPhysicalRegion() != null) {
            PhysicalRegion physicalRegion = context.getPhysicalRegion();
            if (StringUtils.isNotBlank(physicalRegion.getNation()))
                queryWrapper.eq("nation", physicalRegion.getNation());
            if (StringUtils.isNotBlank(physicalRegion.getRegion()))
                queryWrapper.eq("region", physicalRegion.getRegion());
            if (StringUtils.isNotBlank(physicalRegion.getProvince()))
                queryWrapper.eq("province", physicalRegion.getProvince());
            if (StringUtils.isNotBlank(physicalRegion.getCity()))
                queryWrapper.eq("city", physicalRegion.getCity());
            if (StringUtils.isNotBlank(physicalRegion.getCounty()))
                queryWrapper.eq("county", physicalRegion.getCounty());
        }

        if (context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if (context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if (context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if (context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if (context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if (context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }

        return queryWrapper;
    }

    @Override
    @Transactional
    public DataResponse save(ChannelSaveParam param) {
        boolean createFlag = true;
        ChannelSaveContext context = new ChannelSaveContext(param);
        //判断是新增还是更新
        Channel item = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getChannelCode()));
        if (item != null) {
            context.getChannel().setId(item.getId());
            createFlag = false;
        }

        //验证渠道数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return ModelDataResponse.errorParameter(message);
        }
        // 自动补充不存在的数据字典
        processAutoCompleteDictionary(param, context);
        // 写入渠道表
        saveChannel(createFlag, context.getChannel());
        // 写入渠道品牌关系表
        saveChannelBrand(context.getChannel().getId(), context.getChannelBrandList());
        // 写入渠道收货信息
        saveChannelReceiveInfo(context.getChannel().getId(), context.getChannelReceiveInfoList());

        // 自定义字段
        baseDbService.saveOrUpdateCustomFieldData(InformationConstants.ModuleConstants.CHANNEL_INFO, TableConstants.CHANNEL, context.getChannel().getId(), param.getCustomizeData());

        return ModelDataResponse.Success(context.getChannel().getId());
    }

    /**
     * 验证渠道属性
     *
     * @param param
     */
    private List<String> verificationProperty(ChannelSaveParam param, ChannelSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        Channel channel = context.getChannel();

        if (StringUtils.isBlank(param.getChannelCode())) {
            errorMsgList.add("渠道编号(channelCode)不能为空");
        } else {
            Channel item = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getChannelCode()));
            if (item != null) {
                channel.setId(item.getId());
            }
        }

        if (StringUtils.isBlank(param.getChannelName())) {
            errorMsgList.add("渠道简称(channelName)不能为空");
        }
        if (StringUtils.isBlank(param.getChannelFullName())) {
            errorMsgList.add("渠道名称(channelFullName)不能为空");
        }
        //验证 渠道业态
        if (StringUtils.isBlank(param.getBusinessFormat())) {
            errorMsgList.add("渠道业态(businessFormat)不能为空");
        } else {
            ChannelBusinessFormat item = channelBusinessFormatDao.selectOne(new QueryWrapper<ChannelBusinessFormat>().eq("name", param.getBusinessFormat()));
            if (item != null) {
                channel.setBusinessFormatId(item.getId());
            }
        }
        //验证 经营性质
        if (StringUtils.isBlank(param.getBusinessNature())) {
            errorMsgList.add("经营性质(businessNature)不能为空");
        } else {
            ChannelBusinessNature item = channelBusinessNatureDao.selectOne(new QueryWrapper<ChannelBusinessNature>().eq("name", param.getBusinessNature()));
            if (item != null) {
                channel.setBusinessNatureId(item.getId());
            }
        }
        //验证 渠道收货信息
        if (param.getAddressData() != null && param.getAddressData().size() > 0) {
            for (AddressData addressData : param.getAddressData()) {
                if (StringUtils.isBlank(addressData.getAddress())) {
                    errorMsgList.add("详细地址(address)不能为空");
                }
                if (StringUtils.isBlank(addressData.getContactsPerson())) {
                    errorMsgList.add("联系人(contactsPerson)不能为空");
                }
                if (StringUtils.isBlank(addressData.getMobile())) {
                    errorMsgList.add("手机号码(mobile)不能为空");
                }
            }
        }
        // 资金号
        if (StringUtils.isNotBlank(param.getFundAccount())) {
            FundAccount fundAccount = fundAccountDao.selectOne(new QueryWrapper<FundAccount>().eq("name", param.getFundAccount()));
            if (fundAccount == null) {
                errorMsgList.add("资金号(fundAccount)不存在");
            } else {
                channel.setFundAccountId(fundAccount.getId());
            }
        }

        return errorMsgList;
    }

    /**
     * 自动补充不存在的数据字典
     *
     * @param param
     */
    private void processAutoCompleteDictionary(ChannelSaveParam param, ChannelSaveContext context) {
        Channel channel = context.getChannel();
        List<ChannelBrand> channelBrandList = context.getChannelBrandList();
        // 行政区域
        if (param.getChannelBarrio() != null) {
            // 行政区域1
            if (StringUtils.isNotBlank(param.getChannelBarrio().getBarrio1())) {
                Long barrio1 = this.saveChannelArea(new Long(0), 1, param.getChannelBarrio().getBarrio1(), "nation", "1");
                channel.setBarrio1(barrio1);
                // 行政区域2
                if (StringUtils.isNotBlank(param.getChannelBarrio().getBarrio2())) {
                    Long barrio2 = this.saveChannelArea(barrio1, 2, param.getChannelBarrio().getBarrio2(), "province", "2");
                    channel.setBarrio2(barrio2);
                    // 行政区域3
                    if (StringUtils.isNotBlank(param.getChannelBarrio().getBarrio3())) {
                        Long barrio3 = this.saveChannelArea(barrio2, 3, param.getChannelBarrio().getBarrio3(), "city", "3");
                        channel.setBarrio3(barrio3);
                        // 行政区域4
                        if (StringUtils.isNotBlank(param.getChannelBarrio().getBarrio4())) {
                            Long barrio4 = this.saveChannelArea(barrio3, 4, param.getChannelBarrio().getBarrio4(), "county", "4");
                            channel.setBarrio4(barrio4);
                            // 行政区域5
                            if (StringUtils.isNotBlank(param.getChannelBarrio().getBarrio5())) {
                                Long barrio5 = this.saveChannelArea(barrio4, 5, param.getChannelBarrio().getBarrio5(), "county", "5");
                                channel.setBarrio5(barrio5);
                            }
                        }
                    }
                }
            }
        }
        // 品牌
        if (param.getBrand() != null && param.getBrand().length > 0) {
            for (String str : param.getBrand()) {
                Brand brand = brandDao.selectOne(new QueryWrapper<Brand>().eq("name", str));
                if (brand == null) {
                    Brand.build("", str);
                    brandDao.insert(brand);
                }
                channelBrandList.add(new ChannelBrand(channel.getId(), brand.getId()));
            }
        }
        // 分公司
        if (StringUtils.isNotBlank(param.getBranchCompany())) {
            BranchCompany branchCompany = branchCompanyDao.selectOne(new QueryWrapper<BranchCompany>().eq("name", param.getBranchCompany()));
            if (branchCompany == null) {
                branchCompany = new BranchCompany("", param.getBranchCompany());
                branchCompanyDao.insert(branchCompany);
            }
            channel.setBranchCompanyId(branchCompany.getId());
        }
        // 渠道等级
        if (StringUtils.isNotBlank(param.getGrade())) {
            ChannelGrade channelGrade = channelGradeDao.selectOne(new QueryWrapper<ChannelGrade>().eq("name", param.getGrade()));
            if (channelGrade == null) {
                channelGrade = new ChannelGrade("", param.getGrade());
                channelGradeDao.insert(channelGrade);
            }
            channel.setGradeId(channelGrade.getId());
        }
        // 结算方式
        if (StringUtils.isNotBlank(param.getBalanceType())) {
            ChannelBalanceType balanceType = channelBalanceTypeDao.selectOne(new QueryWrapper<ChannelBalanceType>().eq("name", param.getBalanceType()));
            if (balanceType == null) {
                balanceType = new ChannelBalanceType("", param.getBalanceType());
                channelBalanceTypeDao.insert(balanceType);
            }
            channel.setBalanceTypeId(balanceType.getId());
        }
        // 零售吊牌类型
        if (StringUtils.isNotBlank(param.getRetailTagPriceType())) {
            TagPriceType tagPriceType = tagPriceTypeDao.selectOne(new QueryWrapper<TagPriceType>().eq("name", param.getRetailTagPriceType()));
            if (tagPriceType == null) {
                tagPriceType = new TagPriceType("", param.getRetailTagPriceType());
                tagPriceTypeDao.insert(tagPriceType);
            }
            channel.setRetailTagPriceTypeId(tagPriceType.getId());
        }
        // 分销吊牌价类型
        if (StringUtils.isNotBlank(param.getSaleTagPriceType())) {
            TagPriceType tagPriceType = tagPriceTypeDao.selectOne(new QueryWrapper<TagPriceType>().eq("name", param.getSaleTagPriceType()));
            if (tagPriceType == null) {
                tagPriceType = new TagPriceType("", param.getSaleTagPriceType());
                tagPriceTypeDao.insert(tagPriceType);
            }
            channel.setSaleTagPriceTypeId(tagPriceType.getId());
        }
        // 销售范围
        if (StringUtils.isNotBlank(param.getSaleRange())) {
            SaleRange saleRange = saleRangeDao.selectOne(new QueryWrapper<SaleRange>().eq("name", param.getSaleRange()));
            if (saleRange == null) {
                saleRange = new SaleRange("", param.getSaleRange());
                saleRangeDao.insert(saleRange);
            }
            channel.setSaleRangeId(saleRange.getId());
        }

    }

    /**
     * 行政区域
     *
     * @param parentId
     * @param depth
     * @param name
     * @param columnName
     * @param orderNumber
     * @return
     */
    private Long saveChannelArea(Long parentId, Integer depth, String name, String columnName, String orderNumber) {
        ChannelArea channelArea = channelAreaDao.selectOne(new QueryWrapper<ChannelArea>()
                .eq("parent_id", parentId).eq("depth", depth).eq("name", name));
        if (channelArea == null) {
            channelArea = new ChannelArea(parentId, depth, name, columnName, orderNumber);
            channelAreaDao.insert(channelArea);
        }
        return channelArea.getId();
    }

    /**
     * 写入渠道
     *
     * @param createFlag
     * @param channel
     */
    private void saveChannel(Boolean createFlag, Channel channel) {
        if (createFlag) {
            channelDao.insert(channel);
        } else {
            channelDao.updateById(channel);
        }
    }

    /**
     * 写入渠道品牌关系表
     *
     * @param channelId
     * @param channelBrandList
     */
    private void saveChannelBrand(Long channelId, List<ChannelBrand> channelBrandList) {
        if (CollUtil.isNotEmpty(channelBrandList)) {
            channelBrandDao.delete(new QueryWrapper<ChannelBrand>().eq("channel_id", channelId));
            for (ChannelBrand channelBrand : channelBrandList) {
                channelBrandDao.insert(channelBrand);
            }
        }
    }

    /**
     * 写入渠道收货信息
     *
     * @param channelId
     * @param channelReceiveInfoList
     */
    private void saveChannelReceiveInfo(Long channelId, List<ChannelReceiveInfo> channelReceiveInfoList) {
        if (CollUtil.isNotEmpty(channelReceiveInfoList)) {
            channelReceiveInfoDao.delete(new QueryWrapper<ChannelReceiveInfo>().eq("channel_id", channelId));
            for (ChannelReceiveInfo receiveInfo : channelReceiveInfoList) {
                channelReceiveInfoDao.insert(receiveInfo);
            }
        }
    }
}
