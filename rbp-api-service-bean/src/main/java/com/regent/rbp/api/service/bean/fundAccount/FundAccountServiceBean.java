package com.regent.rbp.api.service.bean.fundAccount;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Brand;
import com.regent.rbp.api.core.base.DiscountCategory;
import com.regent.rbp.api.core.channel.ChannelOrganization;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.fundAccount.FundAccountBank;
import com.regent.rbp.api.core.fundAccount.FundAccountBrandPricePolicy;
import com.regent.rbp.api.core.fundAccount.FundAccountPricePolicy;
import com.regent.rbp.api.core.fundAccount.PriceType;
import com.regent.rbp.api.dao.base.BrandDao;
import com.regent.rbp.api.dao.base.CustomizeColumnDao;
import com.regent.rbp.api.dao.base.DiscountCategoryDao;
import com.regent.rbp.api.dao.channel.ChannelOrganizationDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountBankDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountBrandPricePolicyDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dao.fundAccount.FundAccountPricePolicyDao;
import com.regent.rbp.api.dao.fundAccount.PriceTypeDao;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.Channelorganization;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.fundAccount.BankAccount;
import com.regent.rbp.api.dto.fundAccount.BrandPricePolicy;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryParam;
import com.regent.rbp.api.dto.fundAccount.FundAccountQueryResult;
import com.regent.rbp.api.dto.fundAccount.FundAccountSaveParam;
import com.regent.rbp.api.dto.fundAccount.PricePolicy;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.fundAccount.FundAccountService;
import com.regent.rbp.api.service.fundAccount.context.FundAccountQueryContext;
import com.regent.rbp.api.service.fundAccount.context.FundAccountSaveContext;
import com.regent.rbp.api.service.enums.BaseModuleEnum;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 资金号
 * @Author shaoqidong
 * @Date 2021/11/30
 **/
@Service
public class FundAccountServiceBean implements FundAccountService {

    @Autowired
    FundAccountDao fundAccountDao;
    @Autowired
    FundAccountPricePolicyDao fundAccountPricePolicyDao;
    @Autowired
    FundAccountBankDao fundAccountBankDao;
    @Autowired
    FundAccountBrandPricePolicyDao fundAccountBrandPricePolicyDao;
    @Autowired
    PriceTypeDao priceTypeDao;
    @Autowired
    BrandDao brandDao;
    @Autowired
    DiscountCategoryDao discountCategoryDao;
    @Autowired
    CustomizeColumnDao customizeColumnDao;
    @Autowired
    ChannelOrganizationDao channelOrganizationDao;
    @Autowired
    BaseDbService baseDbService;


    @Override
    public PageDataResponse<FundAccountQueryResult> query(FundAccountQueryParam param) {
        FundAccountQueryContext context = new FundAccountQueryContext();
        //将入参转换成查询的上下文对象
        convertFundAccountQueryContext(param, context);
        //查询数据
        PageDataResponse<FundAccountQueryResult> response = searchPage(context);
        return response;
    }

    private PageDataResponse<FundAccountQueryResult> searchPage(FundAccountQueryContext context) {
        PageDataResponse<FundAccountQueryResult> result = new PageDataResponse<FundAccountQueryResult>();

        Page<FundAccount> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        //整理查询条件构造器
        QueryWrapper queryWrapper = processQueryWrapper(context);
        //分页查询
        IPage<FundAccount> pageDate = fundAccountDao.selectPage(pageModel, queryWrapper);
        //处理查询结果的属性
        List<FundAccountQueryResult> list = convertQueryResult(pageDate.getRecords());

        result.setTotalCount(pageDate.getTotal());
        result.setData(list);
        return result;
    }

