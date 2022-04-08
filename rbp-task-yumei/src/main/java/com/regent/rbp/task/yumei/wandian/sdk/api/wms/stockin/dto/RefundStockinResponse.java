package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class RefundStockinResponse {
    /*
{
	"status": 0,
	"data": {
		"total_count": 66,
		"order": [{
			"order_no": "RK1909180009",
			"details_list": [{
				"stockin_id": 1587,
				"num": 1.0000,
				"src_price": 5.0000,
				"price": 5.0000,
				"total_cost": 2.0000,
				"remark": "",
				"right_num": 1.0000,
				"rec_id": 2232,
				"goods_name": "张钟墙的苹果",
				"goods_no": "zzqdpg",
				"spec_no": "zzqdpg",
				"prop2": "",
				"spec_name": "111111111",
				"spec_code": "",
				"brand_no": "BRAND",
				"brand_name": "无",
				"trade_no": "JY201908260009",
				"trade_type": "线下订单"
			}],
			"created_time": 1568802721000,
			"stockin_id": 1587,
			"customer_no": "KH201809200001",
			"total_price": 2.0000,
			"refund_no": "TK1909180004",
			"trade_no_list": "JY201908260009",
			"remark": "----ZS201908260008",
			"goods_count": 1.0000,
			"shop_name": "张钟墙的店铺",
			"warehouse_name": "张钟墙的仓库",
			"actual_refund_amount": 5.0000,
			"warehouse_no": "960430",
			"shop_remark": "",
			"nick_name": "张钟墙",
			"customer_name": "",
			"stockin_time": 1568802722000,
			"status": 80,
			"check_time": 1568802721000,
			"shop_no": "960430"
		}]
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
        private String orderNo;
        private String createdTime;
        private Integer stockinId;
        private String customerNo;
        private BigDecimal totalPrice;
        private String refundNo;
        private String tradeNoList;
        private String remark;
        private BigDecimal goodsCount;
        private String shopName;
        private String warehouseName;
        private BigDecimal actualRefundAmount;
        private String warehouseNo;
        private String shopRemark;
        private String nickName;
        private String customerName;
        private String stockinTime;
        private Byte status;
        private String checkTime;
        private String shopNo;
        @SerializedName("details_list")
        private List<OrderDetailInfoDto> detailList;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }

        public Integer getStockinId() {
            return stockinId;
        }

        public void setStockinId(Integer stockinId) {
            this.stockinId = stockinId;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getRefundNo() {
            return refundNo;
        }

        public void setRefundNo(String refundNo) {
            this.refundNo = refundNo;
        }

        public String getTradeNoList() {
            return tradeNoList;
        }

        public void setTradeNoList(String tradeNoList) {
            this.tradeNoList = tradeNoList;
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

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public BigDecimal getActualRefundAmount() {
            return actualRefundAmount;
        }

        public void setActualRefundAmount(BigDecimal actualRefundAmount) {
            this.actualRefundAmount = actualRefundAmount;
        }

        public String getWarehouseNo() {
            return warehouseNo;
        }

        public void setWarehouseNo(String warehouseNo) {
            this.warehouseNo = warehouseNo;
        }

        public String getShopRemark() {
            return shopRemark;
        }

        public void setShopRemark(String shopRemark) {
            this.shopRemark = shopRemark;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getStockinTime() {
            return stockinTime;
        }

        public void setStockinTime(String stockinTime) {
            this.stockinTime = stockinTime;
        }

        public Byte getStatus() {
            return status;
        }

        public void setStatus(Byte status) {
            this.status = status;
        }

        public String getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String checkTime) {
            this.checkTime = checkTime;
        }

        public String getShopNo() {
            return shopNo;
        }

        public void setShopNo(String shopNo) {
            this.shopNo = shopNo;
        }

        public List<OrderDetailInfoDto> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<OrderDetailInfoDto> detailList) {
            this.detailList = detailList;
        }
    }

    public static class OrderDetailInfoDto {
//        private Integer stockinId;
        private BigDecimal num;
        private BigDecimal srcPrice;
        private BigDecimal price;
        private BigDecimal totalCost;
        private String remark;
        private BigDecimal rightNum;
        private Integer recId;
        private String goodsName;
        private String goodsNo;
        private String specNo;
        private String prop2;
        private String specName;
        private String specCode;
        private String brandNo;
        private String brandName;
        private String tradeNo;
        private String tradeType;

        public BigDecimal getNum() {
            return num;
        }

        public void setNum(BigDecimal num) {
            this.num = num;
        }

        public BigDecimal getSrcPrice() {
            return srcPrice;
        }

        public void setSrcPrice(BigDecimal srcPrice) {
            this.srcPrice = srcPrice;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getTotalCost() {
            return totalCost;
        }

        public void setTotalCost(BigDecimal totalCost) {
            this.totalCost = totalCost;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public BigDecimal getRightNum() {
            return rightNum;
        }

        public void setRightNum(BigDecimal rightNum) {
            this.rightNum = rightNum;
        }

        public Integer getRecId() {
            return recId;
        }

        public void setRecId(Integer recId) {
            this.recId = recId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsNo() {
            return goodsNo;
        }

        public void setGoodsNo(String goodsNo) {
            this.goodsNo = goodsNo;
        }

        public String getSpecNo() {
            return specNo;
        }

        public void setSpecNo(String specNo) {
            this.specNo = specNo;
        }

        public String getProp2() {
            return prop2;
        }

        public void setProp2(String prop2) {
            this.prop2 = prop2;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public String getSpecCode() {
            return specCode;
        }

        public void setSpecCode(String specCode) {
            this.specCode = specCode;
        }

        public String getBrandNo() {
            return brandNo;
        }

        public void setBrandNo(String brandNo) {
            this.brandNo = brandNo;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getTradeType() {
            return tradeType;
        }

        public void setTradeType(String tradeType) {
            this.tradeType = tradeType;
        }
    }
}
