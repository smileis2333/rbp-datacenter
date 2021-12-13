package com.regent.rbp.api.service.bean.supplier;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.core.supplier.SupplierGrade;
import com.regent.rbp.api.core.supplier.SupplierNature;
import com.regent.rbp.api.dao.fundAccount.FundAccountDao;
import com.regent.rbp.api.dao.supplier.SupplierContactsPersonDao;
import com.regent.rbp.api.dao.supplier.SupplierDao;
import com.regent.rbp.api.dao.supplier.SupplierGradeDao;
import com.regent.rbp.api.dao.supplier.SupplierNatureDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceBean implements SupplierService {
    private final SupplierDao supplierDao;
    private final SupplierContactsPersonDao supplierContactsPersonDao;
    private final FundAccountDao fundAccountDao;
    private final BaseDbService baseDbService;
    private final SupplierNatureDao supplierNatureDao;
    private final SupplierGradeDao supplierGradeDao;

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
        ArrayList<Long> natureIds = CollUtil.distinct(CollUtil.map(records, Supplier::getNatureId, true));
        ArrayList<Long> gradeIds = CollUtil.distinct(CollUtil.map(records, Supplier::getGradeId, true));
        ArrayList<Long> fundAccountIds = CollUtil.distinct(CollUtil.map(records, Supplier::getFundAccountId, true));
        Map<Long, SupplierNature> natureMap = natureIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(supplierNatureDao.selectBatchIds(natureIds), new HashMap<>(), SupplierNature::getId);
        Map<Long, SupplierGrade> supplierGradeMap = gradeIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(supplierGradeDao.selectBatchIds(gradeIds), new HashMap<>(), SupplierGrade::getId);
        Map<Long, FundAccount> fundAccountMap = fundAccountIds.isEmpty() ? new HashMap<>() : CollUtil.toMap(fundAccountDao.selectBatchIds(fundAccountIds), new HashMap<>(), FundAccount::getId);
        return records.stream().map(e -> {
            SupplierQueryResult item = BeanUtil.copyProperties(e, SupplierQueryResult.class);
            item.setNature(natureMap.getOrDefault(e.getNatureId(), new SupplierNature()).getName());
            item.setGrade(supplierGradeMap.getOrDefault(e.getGradeId(), new SupplierGrade()).getName());
            item.setFundAccountCode(fundAccountMap.getOrDefault(e.getNatureId(), new FundAccount()).getCode());
            item.setSupplierCode(e.getCode());
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

        queryWrapper.like(StrUtil.isNotEmpty(context.getNation()), "nation", context.getNation());
        queryWrapper.like(StrUtil.isNotEmpty(context.getRegion()), "region", context.getRegion());
        queryWrapper.like(StrUtil.isNotEmpty(context.getProvince()), "province", context.getProvince());
        queryWrapper.like(StrUtil.isNotEmpty(context.getCity()), "city", context.getCity());
        queryWrapper.like(StrUtil.isNotEmpty(context.getCounty()), "county", context.getCounty());
        return queryWrapper;
    }

    private void convertQueryContext(SupplierQueryParam param, SupplierQueryContext context) {
        BeanUtil.copyProperties(param, context);
        context.setNatureIds(!CollUtil.isEmpty(param.getNature()) ? supplierNatureDao.selectList(new QueryWrapper<SupplierNature>().in("name", param.getNature())).stream().map(SupplierNature::getId).collect(Collectors.toList()) : null);
        context.setGradeIds(!CollUtil.isEmpty(param.getGrade()) ? supplierGradeDao.selectList(new QueryWrapper<SupplierGrade>().in("name", param.getGrade())).stream().map(SupplierGrade::getId).collect(Collectors.toList()) : null);
        context.setFundAccountIds(!CollUtil.isEmpty(param.getFundAccountCode()) ? fundAccountDao.selectList(new QueryWrapper<FundAccount>().in("code", param.getFundAccountCode())).stream().map(FundAccount::getId).collect(Collectors.toList()) : null);
        context.setPageNo(OptionalUtil.ofNullable(param, SupplierQueryParam::getPageNo, SystemConstants.PAGE_NO));
        context.setPageSize(OptionalUtil.ofNullable(param, SupplierQueryParam::getPageSize, SystemConstants.PAGE_SIZE));
        BeanUtil.copyProperties(param.getPhysicalRegion(), context);
    }


    @Override
    public DataResponse save(SupplierSaveParam param) {
        SupplierSaveContext context = new SupplierSaveContext(param);
        convertSaveContext(context);
        List<String> errors = validateContext(context);
        if (!errors.isEmpty()) {
            String message = StringUtil.join(errors, ",");
            return DataResponse.errorParameter(message);
        }
        supplierDao.insert(context.supplier);
        context.getSupplierContactsPersonList().forEach(supplierContactsPersonDao::insert);
        baseDbService.saveOrUpdateCustomFieldData(InformationConstants.ModuleConstants.SUPPLIER_INFO, TableConstants.SUPPLIER, context.supplier.getId(), context.getCustomizeDataDtos());
        return DataResponse.success();
    }

    private void convertSaveContext(SupplierSaveContext context) {
        SupplierNature nameNature = StrUtil.isNotEmpty(context.nature) ? supplierDao.selectNatureByName(context.nature) : null;
        SupplierGrade nameGrade = StrUtil.isNotEmpty(context.grade) ? supplierDao.selectGradeByName(context.grade) : null;
        FundAccount codeFundAccount = StrUtil.isNotEmpty(context.fundAccountCode) ? fundAccountDao.selectOne(new QueryWrapper<FundAccount>().eq("code", context.fundAccountCode)) : null;
        context.complete(nameNature, nameGrade, codeFundAccount);
    }

    private List<String> validateContext(SupplierSaveContext c) {
        Supplier codeSupplier = supplierDao.selectOne(new QueryWrapper<Supplier>().eq("code", c.supplier.getCode()));

        ArrayList<String> errors = new ArrayList<>();
        if (codeSupplier != null) {
            errors.add("供应商编号(supplierCode)不能重复");
        }
        if (StrUtil.isNotEmpty(c.nature) && c.supplier.getNatureId() == null) {
            errors.add("供应商性质(nature)不存在");
        }
        if (StrUtil.isNotEmpty(c.grade) && c.supplier.getGradeId() == null) {
            errors.add("等级(grade)不存在");
        }

        return errors;
    }

}
