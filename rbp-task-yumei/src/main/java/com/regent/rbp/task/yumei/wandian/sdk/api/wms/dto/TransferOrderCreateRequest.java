package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

import java.util.List;

public class TransferOrderCreateRequest
{

	private boolean isCheck;
	private List<orderInfoDto> orderInfo;
	private List<detailDto> detailList;

	public boolean isCheck()
	{
		return isCheck;
	}

	public void setCheck(boolean isCheck)
	{
		this.isCheck = isCheck;
	}

	public List<orderInfoDto> getOrderInfo()
	{
		return orderInfo;
	}

	public void setOrderInfo(List<orderInfoDto> orderInfo)
	{
		this.orderInfo = orderInfo;
	}

	public List<detailDto> getDetailList()
	{
		return detailList;
	}

	public void setDetailList(List<detailDto> detailList)
	{
		this.detailList = detailList;
	}

	public static class orderInfoDto
	{

		private String outerNo;
		private String fromWarehouseNo;
		private String toWarehouseNo;
		private Integer mode;
		private String remark;

		public String getOuterNo()
		{
			return outerNo;
		}

		public void setOuterNo(String outerNo)
		{
			this.outerNo = outerNo;
		}

		public String getFromWarehouseNo()
		{
			return fromWarehouseNo;
		}

		public void setFromWarehouseNo(String fromWarehouseNo)
		{
			this.fromWarehouseNo = fromWarehouseNo;
		}

		public String getToWarehouseNo()
		{
			return toWarehouseNo;
		}

		public void setToWarehouseNo(String toWarehouseNo)
		{
			this.toWarehouseNo = toWarehouseNo;
		}

		public Integer getMode()
		{
			return mode;
		}

		public void setMode(Integer mode)
		{
			this.mode = mode;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
		}

	}

	public static class detailDto
	{

		private String specNo;
		private String num;
		private String fromPositionNo;
		private Boolean toPositionNo;

		public String getSpecNo()
		{
			return specNo;
		}

		public void setSpecNo(String specNo)
		{
			this.specNo = specNo;
		}

		public String getNum()
		{
			return num;
		}

		public void setNum(String num)
		{
			this.num = num;
		}

		public String getFromPositionNo()
		{
			return fromPositionNo;
		}

		public void setFromPositionNo(String fromPositionNo)
		{
			this.fromPositionNo = fromPositionNo;
		}

		public Boolean getToPositionNo()
		{
			return toPositionNo;
		}

		public void setToPositionNo(Boolean toPositionNo)
		{
			this.toPositionNo = toPositionNo;
		}
	}

}
