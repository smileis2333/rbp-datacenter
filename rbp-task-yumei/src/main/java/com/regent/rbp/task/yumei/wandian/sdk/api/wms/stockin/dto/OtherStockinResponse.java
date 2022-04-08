package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class OtherStockinResponse {
	/*
{
    "status": 0,
    "data": {
        "total_count": 1,
        "order": [
            {
                "logistics_name": "无",
                "logistics_no": "",
                "created": 1592876966000,
                "detail_list": [
                    {
                        "goods_name": "sss的辣条",
                        "short_name": "",
                        "goods_no": "ES1011479",
                        "spec_code": "",
                        "spec_name": "sss的辣条:",
                        "spec_no": "BM20200519158",
                        "barcode": "",
                        "num": 4,
                        "num2": 4,
                        "position_no": "其它未上架",
                        "expect_num": 4,
                        "remark": "",
                        "weight": 0,
                        "goods_weight": 0,
                        "defect": false,
                        "unit_ratio": 1,
                        "validity_days": 0,
                        "need_inspect_num": 0,
                        "brand_name": "无",
                        "aux_unit_name": "无",
                        "base_unit_name": "无"
                    }
                ],
                "src_order_type": 6,
                "goods_type_count": 1,
                "remark": "",
                "goods_count": 4,
                "operator_name": "小二y",
                "warehouse_name": "巫妖的仓库",
                "producer_name": "【震惊！！】无人生产商",
                "stockin_no": "RK2006230002",
                "modified": 1592876982000,
                "process_no": "PS2020062202",
                "right_num": 4,
                "note_count": 0,
                "status": 80,
                "check_time": 1592876980000
            }
        ]
    }
}
	 */
	
	@SerializedName("status")
	private Integer status;
	@SerializedName("message")
	private String message;
	@SerializedName("data")
	private List<DataInfoDto> data;

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public List<DataInfoDto> getData()
	{
		return data;
	}

	public void setOrders(List<DataInfoDto> data)
	{
		this.data = data;
	}

	public static class DataInfoDto
	{
		private List<OrderInfoDto> order;
		private Integer totalCount;

		public List<OrderInfoDto> getOrder()
		{
			return order;
		}

		public void setOrder(List<OrderInfoDto> order)
		{
			this.order = order;
		}

		public Integer getTotalCount()
		{
			return totalCount;
		}

		public void setTotalCount(Integer totalCount)
		{
			this.totalCount = totalCount;
		}
	}

	public static class OrderInfoDto
	{

		private String stockinId;
		private String orderNo;
		private String warehouseNo;
		private Integer status;
		private String message;
		private String warehouseName;
		private String stockinTime;
		private String createdTime;
		private String reason;
		private String remark;
		private BigDecimal goodsCount;
		private Integer logisticsType;
		private String checkTime;
		private String srcOrderNo;
		private String operatorName;
		private BigDecimal totalPrice;

		@SerializedName("detail_list")
		private List<OrderDetailInfoDto> detailList;

		public String getStockinId()
		{
			return stockinId;
		}

		public void setStockinId(String stockinId)
		{
			this.stockinId = stockinId;
		}

		public String getOrderNo()
		{
			return orderNo;
		}

		public void setOrderNo(String orderNo)
		{
			this.orderNo = orderNo;
		}

		public String getWarehouseNo()
		{
			return warehouseNo;
		}

		public void setWarehouseNo(String warehouseNo)
		{
			this.warehouseNo = warehouseNo;
		}

		public Integer getStatus()
		{
			return status;
		}

		public void setStatus(Integer status)
		{
			this.status = status;
		}

		public String getMessage()
		{
			return message;
		}

		public void setMessage(String message)
		{
			this.message = message;
		}

		public String getWarehouseName()
		{
			return warehouseName;
		}

		public void setWarehouseName(String warehouseName)
		{
			this.warehouseName = warehouseName;
		}

		public String getStockinTime()
		{
			return stockinTime;
		}

		public void setStockinTime(String stockinTime)
		{
			this.stockinTime = stockinTime;
		}

		public String getCreatedTime()
		{
			return createdTime;
		}

		public void setCreatedTime(String createdTime)
		{
			this.createdTime = createdTime;
		}

		public String getReason()
		{
			return reason;
		}

		public void setReason(String reason)
		{
			this.reason = reason;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
		}

		public BigDecimal getGoodsCount()
		{
			return goodsCount;
		}

		public void setGoodsCount(BigDecimal goodsCount)
		{
			this.goodsCount = goodsCount;
		}

		public Integer getLogisticsType()
		{
			return logisticsType;
		}

		public void setLogisticsType(Integer logisticsType)
		{
			this.logisticsType = logisticsType;
		}

		public String getCheckTime()
		{
			return checkTime;
		}

		public void setCheckTime(String checkTime)
		{
			this.checkTime = checkTime;
		}

		public String getSrcOrderNo()
		{
			return srcOrderNo;
		}

		public void setSrcOrderNo(String srcOrderNo)
		{
			this.srcOrderNo = srcOrderNo;
		}

		public String getOperatorName()
		{
			return operatorName;
		}

		public void setOperatorName(String operatorName)
		{
			this.operatorName = operatorName;
		}

		public BigDecimal getTotalPrice()
		{
			return totalPrice;
		}

		public void setTotalPrice(BigDecimal totalPrice)
		{
			this.totalPrice = totalPrice;
		}

	}

	public static class OrderDetailInfoDto
	{
		private String stockinId;
		private BigDecimal goodsCount;
		private BigDecimal totalCost;
		private String remark;

		private BigDecimal rightNum;
		private String goodsUnit;
		private String batchNo;
		private Integer recId;
		private String goodsName;
		private String goodsNo;
		private String specNo;
		private String prop2;
		private String specName;
		private String specCode;
		private String brandNo;
		private String brandName;

		public String getStockinId()
		{
			return stockinId;
		}

		public void setStockinId(String stockinId)
		{
			this.stockinId = stockinId;
		}

		public BigDecimal getGoodsCount()
		{
			return goodsCount;
		}

		public void setGoodsCount(BigDecimal goodsCount)
		{
			this.goodsCount = goodsCount;
		}

		public BigDecimal getTotalCost()
		{
			return totalCost;
		}

		public void setTotalCost(BigDecimal totalCost)
		{
			this.totalCost = totalCost;
		}

		public String getRemark()
		{
			return remark;
		}

		public void setRemark(String remark)
		{
			this.remark = remark;
		}

		public BigDecimal getRightNum()
		{
			return rightNum;
		}

		public void setRightNum(BigDecimal rightNum)
		{
			this.rightNum = rightNum;
		}

		public String getGoodsUnit()
		{
			return goodsUnit;
		}

		public void setGoodsUnit(String goodsUnit)
		{
			this.goodsUnit = goodsUnit;
		}

		public String getBatchNo()
		{
			return batchNo;
		}

		public void setBatchNo(String batchNo)
		{
			this.batchNo = batchNo;
		}

		public Integer getRecId()
		{
			return recId;
		}

		public void setRecId(Integer recId)
		{
			this.recId = recId;
		}

		public String getGoodsName()
		{
			return goodsName;
		}

		public void setGoodsName(String goodsName)
		{
			this.goodsName = goodsName;
		}

		public String getGoodsNo()
		{
			return goodsNo;
		}

		public void setGoodsNo(String goodsNo)
		{
			this.goodsNo = goodsNo;
		}

		public String getProp2()
		{
			return prop2;
		}

		public void setProp2(String prop2)
		{
			this.prop2 = prop2;
		}

		public String getSpecNo()
		{
			return specNo;
		}

		public void setSpecNo(String specNo)
		{
			this.specNo = specNo;
		}

		public String getSpecName()
		{
			return specName;
		}

		public void setSpecName(String specName)
		{
			this.specName = specName;
		}

		public String getSpecCode()
		{
			return specCode;
		}

		public void setSpecCode(String specCode)
		{
			this.specCode = specCode;
		}

		public String getBrandNo()
		{
			return brandNo;
		}

		public void setBrandNo(String brandNo)
		{
			this.brandNo = brandNo;
		}

		public String getBrandName()
		{
			return brandName;
		}

		public void setBrandName(String brandName)
		{
			this.brandName = brandName;
		}

	}

}
