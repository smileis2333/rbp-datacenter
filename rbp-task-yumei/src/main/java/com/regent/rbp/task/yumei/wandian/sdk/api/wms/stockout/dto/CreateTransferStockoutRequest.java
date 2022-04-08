package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto;

import java.util.List;

public class CreateTransferStockoutRequest
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
		private String srcOrderNo;
		private String warehouseNo;
		private String logisticsCode;
		private String remark;

		public String getSrcOrderNo()
		{
			return srcOrderNo;
		}

		public void setSrcOrderNo(String srcOrderNo)
		{
			this.srcOrderNo = srcOrderNo;
		}

		public String getWarehouseNo()
		{
			return warehouseNo;
		}

		public void setWarehouseNo(String warehouseNo)
		{
			this.warehouseNo = warehouseNo;
		}

		public String getLogisticsCode()
		{
			return logisticsCode;
		}

		public void setLogisticsCode(String logisticsCode)
		{
			this.logisticsCode = logisticsCode;
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
		private String unitName;
		private String positionNo;
		private Boolean defect;
		private Boolean remark;

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

		public String getUnitName()
		{
			return unitName;
		}

		public void setUnitName(String unitName)
		{
			this.unitName = unitName;
		}

		public String getPositionNo()
		{
			return positionNo;
		}

		public void setPositionNo(String positionNo)
		{
			this.positionNo = positionNo;
		}

		public Boolean getDefect()
		{
			return defect;
		}

		public void setDefect(Boolean defect)
		{
			this.defect = defect;
		}

		public Boolean getRemark()
		{
			return remark;
		}

		public void setRemark(Boolean remark)
		{
			this.remark = remark;
		}

	}

}
