package com.regent.rbp.api.service.sale.context;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.salePlan.SalePlanBill;
import com.regent.rbp.api.core.salePlan.SalePlanBillGoods;
import com.regent.rbp.api.core.salePlan.SalePlanBillLogistics;
import com.regent.rbp.api.core.salePlan.SalePlanBillSize;
import com.regent.rbp.api.dto.sale.SalePlanSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class SalesPlanSaveContext {
    // state info
    private SalePlanBill salePlanBill;
    private List<SalePlanBillGoods> salePlanBillGoodsList = new ArrayList<>();
    private List<SalePlanBillSize> salePlanBillSizeList = new ArrayList<>();
    private SalePlanBillLogistics salePlanBillLogistics;
    private Map<String, Object> customizeData;

    // -------- support check
    private String businessType;
    private String channelCode;
    private String priceType;
    private String currencyType;
    private String logisticsCompanyCode;
    private SalePlanSaveParam param;


    public SalesPlanSaveContext() {
        this(null);
    }

    public SalesPlanSaveContext(SalePlanSaveParam param) {
        this.salePlanBill = new SalePlanBill();
        Long userId = ThreadLocalGroup.getUserId();
        this.salePlanBill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.salePlanBill.setCreatedBy(userId);
        this.salePlanBill.setUpdatedBy(userId);
        this.salePlanBill.setProcessStatus(0);
        BeanUtil.copyProperties(param, this.salePlanBill);

        this.salePlanBillGoodsList = new ArrayList<>();

        this.salePlanBillLogistics = new SalePlanBillLogistics();
        this.salePlanBillLogistics.setBillId(salePlanBill.getId());
        this.salePlanBillLogistics.setCreatedBy(userId);
        this.salePlanBillLogistics.setUpdatedBy(userId);
        this.salePlanBillLogistics.setNotes(param.getLogisticsNotes());

        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            customizeData = customFieldMap;
            customFieldMap.put("id", salePlanBill.getId());
        }

        businessType = param.getBusinessType();
        channelCode = param.getChannelCode();
        priceType = param.getPriceType();
        currencyType = param.getCurrencyType();
        logisticsCompanyCode = param.getLogisticsCompanyCode();

    }

    public void addSalePlanBillSize(SalePlanBillSize salePlanBillSize) {
        this.salePlanBillSizeList.add(salePlanBillSize);
    }

    public void addSalePlanBillSize(List<SalePlanBillSize> salePlanBillSizeList) {
        this.salePlanBillSizeList.addAll(salePlanBillSizeList);
    }

    public void addSalePlanBillGoods(SalePlanBillGoods salePlanBillGoods) {
        this.salePlanBillGoodsList.add(salePlanBillGoods);
    }

    public void addSalePlanBillGoods(List<SalePlanBillGoods> salePlanBillGoodsList) {
        this.salePlanBillGoodsList.addAll(salePlanBillGoodsList);
    }
}
