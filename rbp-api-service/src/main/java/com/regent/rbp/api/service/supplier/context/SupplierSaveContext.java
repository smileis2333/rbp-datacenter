package com.regent.rbp.api.service.supplier.context;

import cn.hutool.core.bean.BeanUtil;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.supplier.Supplier;
import com.regent.rbp.api.core.supplier.SupplierContactsPerson;
import com.regent.rbp.api.core.supplier.SupplierGrade;
import com.regent.rbp.api.core.supplier.SupplierNature;
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
//    private List<SupplierSendAddress> supplierSendAddresses = new ArrayList<>(); //todo
    private Map<String, Object> customizeDataDtos = new HashMap<>();
    public final SupplierSaveParam params;
    private boolean complete;

    // support check
    public final String nature;
    public final String grade;
    public final String fundAccountCode;

    public SupplierSaveContext(SupplierSaveParam param) {
        params = param;
        supplier = new Supplier();
        BeanUtil.copyProperties(param.getPhysicalRegion(), supplier);
        BeanUtil.copyProperties(param, supplier);
        supplier.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        supplier.setCode(param.getSupplierCode());

        nature = param.getNature();
        grade = param.getGrade();
        fundAccountCode = param.getFundAccountCode();
    }

    public void addContactsPersonList(ContactData contactsPerson) {
        SupplierContactsPerson item = BeanUtil.copyProperties(contactsPerson, SupplierContactsPerson.class);
        item.setSupplierId(supplier.getId());
        supplierContactsPersonList.add(item);
    }

//    public void addSendAddress(AddressData addressData) {
//        SupplierSendAddress item = BeanUtil.copyProperties(addressData, SupplierSendAddress.class);
//        item.setSupplierId(supplier.getId());
//        supplierSendAddresses.add(item);
//    }

    public void complete(SupplierNature nameNature, SupplierGrade nameGrade, FundAccount codeFundAccount) {
        if (nameNature != null) supplier.setNatureId(nameNature.getId());
        if (nameGrade != null) supplier.setGradeId(nameGrade.getId());
        if (codeFundAccount != null) supplier.setFundAccountId(codeFundAccount.getId());
        params.getContactsPerson().forEach(this::addContactsPersonList);
//        params.getAddressData().forEach(this::addSendAddress);
        params.getCustomizeData().forEach(e -> customizeDataDtos.put(e.getCode(), e.getValue()));
        complete = true;
    }

    public List<SupplierContactsPerson> getSupplierContactsPersonList() {
        if (!complete)
            throw new RuntimeException("broke state");
        return supplierContactsPersonList;
    }

//    public List<SupplierSendAddress> getSupplierSendAddresses() {
//        if (!complete)
//            throw new RuntimeException("broke state");
//        return supplierSendAddresses;
//    }

    public Map<String, Object> getCustomizeDataDtos() {
        if (!complete)
            throw new RuntimeException("broke state");
        return customizeDataDtos;
    }
}
