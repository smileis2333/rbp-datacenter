package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreateOtherStockinRequest
{
	private String outerNo;
	private String warehouseNo;
	private String logisticsNo;
	private String logisticsCode;
	private Boolean isCheck;
	private List<GoodsList> goodsList;
	private String remark;
	private String reason;

	public static class GoodsList
	{

		private String specNo;
		private BigDecimal num;
		private String remark;
		private String batchNo;
		private String positionNo;
		private String productionDate;
		private String expireDate;

		public String getSpecNo()
		{
			return specNo;
		}

		public void setSpecNo(String specNo)
		{
			this.specNo = specNo;
		}

		public BigDecimal getNum()
		{
			return num;
		}

		public void setNum(BigDecimal num)
		{
			this.num = num;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
		}

		public String getBatchNo()
		{
			return batchNo;
		}

		public void setBatchNo(String batchNo)
		{
			this.batchNo = batchNo;
		}

		public String getPositionNo()
		{
			return positionNo;
		}

		public void setPositionNo(String positionNo)
		{
			this.positionNo = positionNo;
		}

		public String getProductionDate()
		{
			return productionDate;
		}

		public void setProductionDate(String productionDate)
		{
			this.productionDate = productionDate;
		}

		public String getExpireDate()
		{
			return expireDate;
		}

		public void setExpireDate(String expireDate)
		{
			this.expireDate = expireDate;
		}

	}

	public String getOuterNo()
	{
		return outerNo;
	}

	public void setOuterNo(String outerNo)
	{
		this.outerNo = outerNo;
	}

	public String getWarehouseNo()
	{
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo)
	{
		this.warehouseNo = warehouseNo;
	}

	public String getLogisticsNo()
	{
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo)
	{
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsCode()
	{
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode)
	{
		this.logisticsCode = logisticsCode;
	}

	public Boolean getisCheck()
	{
		return isCheck;
	}

	public void setisCheck(Boolean check)
	{
		isCheck = check;
	}

	public List<GoodsList> getGoodsList()
	{
		return goodsList;
	}

	public void setGoodsList(List<GoodsList> goodsList)
	{
		this.goodsList = goodsList;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}
}
