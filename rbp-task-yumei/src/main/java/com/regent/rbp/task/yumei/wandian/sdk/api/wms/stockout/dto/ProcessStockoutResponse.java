package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class ProcessStockoutResponse {

	/*
{
    "status": 0,
    "data": {
        "total_count": 1,
        "order": [
            {
                "logistics_name": "无",
                "freeze_reason": 0,
                "consign_time": 1592876325000,
                "error_info": "",
                "detail_list": [
                    {
                        "spec_no": "23441",
                        "goods_no": "zzqdpg",
                        "goods_name": "张钟墙的苹果",
                        "short_name": "苹果",
                        "spec_code": "",
                        "spec_name": "111",
                        "barcode": "1111",
                        "num": 5,
                        "scan_type": 0,
                        "defect": false,
                        "remark": "",
                        "expect_num": 5,
                        "checked_cost_price": 20,
                        "brand_name": "无",
                        "aux_unit_name": "无",
                        "base_unit_name": "无"
                    },
                    {
                        "spec_no": "mkb-yellow",
                        "goods_no": "23423231",
                        "goods_name": "马克杯",
                        "short_name": "",
                        "spec_code": "-yellow",
                        "spec_name": "-yellow",
                        "barcode": "mkb-yellow",
                        "num": 5,
                        "scan_type": 0,
                        "defect": false,
                        "remark": "",
                        "expect_num": 5,
                        "checked_cost_price": 36,
                        "brand_name": "无",
                        "aux_unit_name": "无",
                        "base_unit_name": "无"
                    }
                ],
                "goods_type_count": 2,
                "remark": "",
                "goods_count": 10,
                "checked_goods_total_cost": 280,
                "modified": 1592876328000,
                "note_count": 0,
                "src_order_id": 268,
                "logistics_no": "",
                "created": 1592817150000,
                "stockout_no": "CK2020062208",
                "src_order_type": 5,
                "operator_name": "小二y",
                "weigh_post_cost": 0,
                "warehouse_name": "巫妖的仓库",
                "checkouter_name": "系统",
                "process_no": "PS2020062202",
                "status": 110
            }
        ]
    }
}
	 */
	@SerializedName("total_count")
    private Integer total;
	@SerializedName("order")
    private List<OrderInfoDto> orders;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<OrderInfoDto> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderInfoDto> orders) {
		this.orders = orders;
	}
	
	public static class OrderInfoDto {
		private String logisticsName;
		private String consignTime;
		private String errorInfo;
		private int goodsTypeCount;
		private String remark;
		private BigDecimal goodsCount;
		private BigDecimal checkedGoodsTotalCost;
		private String modified;
		private int noteCount;
		private String logisticsNo;
		private String created;
		private String stockoutNo;
		private int srcOrderType;
		private String operatorName;
		private BigDecimal weighPostCost;
		private String warehouseName;
		private String checkouterName;
		private String processNo;
		private int status;
		@SerializedName("detail_list")
        private List<OrderDetailInfoDto> detailList;
		public String getLogisticsName() {
			return logisticsName;
		}
		public void setLogisticsName(String logisticsName) {
			this.logisticsName = logisticsName;
		}
		public String getConsignTime() {
			return consignTime;
		}
		public void setConsignTime(String consignTime) {
			this.consignTime = consignTime;
		}
		public String getErrorInfo() {
			return errorInfo;
		}
		public void setErrorInfo(String errorInfo) {
			this.errorInfo = errorInfo;
		}
		public int getGoodsTypeCount() {
			return goodsTypeCount;
		}
		public void setGoodsTypeCount(int goodsTypeCount) {
			this.goodsTypeCount = goodsTypeCount;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public BigDecimal getGoodsCount() {
			return goodsCount;
		}
		public void setGoodsCount(BigDecimal goodsCount) {
			this.goodsCount = goodsCount;
		}
		public BigDecimal getCheckedGoodsTotalCost() {
			return checkedGoodsTotalCost;
		}
		public void setCheckedGoodsTotalCost(BigDecimal checkedGoodsTotalCost) {
			this.checkedGoodsTotalCost = checkedGoodsTotalCost;
		}
		public String getModified() {
			return modified;
		}
		public void setModified(String modified) {
			this.modified = modified;
		}
		public int getNoteCount() {
			return noteCount;
		}
		public void setNoteCount(int noteCount) {
			this.noteCount = noteCount;
		}
		public String getLogisticsNo() {
			return logisticsNo;
		}
		public void setLogisticsNo(String logisticsNo) {
			this.logisticsNo = logisticsNo;
		}
		public String getCreated() {
			return created;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public String getStockoutNo() {
			return stockoutNo;
		}
		public void setStockoutNo(String stockoutNo) {
			this.stockoutNo = stockoutNo;
		}
		public int getSrcOrderType() {
			return srcOrderType;
		}
		public void setSrcOrderType(int srcOrderType) {
			this.srcOrderType = srcOrderType;
		}
		public String getOperatorName() {
			return operatorName;
		}
		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}
		public BigDecimal getWeighPostCost() {
			return weighPostCost;
		}
		public void setWeighPostCost(BigDecimal weighPostCost) {
			this.weighPostCost = weighPostCost;
		}
		public String getWarehouseName() {
			return warehouseName;
		}
		public void setWarehouseName(String warehouseName) {
			this.warehouseName = warehouseName;
		}
		public String getCheckouterName() {
			return checkouterName;
		}
		public void setCheckouterName(String checkouterName) {
			this.checkouterName = checkouterName;
		}
		public String getProcessNo() {
			return processNo;
		}
		public void setProcessNo(String processNo) {
			this.processNo = processNo;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public List<OrderDetailInfoDto> getDetailList() {
			return detailList;
		}
		public void setDetailList(List<OrderDetailInfoDto> detailList) {
			this.detailList = detailList;
		}
	}
	
	public static class OrderDetailInfoDto{
		private String specNo;
		private String goodsNo;
		private String goodsName;
		private String shortName;
		private String specCode;
		private String specName;
		private String barcode;
		private BigDecimal num;
		private int scanType;
		private boolean defect;
		private String expireDate;
		private String remark;
		private String positionNo;
		private String batchNo;
		private BigDecimal expectNum;
		private BigDecimal chectedCostPrice;
		private String brandName;
		private String auxUnitName;
		private String baseUnitName;
		
		public String getSpecNo() {
			return specNo;
		}
		public void setSpecNo(String specNo) {
			this.specNo = specNo;
		}
		public String getGoodsNo() {
			return goodsNo;
		}
		public void setGoodsNo(String goodsNo) {
			this.goodsNo = goodsNo;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getShortName() {
			return shortName;
		}
		public void setShortName(String shortName) {
			this.shortName = shortName;
		}
		public String getSpecCode() {
			return specCode;
		}
		public void setSpecCode(String specCode) {
			this.specCode = specCode;
		}
		public String getSpecName() {
			return specName;
		}
		public void setSpecName(String specName) {
			this.specName = specName;
		}
		public String getBarcode() {
			return barcode;
		}
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}
		public BigDecimal getNum() {
			return num;
		}
		public void setNum(BigDecimal num) {
			this.num = num;
		}
		public int getScanType() {
			return scanType;
		}
		public void setScanType(int scanType) {
			this.scanType = scanType;
		}
		public boolean isDefect() {
			return defect;
		}
		public void setDefect(boolean defect) {
			this.defect = defect;
		}
		public String getExpireDate() {
			return expireDate;
		}
		public void setExpireDate(String expireDate) {
			this.expireDate = expireDate;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getPositionNo() {
			return positionNo;
		}
		public void setPositionNo(String positionNo) {
			this.positionNo = positionNo;
		}
		public String getBatchNo() {
			return batchNo;
		}
		public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}
		public BigDecimal getExpectNum() {
			return expectNum;
		}
		public void setExpectNum(BigDecimal expectNum) {
			this.expectNum = expectNum;
		}
		public BigDecimal getChectedCostPrice() {
			return chectedCostPrice;
		}
		public void setChectedCostPrice(BigDecimal chectedCostPrice) {
			this.chectedCostPrice = chectedCostPrice;
		}
		public String getBrandName() {
			return brandName;
		}
		public void setBrandName(String brandName) {
			this.brandName = brandName;
		}
		public String getAuxUnitName() {
			return auxUnitName;
		}
		public void setAuxUnitName(String auxUnitName) {
			this.auxUnitName = auxUnitName;
		}
		public String getBaseUnitName() {
			return baseUnitName;
		}
		public void setBaseUnitName(String baseUnitName) {
			this.baseUnitName = baseUnitName;
		}
	}
}
