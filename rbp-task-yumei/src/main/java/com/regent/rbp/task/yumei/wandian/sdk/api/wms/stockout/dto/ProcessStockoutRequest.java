package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto;

public class ProcessStockoutRequest {
	
	private String stockoutNo;
	private String processNo;
	private String warehouseNo;
	private String status;
	private String startTime ;
    private String endTime ;
    private int timeType;
    
	public String getStockoutNo() {
		return stockoutNo;
	}
	public void setStockoutNo(String stockoutNo) {
		this.stockoutNo = stockoutNo;
	}
	public String getProcessNo() {
		return processNo;
	}
	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}
	public String getWarehouseNo() {
		return warehouseNo;
	}
	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	
}