    private List<FundAccountQueryResult> convertQueryResult(List<FundAccount> records) {
        List<FundAccountQueryResult> results = new ArrayList<>();
        if(CollUtil.isNotEmpty(records)){
            List<Long> fundAccountIdList = records.stream().map(FundAccount::getId).collect(Collectors.toList());
            //资金号价格政策
            List<FundAccountPricePolicy> fundAccountPricePolicies = fundAccountPricePolicyDao.selectList(new LambdaQueryWrapper<FundAccountPricePolicy>().eq(FundAccountPricePolicy::getFundAccountId, fundAccountIdList));
            Map<Long, List<FundAccountPricePolicy>> hashMapPricePolicie = fundAccountPricePolicies.stream().collect(Collectors.groupingBy(FundAccountPricePolicy::getFundAccountId));
            //资金号品牌价格政策
            List<FundAccountBrandPricePolicy> fundAccountBrandPricePolicyList = fundAccountBrandPricePolicyDao.selectList(new LambdaQueryWrapper<FundAccountBrandPricePolicy>().eq(FundAccountBrandPricePolicy::getFundAccountId, fundAccountIdList));
            Map<Long, List<FundAccountBrandPricePolicy>> hashMapBrandPricePolicy = fundAccountBrandPricePolicyList.stream().collect(Collectors.groupingBy(FundAccountBrandPricePolicy::getFundAccountId));
            //资金号银行账号
            List<FundAccountBank> fundAccountBankList = fundAccountBankDao.selectList(new LambdaQueryWrapper<FundAccountBank>().eq(FundAccountBank::getFundAccountId, fundAccountIdList));
            Map<Long, List<FundAccountBank>> hashMapBank = fundAccountBankList.stream().collect(Collectors.groupingBy(FundAccountBank::getFundAccountId));
            // 加载所有 渠道组织架构
            List<ChannelOrganization> organizationList = channelOrganizationDao.selectList(new QueryWrapper<ChannelOrganization>().select("id", "name"));
            Map<Long, String> mapOrganization = organizationList.stream().collect(Collectors.toMap(ChannelOrganization::getId, ChannelOrganization::getName));
            List<PriceType> priceTypeList = priceTypeDao.selectList(new QueryWrapper<>());
            Map<Long, String> hashMapPriceType = priceTypeList.stream().collect(Collectors.toMap(PriceType::getId, x -> x.getName()));
            //折扣类型
            List<DiscountCategory> listDiscountCategory = discountCategoryDao.selectList(new QueryWrapper<DiscountCategory>().select("id","name"));
            Map<Long, String> mapDiscountCategory = listDiscountCategory.stream().collect(Collectors.toMap(DiscountCategory::getId, DiscountCategory::getName));
            //品牌
            List<Brand> brands = brandDao.selectList(new QueryWrapper<>());
            Map<Long, String> mapBrand = brands.stream().collect(Collectors.toMap(Brand::getId, Brand::getName));

            //自定义字段列表
            List<String> listCustomizeColumn = customizeColumnDao.selectCustomizeColumnCodeByModuleId(BaseModuleEnum.GOODS.getBaseModuleId());
            for (FundAccount record : records) {
                FundAccountQueryResult result = new FundAccountQueryResult();
                result.setCode(record.getCode());
                result.setName(record.getName());
                result.setNotes(record.getNotes());
                result.setLegalPerson(record.getLegalPerson());
                result.setCredit(record.getCredit());
                result.setTaxNumber(record.getTaxNumber());
                result.setTaxRate(record.getTaxRate());
                result.setType(record.getType());
                //上级资金号
                if(record.getParentId() != null){
                    FundAccount fundAccount = fundAccountDao.selectById(record.getParentId());
                    result.setParentCode(fundAccount != null ? fundAccount.getCode():null);
                }

                //资金号价格政策
                if (record.getId() != null && hashMapPricePolicie.containsKey(record.getId())) {
                    List<FundAccountPricePolicy> policyList = hashMapPricePolicie.get(record.getId());
                    if(CollUtil.isNotEmpty(listCustomizeColumn)){
                        FundAccountPricePolicy fundAccountPricePolicy = policyList.get(0);
                        PricePolicy pricePolicy = new PricePolicy();
                        pricePolicy.setDiscount(fundAccountPricePolicy.getDiscount());
                        pricePolicy.setPriceTypeName(hashMapPriceType.get(fundAccountPricePolicy.getPriceTypeId()));
                        result.setPricePolicy(pricePolicy);
                    }
                }

                //资金号品牌价格政策
                if (record.getId() != null && hashMapBrandPricePolicy.containsKey(record.getId())) {
                    List<FundAccountBrandPricePolicy> brandPricePolicyList = hashMapBrandPricePolicy.get(record.getId());
                    if(CollUtil.isNotEmpty(listCustomizeColumn)){
                        FundAccountBrandPricePolicy fundAccountBrandPricePolicy = brandPricePolicyList.get(0);
                        BrandPricePolicy brandPricePolicy = new BrandPricePolicy();
                        brandPricePolicy.setBrandName(mapBrand.get(fundAccountBrandPricePolicy.getBrandId()));
                        brandPricePolicy.setDiscount(fundAccountBrandPricePolicy.getDiscount());
                        brandPricePolicy.setDiscountCategoryName(mapDiscountCategory.get(fundAccountBrandPricePolicy.getDiscountCategoryId()));
                        brandPricePolicy.setPriceTypeName(hashMapPriceType.get(fundAccountBrandPricePolicy.getPriceTypeId()));
                        result.setBrandPricePolicy(brandPricePolicy);
                    }
                }

                //资金号银行账号
                if (record.getId() != null && hashMapBank.containsKey(record.getId())) {
                    List<FundAccountBank> fundAccountBanks = hashMapBank.get(record.getId());
                    if(CollUtil.isNotEmpty(listCustomizeColumn)){
                        FundAccountBank fundAccountBank = fundAccountBanks.get(0);
                        BankAccount bankAccount = new BankAccount();
                        bankAccount.setAccount(fundAccountBank.getAccount());
                        bankAccount.setAccountBank(fundAccountBank.getAccountBank());
                        bankAccount.setBankName(fundAccountBank.getAccountBankName());
                        bankAccount.setBankSubject(fundAccountBank.getBankSubject());
                        result.setBankAccount(bankAccount);
                    }
                }
                // 组织架构
                Channelorganization channelorganization = new Channelorganization();
                if (record.getOrganization1() != null && mapOrganization.containsKey(record.getOrganization1())) {
                    channelorganization.setOrganization1(mapOrganization.get(record.getOrganization1()));
                }
                if (record.getOrganization2() != null && mapOrganization.containsKey(record.getOrganization2())) {
                    channelorganization.setOrganization2(mapOrganization.get(record.getOrganization2()));
                }
                if (record.getOrganization3() != null && mapOrganization.containsKey(record.getOrganization3())) {
                    channelorganization.setOrganization3(mapOrganization.get(record.getOrganization3()));
                }
                if (record.getOrganization4() != null && mapOrganization.containsKey(record.getOrganization4())) {
                    channelorganization.setOrganization4(mapOrganization.get(record.getOrganization4()));
                }
                if (record.getOrganization5() != null && mapOrganization.containsKey(record.getOrganization5())) {
                    channelorganization.setOrganization5(mapOrganization.get(record.getOrganization5()));
                }
                result.setOrganization(channelorganization);
                //自定义字段
                Map<String, Object> map = baseDbService.queryCustomData(TableConstants.FUND_ACCOUNT, record.getId());
                if (CollUtil.isNotEmpty(map)) {
                    List<CustomizeDataDto> customizeDataDtoList = new ArrayList<>();
                    map.forEach((k,v)->{
                        CustomizeDataDto customizeDataDto = new CustomizeDataDto();
                        customizeDataDto.setCode(k);
                        customizeDataDto.setValue((String) v);
                        customizeDataDtoList.add(customizeDataDto);
                    });
                    result.setCustomizeData(customizeDataDtoList);
                }
                results.add(result);
            }
        }
        return results;
    }

