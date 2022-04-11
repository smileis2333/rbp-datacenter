package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto;

public class OtherStockinRequest
{

	private String startTime;
	private String endTime;
	private Integer status;
	private String warehouseNo;
	private String stockinNo;
	private String otherStockinNo;

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getWarehouseNo()
	{
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo)
	{
		this.warehouseNo = warehouseNo;
	}

	public String getStockinNo()
	{
		return stockinNo;
	}

	public void setStockinNo(String stockinNo)
	{
		this.stockinNo = stockinNo;
	}

	public String getOtherStockinNo()
	{
		return otherStockinNo;
	}

	public void setOtherStockinNo(String otherStockinNo)
	{
		this.otherStockinNo = otherStockinNo;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer string)
	{
		this.status = string;
	}
}
