package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

import java.util.List;

public class StockSearchRequest {

    public static final byte MASK_SALES = 1;
    public static final byte MASK_COST = 2;

    private List<String> specNos;
    private String warehouseNo;
    private String startTime;
    private String endTime;
    private int mask = 0;

    public List<String> getSpecNos() {
        return specNos;
    }

    public void setSpecNos(List<String> specNos) {
        this.specNos = specNos;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }

}
