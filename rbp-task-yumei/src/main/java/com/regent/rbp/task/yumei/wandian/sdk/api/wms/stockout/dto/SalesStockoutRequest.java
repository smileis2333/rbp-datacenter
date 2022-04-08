package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto;

import com.google.gson.annotations.SerializedName;

public class SalesStockoutRequest {

    public static final Byte STATUS_TYPE_CANCEL = 1;
    public static final Byte STATUS_TYPE_PROCESS = 2;
    public static final Byte STATUS_TYPE_CONSIGNED = 3; // 时间条件为发货时间,状态为其他时,为最后修改时间

    private String startTime ;
    private String endTime ;
    private Byte statusType ;
    private String warehouseNo ;
    private String stockoutNo ;
    private String srcOrderNo ;
    @SerializedName("position")
    private Boolean positionSort ;

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

    public Byte getStatusType() {
        return statusType;
    }

    public void setStatusType(Byte statusType) {
        this.statusType = statusType;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getStockoutNo() {
        return stockoutNo;
    }

    public void setStockoutNo(String stockoutNo) {
        this.stockoutNo = stockoutNo;
    }

    public String getSrcOrderNo() {
        return srcOrderNo;
    }

    public void setSrcOrderNo(String srcOrderNo) {
        this.srcOrderNo = srcOrderNo;
    }

    public Boolean getPositionSort() {
        return positionSort;
    }

    public void setPositionSort(Boolean positionSort) {
        this.positionSort = positionSort;
    }
}
