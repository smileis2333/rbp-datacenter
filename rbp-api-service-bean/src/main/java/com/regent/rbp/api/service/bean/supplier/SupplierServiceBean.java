package com.regent.rbp.api.service.bean.supplier;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.supplier.*;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dao.supplier.*;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.supplier.ContactData;
import com.regent.rbp.api.dto.supplier.SupplierQueryParam;
import com.regent.rbp.api.dto.supplier.SupplierQueryResult;
import com.regent.rbp.api.dto.supplier.SupplierSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.supplier.SupplierService;
import com.regent.rbp.api.service.supplier.context.SupplierQueryContext;
import com.regent.rbp.api.service.supplier.context.SupplierSaveContext;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SupplierServiceBean implements SupplierService {
    @Autowired
    private  SupplierDao supplierDao;
    @Autowired
    private  SupplierContactsPersonDao supplierContactsPersonDao;
    @Autowired
    private  FundAccountDao fundAccountDao;
    @Autowired
    private  BaseDbService baseDbService;
    @Autowired
    private  SupplierNatureDao supplierNatureDao;
    @Autowired
    private  SupplierGradeDao supplierGradeDao;
    @Autowired
    private  SupplierAreaDao supplierAreaDao;
    @Autowired
    private  SupplierSendAddressDao supplierSendAddressDao;

    @Override
    public PageDataResponse<SupplierQueryResult> query(SupplierQueryParam param) {
        SupplierQueryContext context = new SupplierQueryContext();
        convertQueryContext(param, context);
        if (!context.isValid()) {
            return new PageDataResponse<>(0, new ArrayList<>());
        }

        PageDataResponse<SupplierQueryResult> result = new PageDataResponse<>();
        Page<Supplier> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper<Supplier> queryWrapper = processQueryWrapper(context);
        IPage<Supplier> dataTotal = supplierDao.selectPage(pageModel, queryWrapper);
        List<SupplierQueryResult> list = convertQueryResult(dataTotal.getRecords());
        result.setData(list);
        result.setTotalCount(dataTotal.getTotal());
        return result;
    }

    private List<SupplierQueryResult> convertQueryResult(List<Supplier> records) {
        if (records.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> supplierIds = CollUtil.map(records, Supplier::getId, true);
        ArrayList<Long> natureIds = CollUtil.distinct(CollUtil.map(records, Supplier::getNatureId, true));
        ArrayList<Long> gradeIds = CollUtil.distinct(CollUtil.map(records, Supplier::getGradeId, true));
        ArrayList<Long> fundAccountIds = CollUtil.distinct(CollUtil.map(records, Supplier::getFundAccountId, true));
        Map<Long, SupplierNature> natureMap = natureIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(supplierNatureDao.selectBatchIds(natureIds), new HashMap<>(), SupplierNature::getId);
        Map<Long, SupplierGrade> supplierGradeMap = gradeIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(supplierGradeDao.selectBatchIds(gradeIds), new HashMap<>(), SupplierGrade::getId);
        Map<Long, FundAccount> fundAccountMap = fundAccountIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(fundAccountDao.selectBatchIds(fundAccountIds), new HashMap<>(), FundAccount::getId);
        Map<Long, SupplierArea> areaMap = supplierAreaDao.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(SupplierArea::getId, Function.identity()));
        Map<Long, List<SupplierSendAddress>> sendAddressMap = supplierSendAddressDao.selectList(new QueryWrapper<SupplierSendAddress>().in("supplier_id", supplierIds)).stream().collect(Collectors.groupingBy(SupplierSendAddress::getSupplierId));
        Map<Long, List<SupplierContactsPerson>> contactMap = supplierContactsPersonDao.selectList(new QueryWrapper<SupplierContactsPerson>().in("supplier_id", supplierIds)).stream().collect(Collectors.groupingBy(SupplierContactsPerson::getSupplierId));
        Map<Long, List<CustomizeDataDto>> customizeColumnMap = baseDbService.getCustomizeColumnMap(TableConstants.SUPPLIER, supplierIds);

        return records.stream().map(e -> {
            SupplierQueryResult item = new SupplierQueryResult();
            BeanUtil.copyProperties(e, item, "nation", "province", "city", "county");
            item.setNature(natureMap.getOrDefault(e.getNatureId(), new SupplierNature()).getName());
            item.setGrade(supplierGradeMap.getOrDefault(e.getGradeId(), new SupplierGrade()).getName());
            item.setFundAccountCode(fundAccountMap.getOrDefault(e.getNatureId(), new FundAccount()).getCode());
            item.setSupplierCode(e.getCode());
            item.setNation(areaMap.getOrDefault(e.getNation(), new SupplierArea()).getName());
            item.setProvince(areaMap.getOrDefault(e.getProvince(), new SupplierArea()).getName());
            item.setCity(areaMap.getOrDefault(e.getCity(), new SupplierArea()).getName());
            item.setCounty(areaMap.getOrDefault(e.getCounty(), new SupplierArea()).getName());

            List<AddressData> addressData = sendAddressMap.containsKey(e.getId()) ? sendAddressMap.get(e.getId()).stream().map(ele -> {
                AddressData i = new AddressData();
                BeanUtil.copyProperties(ele, i);
                return i;
            }).collect(Collectors.toList()) : null;
            item.setAddressData(addressData);

            List<ContactData> contactData = contactMap.containsKey(e.getId()) ? contactMap.get(e.getId()).stream().map(ele -> {
                ContactData cd = new ContactData();
                BeanUtil.copyProperties(ele, cd);
                return cd;
            }).collect(Collectors.toList()) : null;
            item.setContactsPerson(contactData);
            item.setCustomizeData(customizeColumnMap.get(e.getId()));
            return item;
        }).collect(Collectors.toList());
    }

    private QueryWrapper processQueryWrapper(SupplierQueryContext context) {
        QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotEmpty(context.getSupplierCode()), "code", context.getSupplierCode());
        queryWrapper.like(StrUtil.isNotEmpty(context.getName()), "name", context.getName());
        queryWrapper.like(StrUtil.isNotEmpty(context.getFullName()), "full_name", context.getFullName());
        queryWrapper.in(CollUtil.isNotEmpty(context.getType()), "type", context.getType());

        queryWrapper.in(CollUtil.isNotEmpty(context.getReceiveDifferentType()), "receive_different_type", context.getReceiveDifferentType());
        queryWrapper.in(CollUtil.isNotEmpty(context.getNatureIds()), "nature_id", context.getNatureIds());
        queryWrapper.in(CollUtil.isNotEmpty(context.getGradeIds()), "grade_id", context.getGradeIds());
        queryWrapper.in(CollUtil.isNotEmpty(context.getHeadPerson()), "head_person", context.getHeadPerson());
        queryWrapper.like(StrUtil.isNotEmpty(context.getAddress()), "address", context.getAddress());
        queryWrapper.in(CollUtil.isNotEmpty(context.getFundAccountIds()), "fund_account_id", context.getFundAccountIds());
        queryWrapper.in(CollUtil.isNotEmpty(context.getStatus()), "status", context.getStatus());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());
        queryWrapper.ge(null != context.getCheckDateStart(), "check_time", context.getCheckDateStart());
        queryWrapper.le(null != context.getCheckDateEnd(), "check_time", context.getCheckDateEnd());

        queryWrapper.like(context.getNation() != null, "nation", context.getNation());
        queryWrapper.like(context.getProvince() != null, "province", context.getProvince());
        queryWrapper.like(context.getCity() != null, "city", context.getCity());
        queryWrapper.like(context.getCounty() != null, "county", context.getCounty());
        return queryWrapper;
    }

    private void convertQueryContext(SupplierQueryParam param, SupplierQueryContext context) {
        BeanUtil.copyProperties(param, context);
        context.setNatureIds(!CollUtil.isEmpty(param.getNature()) ? supplierNatureDao.selectList(new QueryWrapper<SupplierNature>().in("name", param.getNature())).stream().map(SupplierNature::getId).collect(Collectors.toList()) : null);
        context.setGradeIds(!CollUtil.isEmpty(param.getGrade()) ? supplierGradeDao.selectList(new QueryWrapper<SupplierGrade>().in("name", param.getGrade())).stream().map(SupplierGrade::getId).collect(Collectors.toList()) : null);
        context.setFundAccountIds(!CollUtil.isEmpty(param.getFundAccountCode()) ? fundAccountDao.selectList(new QueryWrapper<FundAccount>().in("code", param.getFundAccountCode())).stream().map(FundAccount::getId).collect(Collectors.toList()) : null);
        context.setPageNo(OptionalUtil.ofNullable(param, SupplierQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, SupplierQueryParam::getPageSize, SystemConstants.PAGE_SIZE));
        Map<String, SupplierArea> areaMap = supplierAreaDao.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(e -> String.format("%s_%s", e.getColumnName(), e.getName()), Function.identity()));
        if (StrUtil.isNotEmpty(param.getNation())) {
            context.setNation(areaMap.getOrDefault(String.format("%s_%s", "nation", param.getNation()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(param.getProvince())) {

            context.setNation(areaMap.getOrDefault(String.format("%s_%s", "province", param.getProvince()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(param.getCity())) {

            context.setNation(areaMap.getOrDefault(String.format("%s_%s", "city", param.getCity()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(param.getCounty())) {
            context.setNation(areaMap.getOrDefault(String.format("%s_%s", "country", param.getCounty()), new SupplierArea()).getId());
        }
    }


    @Override
    @Transactional
    public DataResponse save(SupplierSaveParam param) {
        Supplier supplier = supplierDao.selectOne(new QueryWrapper<Supplier>().eq("code", param.getSupplierCode()));
        SupplierSaveContext context = supplier != null ? new SupplierSaveContext(param, supplier.getId()) : new SupplierSaveContext(param);
        convertSaveContext(context);
        List<String> errors = validateContext(context);
        if (!errors.isEmpty()) {
            String message = StringUtil.join(errors, ",");
            return DataResponse.errorParameter(message);
        }
        if (supplier != null) {
            supplierDao.updateById(context.supplier);
            supplierContactsPersonDao.delete(new QueryWrapper<SupplierContactsPerson>().eq("supplier_id", context.supplier.getId()));
            supplierSendAddressDao.delete(new QueryWrapper<SupplierSendAddress>().eq("supplier_id", context.supplier.getId()));

        } else {
            supplierDao.insert(context.supplier);
        }
        context.getSupplierContactsPersonList().forEach(supplierContactsPersonDao::insert);
        context.getSupplierSendAddresses().forEach(supplierSendAddressDao::insert);
        baseDbService.saveOrUpdateCustomFieldData(InformationConstants.ModuleConstants.SUPPLIER_INFO, TableConstants.SUPPLIER, context.supplier.getId(), context.getCustomizeDataDtos());
        return DataResponse.success();
    }

    private void convertSaveContext(SupplierSaveContext context) {
        SupplierNature nameNature = StrUtil.isNotEmpty(context.nature) ? supplierDao.selectNatureByName(context.nature) : null;
        SupplierGrade nameGrade = StrUtil.isNotEmpty(context.grade) ? supplierDao.selectGradeByName(context.grade) : null;
        FundAccount codeFundAccount = StrUtil.isNotEmpty(context.fundAccountCode) ? fundAccountDao.selectOne(new QueryWrapper<FundAccount>().eq("code", context.fundAccountCode)) : null;
        Map<String, SupplierArea> areaMap = supplierAreaDao.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(e -> String.format("%s_%s", e.getColumnName(), e.getName()), Function.identity()));
        context.complete(nameNature, nameGrade, codeFundAccount, areaMap);
    }

    private List<String> validateContext(SupplierSaveContext c) {
        ArrayList<String> errors = new ArrayList<>();
        if (StrUtil.isNotEmpty(c.nature) && c.supplier.getNatureId() == null) {
            errors.add("供应商性质(nature)不存在");
        }
        if (StrUtil.isNotEmpty(c.grade) && c.supplier.getGradeId() == null) {
            errors.add("等级(grade)不存在");
        }
        Map<String, SupplierArea> areaMap = supplierAreaDao.selectList(new QueryWrapper<>()).stream().collect(Collectors.toMap(e -> String.format("%s_%s", e.getColumnName(), e.getName()), Function.identity()));

        if (StrUtil.isNotEmpty(c.params.getNation()) && !areaMap.containsKey(String.format("%s_%s", "nation", c.params.getNation()))) {
            errors.add(String.format("国家(nation)%s不存在", c.params.getNation()));
        }
        if (StrUtil.isNotEmpty(c.params.getProvince()) && !areaMap.containsKey(String.format("%s_%s", "province", c.params.getProvince()))) {
            errors.add(String.format("省(province)%s不存在", c.params.getProvince()));
        }
        if (StrUtil.isNotEmpty(c.params.getCity()) && !areaMap.containsKey(String.format("%s_%s", "city", c.params.getCity()))) {
            errors.add(String.format("城市(city)%s不存在", c.params.getCity()));
        }
        if (StrUtil.isNotEmpty(c.params.getCounty()) && !areaMap.containsKey(String.format("%s_%s", "country", c.params.getCounty()))) {
            errors.add(String.format("区/县(country)%s不存在", c.params.getCounty()));
        }

        return errors;
    }

}