    private QueryWrapper processQueryWrapper(FundAccountQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<FundAccount>();
        if(context.getCode() != null && context.getCode().length > 0 ) {
            queryWrapper.in("code", context.getCode());
        }
        if(context.getName() != null && context.getName().length > 0 ) {
            queryWrapper.in("name", context.getName());
        }
        if(context.getParentId() != null && context.getParentId().size() > 0 ) {
            queryWrapper.in("parent_id", context.getParentId());
        }
        if(context.getLegalPerson() != null && context.getLegalPerson().length > 0 ) {
            queryWrapper.in("legal_person", context.getLegalPerson());
        }
        if(context.getTaxNumber() != null && context.getTaxNumber().length > 0 ) {
            queryWrapper.in("tax_number", context.getTaxNumber());
        }
        if(context.getType() != null && context.getType().length > 0 ) {
            queryWrapper.in("type", context.getType());
        }
        if(context.getStatus() != null && context.getStatus().length > 0 ) {
            queryWrapper.in("status", context.getStatus());
        }
        if(context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if(context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if(context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if(context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if(context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if(context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }
        return queryWrapper;
    }

    private void convertFundAccountQueryContext(FundAccountQueryParam param, FundAccountQueryContext context) {
        context.setPageNo(param.getPageNo());
        context.setPageSize(param.getPageSize());
        context.setCode(param.getCode());
        context.setName(param.getName());
        context.setLegalPerson(param.getLegalPerson());
        context.setTaxNumber(param.getTaxNumber());
        context.setType(param.getType());
        context.setStatus(param.getStatus());

        if(param.getParentCode() != null && param.getParentCode().length > 0){
            List<FundAccount> codeList = fundAccountDao.selectList(new QueryWrapper<FundAccount>().in("code", param.getParentCode()));
            context.setParentId(codeList.stream().map(FundAccount::getId).collect(Collectors.toList()));
        }
        if(StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if(StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateend = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateend);
        }

        if(StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if(StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateend = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateend);
        }

        if(StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updateDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updateDateStart);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updateDateend = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updateDateend);
        }

    }

    @Override
    @Transactional
    public DataResponse save(FundAccountSaveParam param) {
        boolean createFlag = true;
        FundAccountSaveContext context = new FundAccountSaveContext(param);
        //判断是新增还是更新
        FundAccount item = fundAccountDao.selectOne(new QueryWrapper<FundAccount>().eq("code", param.getCode()));
        if (item != null) {
            context.getFundAccount().setId(item.getId());
            createFlag = false;
        }
        //验证数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }

        //保存资金号价格政策
        savaFundAccountPricePolicy(createFlag,context);
        //保存品牌价格政策
        savaBrandPricePolicy(createFlag,context);
        //保存银行账号
        savaBankAccount(createFlag,context);
        //保存价格政策
        savePricePolicy(createFlag,context);
        //保存资金号
        saveFundAccount(createFlag, context.getFundAccount());
        //保存单据自定义字段
        if (CollUtil.isNotEmpty(context.getCustomizeData())) {
            baseDbService.saveCustomFieldData(TableConstants.FUND_ACCOUNT, context.getFundAccount().getId(), context.getCustomizeData());
        }
        return ModelDataResponse.Success(null);
    }

    private void savePricePolicy(boolean createFlag, FundAccountSaveContext context) {
        fundAccountPricePolicyDao.insert(context.getFundAccountPricePolicy());
    }

    private void savaBankAccount(boolean createFlag, FundAccountSaveContext context) {
        fundAccountBankDao.insert(context.getFundAccountBank());
    }

    private void savaBrandPricePolicy(boolean createFlag, FundAccountSaveContext context) {
        fundAccountBrandPricePolicyDao.insert(context.getFundAccountBrandPricePolicy());
    }

    private void savaFundAccountPricePolicy(boolean createFlag, FundAccountSaveContext context) {
            fundAccountPricePolicyDao.insert(context.getFundAccountPricePolicy());
    }

    private void saveFundAccount(boolean createFlag, FundAccount fundAccount) {
        if (createFlag) {
            fundAccountDao.insert(fundAccount);
        } else {
            fundAccountDao.updateById(fundAccount);
        }
    }

    private List<String> verificationProperty(FundAccountSaveParam param, FundAccountSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        FundAccount fundAccount = context.getFundAccount();
        if(StringUtil.isEmpty(param.getCode())){
            errorMsgList.add("资金号(code)不能为空");
        }else{
            FundAccount item = fundAccountDao.selectOne(new QueryWrapper<FundAccount>().eq("code", param.getCode()));
            if(item != null) {
                fundAccount.setId(item.getId());
            }
        }
        if(StringUtil.isEmpty(param.getName())){
            errorMsgList.add("资金号名称(name)不能为空");
        }else{
            List<FundAccount> code = fundAccountDao.selectList(new QueryWrapper<FundAccount>().eq("code", param.getCode()));
            if(CollUtil.isEmpty(code)){
                errorMsgList.add("上级资金号(parentCode)不存在");
            }
        }

        //价格政策
        PricePolicy pricePolicy = param.getPricePolicy();
        if(null != pricePolicy){
            if (StringUtil.isEmpty(pricePolicy.getPriceTypeName())) {
                errorMsgList.add("价格类型名称(priceTypeName)不存在");
            }else{
                List<PriceType> priceTypeList = priceTypeDao.selectList(new QueryWrapper<PriceType>().eq("name", pricePolicy.getPriceTypeName()));
                if(CollUtil.isNotEmpty(priceTypeList)){
                    errorMsgList.add("价格类型名称(priceTypeName)不存在");
                }else{
                    PriceType priceType = priceTypeList.get(0);
                    context.getFundAccountPricePolicy().setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    context.getFundAccountPricePolicy().setPriceTypeId(priceType.getId());
                    context.getFundAccountPricePolicy().setDiscount(param.getPricePolicy().getDiscount());
                    context.getFundAccountPricePolicy().preInsert();;
                }
            }
        }

        //资金号银行账号
        BankAccount bankAccount = param.getBankAccount();
        if(null != bankAccount){
            context.getFundAccountBank().setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            context.getFundAccountBank().setFundAccountId(fundAccount.getId());
            context.getFundAccountBank().setAccount(bankAccount.getAccount());
            context.getFundAccountBank().setAccountBank(bankAccount.getAccountBank());
            context.getFundAccountBank().setAccountBankName(bankAccount.getBankName());
            context.getFundAccountBank().setBankSubject(bankAccount.getBankSubject());
            context.getFundAccountBank().preUpdate();
        }

        //资金号品牌价格政策
        BrandPricePolicy brandPricePolicy = param.getBrandPricePolicy();
        if(null != brandPricePolicy){
            context.getFundAccountBrandPricePolicy().setFundAccountId(fundAccount.getId());
            context.getFundAccountBrandPricePolicy().setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            context.getFundAccountBrandPricePolicy().setDiscount(brandPricePolicy.getDiscount());
            context.getFundAccountBrandPricePolicy().preInsert();
            if (StringUtil.isNotEmpty(brandPricePolicy.getBrandName())) {
                List<Brand> priceTypeList = brandDao.selectList(new QueryWrapper<Brand>().eq("name", brandPricePolicy.getBrandName()));
                if(CollUtil.isNotEmpty(priceTypeList)){
                    errorMsgList.add("品牌名称(brandName)不存在");
                }else{
                    Brand brand = priceTypeList.get(0);
                    context.getFundAccountBrandPricePolicy().setBrandId(brand.getId());
                }
            }

            if (StringUtil.isNotEmpty(brandPricePolicy.getDiscountCategoryName())) {
                List<DiscountCategory> discountCategoryList = discountCategoryDao.selectList(new QueryWrapper<DiscountCategory>().eq("name", brandPricePolicy.getDiscountCategoryName()));
                if(CollUtil.isNotEmpty(discountCategoryList)){
                    errorMsgList.add("折扣类别(discountCategoryName)不存在");
                }else{
                    DiscountCategory discountCategory = discountCategoryList.get(0);
                    context.getFundAccountBrandPricePolicy().setDiscountCategoryId(discountCategory.getId());
                }
            }

            if (StringUtil.isNotEmpty(brandPricePolicy.getPriceTypeName())) {
                List<PriceType> priceTypeList = priceTypeDao.selectList(new QueryWrapper<PriceType>().eq("name", brandPricePolicy.getPriceTypeName()));
                if(CollUtil.isNotEmpty(priceTypeList)){
                    errorMsgList.add("价格类型(priceTypeName)不存在");
                }else{
                    PriceType priceType = priceTypeList.get(0);
                    context.getFundAccountBrandPricePolicy().setPriceTypeId(priceType.getId());
                }
            }
        }
        return errorMsgList;
    }
}
