package com.regent.rbp.api.service.stock.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.stock.StockAdjustBill;
import com.regent.rbp.api.core.stock.StockAdjustBillGoods;
import com.regent.rbp.api.core.stock.StockAdjustBillSize;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/04/28
 * @description
 */
@Data
public class StockAdjustBillSaveContext {
    private StockAdjustBill bill;

    private List<StockAdjustBillGoods> billGoods = new ArrayList<>();

    private List<StockAdjustBillSize> billSizes = new ArrayList<>();

    private List<CustomizeDataDto> billCustomizeData;

    private List<Map<String, Object>> goodsCustomizeData = new ArrayList<>();

    public void addBillGoods(StockAdjustBillGoods billGoods) {
        if (billGoods != null) {
            billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            billGoods.setBillId(bill.getId());
            this.billGoods.add(billGoods);
        }
    }

    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        if (CollUtil.isNotEmpty(customizeDataDto)) {
            HashMap<String, Object> ele = new HashMap<>();
            customizeDataDto.forEach(e -> {
                ele.put(e.getCode(), e.getValue());
            });
            ele.put("id", billGoodsId);
            goodsCustomizeData.add(ele);
        }
    }

    public void addBillSize(StockAdjustBillSize billSize) {
        billSize.setBillId(bill.getId());
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSizes.add(billSize);
    }
}
