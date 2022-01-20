package com.regent.rbp.api.service.box.context;

import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/01/19
 * @description
 */
@Data
public class BoxQueryContext {
    private List<Long> boxIds;
    private List<String> manualId;
    private List<String> batchNumber;
    private List<Long> channelIds;
    private List<Long> supplierIds;
    private List<Long> distributionTypeIds;
    private Integer type;
    private List<Integer> status;
    private List<Long> goodsIds;
    private String fields;
    private Integer pageNo;
    private Integer pageSize;
}
