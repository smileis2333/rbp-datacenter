package com.regent.rbp.api.service.receive.context;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.core.receiveBill.*;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2021/12/17
 * @description
 */
@Data
public class ReceiveBillSaveContext {
    private ReceiveBill bill;
    private List<ReceiveBillRealGoods> billRealGoodsList;
    private List<ReceiveBillRealSize> billRealSizeList;
    private List<ReceiveBillGoods> billGoodsList;
    private List<ReceiveBillSize> billSizeList;
    private List<CustomizeDataDto> billCustomizeDataDtos;
    private List<Map<String, Object>> receiveGoodsCustomizeData = new ArrayList<>();
    private Long baseBusinessTypeId;

    public void addGoodsDetailCustomData(List<CustomizeDataDto> customizeDataDto, Long billGoodsId) {
        if (CollUtil.isEmpty(customizeDataDto)){
            return;
        }
        HashMap<String, Object> ele = new HashMap<>();
        customizeDataDto.forEach(e -> {
            ele.put(e.getCode(), e.getValue());
        });
        ele.put("id", billGoodsId);
        receiveGoodsCustomizeData.add(ele);
    }

    public List<Map<String, Object>> getReceiveGoodsCustomizeData() {
        return receiveGoodsCustomizeData;
    }
}
