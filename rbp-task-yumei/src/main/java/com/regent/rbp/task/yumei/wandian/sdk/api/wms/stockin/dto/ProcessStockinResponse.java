package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class ProcessStockinResponse {
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
		private String logisticsNo;
		private String created;
		private int srcOrderType;
		private int goodsTypeCount;
		private String remark;
		private BigDecimal goodsCount;
		private String operatorName;
		private String warehouseName;
		private String producerName;
		private String stockinNo;
		private String modified;
		private String processNo;
		private BigDecimal rightNum;
		private int noteCount;
		private int status;
		private String checkTime;
		@SerializedName("detail_list")
        private List<OrderDetailInfoDto> detailList;
		public String getLogisticsName() {
			return logisticsName;
		}
		public void setLogisticsName(String logisticsName) {
			this.logisticsName = logisticsName;
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
		public int getSrcOrderType() {
			return srcOrderType;
		}
		public void setSrcOrderType(int srcOrderType) {
			this.srcOrderType = srcOrderType;
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
		public String getOperatorName() {
			return operatorName;
		}
		public void setOperatorName(String operatorName) {
			this.operatorName = operatorName;
		}
		public String getWarehouseName() {
			return warehouseName;
		}
		public void setWarehouseName(String warehouseName) {
			this.warehouseName = warehouseName;
		}
		public String getProducerName() {
			return producerName;
		}
		public void setProducerName(String producerName) {
			this.producerName = producerName;
		}
		public String getStockinNo() {
			return stockinNo;
		}
		public void setStockinNo(String stockinNo) {
			this.stockinNo = stockinNo;
		}
		public String getModified() {
			return modified;
		}
		public void setModified(String modified) {
			this.modified = modified;
		}
		public String getProcessNo() {
			return processNo;
		}
		public void setProcessNo(String processNo) {
			this.processNo = processNo;
		}
		public BigDecimal getRightNum() {
			return rightNum;
		}
		public void setRightNum(BigDecimal rightNum) {
			this.rightNum = rightNum;
		}
		public int getNoteCount() {
			return noteCount;
		}
		public void setNoteCount(int noteCount) {
			this.noteCount = noteCount;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getCheckTime() {
			return checkTime;
		}
		public void setCheckTime(String checkTime) {
			this.checkTime = checkTime;
		}
		public List<OrderDetailInfoDto> getDetailList() {
			return detailList;
		}
		public void setDetailList(List<OrderDetailInfoDto> detailList) {
			this.detailList = detailList;
		}
	}
    
    public static class OrderDetailInfoDto{
    	private String goodsName;
    	private String shortName;
    	private String goodsNo;
    	private String specCode;
    	private String specName;
    	private String specNo;
    	private String barcode;
    	private BigDecimal num;
    	private BigDecimal num2;
    	private String positionNo;
    	private BigDecimal expectNum;
    	private String remark;
    	private BigDecimal weight;
    	private BigDecimal goodsWeight;
    	private boolean defect;
    	private BigDecimal unitRatio;
    	private int validityDays;
    	private BigDecimal needInspectNum;
    	private String brandName;
    	private String auxUnitName;
    	private String baseUnitName;
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
		public String getGoodsNo() {
			return goodsNo;
		}
		public void setGoodsNo(String goodsNo) {
			this.goodsNo = goodsNo;
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
		public String getSpecNo() {
			return specNo;
		}
		public void setSpecNo(String specNo) {
			this.specNo = specNo;
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
		public BigDecimal getNum2() {
			return num2;
		}
		public void setNum2(BigDecimal num2) {
			this.num2 = num2;
		}
		public String getPositionNo() {
			return positionNo;
		}
		public void setPositionNo(String positionNo) {
			this.positionNo = positionNo;
		}
		public BigDecimal getExpectNum() {
			return expectNum;
		}
		public void setExpectNum(BigDecimal expectNum) {
			this.expectNum = expectNum;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public BigDecimal getWeight() {
			return weight;
		}
		public void setWeight(BigDecimal weight) {
			this.weight = weight;
		}
		public BigDecimal getGoodsWeight() {
			return goodsWeight;
		}
		public void setGoodsWeight(BigDecimal goodsWeight) {
			this.goodsWeight = goodsWeight;
		}
		public boolean isDefect() {
			return defect;
		}
		public void setDefect(boolean defect) {
			this.defect = defect;
		}
		public BigDecimal getUnitRatio() {
			return unitRatio;
		}
		public void setUnitRatio(BigDecimal unitRatio) {
			this.unitRatio = unitRatio;
		}
		public int getValidityDays() {
			return validityDays;
		}
		public void setValidityDays(int validityDays) {
			this.validityDays = validityDays;
		}
		public BigDecimal getNeedInspectNum() {
			return needInspectNum;
		}
		public void setNeedInspectNum(BigDecimal needInspectNum) {
			this.needInspectNum = needInspectNum;
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
