package com.regent.rbp.api.service.supplier.context;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.supplier.*;
import com.regent.rbp.api.dto.channel.AddressData;
import com.regent.rbp.api.dto.supplier.ContactData;
import com.regent.rbp.api.dto.supplier.SupplierSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupplierSaveContext {
    // state info
    public final Supplier supplier;
    private List<SupplierContactsPerson> supplierContactsPersonList = new ArrayList<>();
    private List<SupplierSendAddress> supplierSendAddresses = new ArrayList<>(); //todo
    private Map<String, Object> customizeDataDtos = new HashMap<>();
    public final SupplierSaveParam params;
    private boolean complete;

    // support check
    public final String nature;
    public final String grade;
    public final String fundAccountCode;

    public SupplierSaveContext(SupplierSaveParam param, Long id) {
        params = param;
        supplier = new Supplier();
        BeanUtil.copyProperties(param, supplier, "nation", "province", "city", "county");
        supplier.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        supplier.setId(id);
        supplier.setCode(param.getSupplierCode());
        supplier.setStatus(param.getStatus());

        nature = param.getNature();
        grade = param.getGrade();
        fundAccountCode = param.getFundAccountCode();
    }

    public SupplierSaveContext(SupplierSaveParam param) {
        this(param, SnowFlakeUtil.getDefaultSnowFlakeId());
    }


    public void addContactsPersonList(ContactData contactsPerson) {
        SupplierContactsPerson item = BeanUtil.copyProperties(contactsPerson, SupplierContactsPerson.class);
        item.setSupplierId(supplier.getId());
        supplierContactsPersonList.add(item);
    }

    public void addSendAddress(AddressData addressData) {
        SupplierSendAddress item = new SupplierSendAddress();
        item.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        item.setSupplierId(supplier.getId());
        item.setAddress(addressData.getAddress());
        item.setContactsPerson(addressData.getContactsPerson());
        item.setPostCode(Long.valueOf(addressData.getPostCode()));
        item.setMobile(Long.valueOf(addressData.getMobile()));
        item.setDefaultFlag(false);
        item.setNation(addressData.getNation());
        item.setProvince(addressData.getProvince());
        item.setCity(addressData.getCity());
        item.setCounty(addressData.getCounty());
        supplierSendAddresses.add(item);
    }

    public void complete(SupplierNature nameNature, SupplierGrade nameGrade, FundAccount codeFundAccount, Map<String, SupplierArea> areaMap) {
        if (nameNature != null) supplier.setNatureId(nameNature.getId());
        if (nameGrade != null) supplier.setGradeId(nameGrade.getId());
        if (codeFundAccount != null) supplier.setFundAccountId(codeFundAccount.getId());
        if (params.getContactsPerson() != null)
            params.getContactsPerson().forEach(this::addContactsPersonList);
        if (params.getAddressData() != null)
            params.getAddressData().forEach(this::addSendAddress);
        if (params.getCustomizeData() != null)
            params.getCustomizeData().forEach(e -> customizeDataDtos.put(e.getCode(), e.getValue()));
        if (StrUtil.isNotEmpty(params.getNation())) {
            supplier.setNation(areaMap.getOrDefault(String.format("%s_%s", "nation", params.getNation()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(params.getProvince())) {

            supplier.setNation(areaMap.getOrDefault(String.format("%s_%s", "province", params.getProvince()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(params.getCity())) {

            supplier.setNation(areaMap.getOrDefault(String.format("%s_%s", "city", params.getCity()), new SupplierArea()).getId());
        }
        if (StrUtil.isNotEmpty(params.getCounty())) {
            supplier.setNation(areaMap.getOrDefault(String.format("%s_%s", "country", params.getCounty()), new SupplierArea()).getId());
        }

        complete = true;
    }

    public List<SupplierContactsPerson> getSupplierContactsPersonList() {
        if (!complete)
            throw new RuntimeException("broke state");
        return supplierContactsPersonList;
    }

    public List<SupplierSendAddress> getSupplierSendAddresses() {
        if (!complete)
            throw new RuntimeException("broke state");
        return supplierSendAddresses;
    }

    public Map<String, Object> getCustomizeDataDtos() {
        if (!complete)
            throw new RuntimeException("broke state");
        return customizeDataDtos;
    }
}
