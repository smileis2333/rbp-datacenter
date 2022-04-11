package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class SalesStockoutResponse {

    /*
    {
        "status": 0,
        "data": {
            "total_count": 107,
            "order": [{
                "logistics_name": "无需物流",
                "details_list": [{
                    "rec_id": 16979,
                    "stockout_id": 4958,
                    "spec_id": 87,
                    "goods_count": 1.0000,
                    "total_amount": 99.9900,
                    "paid": 0.0000,
                    "sell_price": 99.9900,
                    "remark": "",
                    "goods_name": "奶糖",
                    "goods_no": "NT",
                    "spec_name": "含乳片",
                    "spec_code": "HRP",
                    "cost_price": 99.9900,
                    "weight": 0.0000,
                    "goods_id": 68,
                    "prop1": "",
                    "prop2": "",
                    "prop3": "",
                    "prop4": "",
                    "prop5": "",
                    "prop6": "",
                    "platform_id": 0,
                    "refund_status": 0,
                    "market_price": 99.9900,
                    "discount": 0.0000,
                    "share_amount": 99.9900,
                    "tax_rate": 0.0000,
                    "barcode": "NT",
                    "sale_order_id": 13840,
                    "gift_type": 0,
                    "src_oid": "AD201909240002",
                    "src_tid": "AT201909240002",
                    "from_mask": 0,
                    "goods_type": 1,
                    "good_prop1": "",
                    "good_prop2": "",
                    "good_prop3": "",
                    "good_prop4": "",
                    "good_prop5": "",
                    "good_prop6": ""
                }],
                "consign_time": 1570850899000,
                "post_amount": 0.0000,
                "trade_time": 1569306271000,
                "receiver_dtb": "北京市 海淀区",
                "bad_reason": 0,
                "employee_no": "LJL",
                "discount": 0.0000,
                "tax_rate": 0.0000,
                "trade_id": 7296,
                "shop_remark": "",
                "invoice_id": 0,
                "receiver_country": 0,
                "order_type": 1,
                "shop_no": "1001011",
                "receiver_area": "北京 北京市 海淀区",
                "customer_no": "KH201807250003",
                "refund_status": 0,
                "receiver_province": 110000,
                "created": 1569306571000,
                "weight": 0.0000,
                "block_reason": 0,
                "tax": 0.0000,
                "logistics_code": "0914",
                "shop_name": "随便",
                "pay_time": 1569306271000,
                "warehouse_name": "罗家林仓库",
                "goods_total_cost": 99.99000000,
                "nick_name": "罗家林",
                "trade_no": "JY201909240013",
                "id_card_type": 0,
                "status": 110,
                "order_no": "CK2019092420",
                "src_trade_no": "",
                "post_fee": 0.0000,
                "receiver_city": 110100,
                "id_card": "",
                "remark": "",
                "goods_count": 1.0000,
                "stockout_id": 4958,
                "cod_amount": 0.0000,
                "src_order_no": "JY201909240013",
                "warehouse_no": "LJLTEST",
                "receiver_telno": "",
                "receiver_zip": "",
                "trade_status": 110,
                "receiver_name": "罗家林",
                "invoice_type": 0,
                "currency": "",
                "logistics_type": 1,
                "delivery_term": 1,
                "logistics_no": "ZS201909240003",
                "receiver_district": 110108,
                "goods_total_amount": 99.9900,
                "receivable": 99.9900,
                "receiver_mobile": "10899199009",
                "salesman_no": "LJL",
                "stock_check_time": 1569306634000,
                "platform_id": 0,
                "receiver_address": "花园路5号院13号楼二楼216",
                "trade_type": 2,
                "fullname": "罗家林",
                "customer_name": "罗家林"
            }]
        }
    }
     */

    @SerializedName("total_count")
    private Integer total;
    @SerializedName("order")
    private List<OrderInfoDto> orderList;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<OrderInfoDto> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderInfoDto> orderList) {
        this.orderList = orderList;
    }

    public static class OrderInfoDto {

        private String logisticsName;
        private BigDecimal postAmount;
        private String receiverDtb;
        private Integer badReason;
        private String employeeNo;
        private BigDecimal discount;
        private BigDecimal taxRate;
        private Integer tradeId;
        private String shopRemark;
        private Integer invoiceId;
        private Integer receiverCountry;
        private Integer orderType;
        private String shopNo;
        private String receiverArea;
        private String customerNo;
        private Integer refundStatus;
        private Integer receiverProvince;
        private BigDecimal weight;
        private Integer blockReason;
        private BigDecimal tax;
        private String logisticsCode;
        private String shopName;
        private String warehouseName;
        private BigDecimal goodsTotalCost;
        private String nickName;
        private String tradeNo;
        private Integer idCardType;
        private Integer status;
        private String orderNo;
        private String srcTradeNo;
        private BigDecimal postFee;
        private Integer receiverCity;
        private String idCard;
        private String remark;
        private BigDecimal goodsCount;
        private Integer stockoutId;
        private String stockoutNo;
        private BigDecimal codAmount;
        private String srcOrderNo;
        private String warehouseNo;
        private String receiverTelno;
        private String receiverZip;
        private Integer tradeStatus;
        private String receiverName;
        private Integer invoiceType;
        private String currency;
        private Integer logisticsType;
        private Integer deliveryTerm;
        private String logisticsNo;
        private Integer receiverDistrict;
        private BigDecimal goodsTotalAmount;
        private BigDecimal receivable;
        private String receiverMobile;
        private String salesmanNo;
        private Integer platformId;
        private String receiverAddress;
        private Integer tradeType;
        private String fullname;
        private String customerName;
        private String flagName;
        private String printRemark;
        private String buyerMessage;
        private String csRemark;
        private String invoiceTitle;
        private String invoiceContent;
        private BigDecimal packageFee;
        
        private String tradeTime;
        private String payTime;
        private String stockCheckTime;
        private String created;
        private String consignTime;
        @SerializedName("details_list")
        private List<OrderDetailInfoDto> detailList;

        public String getLogisticsName() {
            return logisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            this.logisticsName = logisticsName;
        }

        public BigDecimal getPostAmount() {
            return postAmount;
        }

        public void setPostAmount(BigDecimal postAmount) {
            this.postAmount = postAmount;
        }

        public String getReceiverDtb() {
            return receiverDtb;
        }

        public void setReceiverDtb(String receiverDtb) {
            this.receiverDtb = receiverDtb;
        }

        public Integer getBadReason() {
            return badReason;
        }

        public void setBadReason(Integer badReason) {
            this.badReason = badReason;
        }

        public String getEmployeeNo() {
            return employeeNo;
        }

        public void setEmployeeNo(String employeeNo) {
            this.employeeNo = employeeNo;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }

        public Integer getTradeId() {
            return tradeId;
        }

        public void setTradeId(Integer tradeId) {
            this.tradeId = tradeId;
        }

        public String getShopRemark() {
            return shopRemark;
        }

        public void setShopRemark(String shopRemark) {
            this.shopRemark = shopRemark;
        }

        public Integer getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(Integer invoiceId) {
            this.invoiceId = invoiceId;
        }

        public Integer getReceiverCountry() {
            return receiverCountry;
        }

        public void setReceiverCountry(Integer receiverCountry) {
            this.receiverCountry = receiverCountry;
        }

        public Integer getOrderType() {
            return orderType;
        }

        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }

        public String getShopNo() {
            return shopNo;
        }

        public void setShopNo(String shopNo) {
            this.shopNo = shopNo;
        }

        public String getReceiverArea() {
            return receiverArea;
        }

        public void setReceiverArea(String receiverArea) {
            this.receiverArea = receiverArea;
        }

        public String getCustomerNo() {
            return customerNo;
        }

        public void setCustomerNo(String customerNo) {
            this.customerNo = customerNo;
        }

        public Integer getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(Integer refundStatus) {
            this.refundStatus = refundStatus;
        }

        public Integer getReceiverProvince() {
            return receiverProvince;
        }

        public void setReceiverProvince(Integer receiverProvince) {
            this.receiverProvince = receiverProvince;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public Integer getBlockReason() {
            return blockReason;
        }

        public void setBlockReason(Integer blockReason) {
            this.blockReason = blockReason;
        }

        public BigDecimal getTax() {
            return tax;
        }

        public void setTax(BigDecimal tax) {
            this.tax = tax;
        }

        public String getLogisticsCode() {
            return logisticsCode;
        }

        public void setLogisticsCode(String logisticsCode) {
            this.logisticsCode = logisticsCode;
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

        public BigDecimal getGoodsTotalCost() {
            return goodsTotalCost;
        }

        public void setGoodsTotalCost(BigDecimal goodsTotalCost) {
            this.goodsTotalCost = goodsTotalCost;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public Integer getIdCardType() {
            return idCardType;
        }

        public void setIdCardType(Integer idCardType) {
            this.idCardType = idCardType;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getSrcTradeNo() {
            return srcTradeNo;
        }

        public void setSrcTradeNo(String srcTradeNo) {
            this.srcTradeNo = srcTradeNo;
        }

        public BigDecimal getPostFee() {
            return postFee;
        }

        public void setPostFee(BigDecimal postFee) {
            this.postFee = postFee;
        }

        public Integer getReceiverCity() {
            return receiverCity;
        }

        public void setReceiverCity(Integer receiverCity) {
            this.receiverCity = receiverCity;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
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

        public Integer getStockoutId() {
            return stockoutId;
        }

        public void setStockoutId(Integer stockoutId) {
            this.stockoutId = stockoutId;
        }
        
        public String getStockoutNo() {
			return stockoutNo;
		}
        
        public void setStockoutNo(String stockoutNo) {
			this.stockoutNo = stockoutNo;
		}

        public BigDecimal getCodAmount() {
            return codAmount;
        }

        public void setCodAmount(BigDecimal codAmount) {
            this.codAmount = codAmount;
        }

        public String getSrcOrderNo() {
            return srcOrderNo;
        }

        public void setSrcOrderNo(String srcOrderNo) {
            this.srcOrderNo = srcOrderNo;
        }

        public String getWarehouseNo() {
            return warehouseNo;
        }

        public void setWarehouseNo(String warehouseNo) {
            this.warehouseNo = warehouseNo;
        }

        public String getReceiverTelno() {
            return receiverTelno;
        }

        public void setReceiverTelno(String receiverTelno) {
            this.receiverTelno = receiverTelno;
        }

        public String getReceiverZip() {
            return receiverZip;
        }

        public void setReceiverZip(String receiverZip) {
            this.receiverZip = receiverZip;
        }

        public Integer getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(Integer tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public Integer getInvoiceType() {
            return invoiceType;
        }

        public void setInvoiceType(Integer invoiceType) {
            this.invoiceType = invoiceType;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getLogisticsType() {
            return logisticsType;
        }

        public void setLogisticsType(Integer logisticsType) {
            this.logisticsType = logisticsType;
        }

        public Integer getDeliveryTerm() {
            return deliveryTerm;
        }

        public void setDeliveryTerm(Integer deliveryTerm) {
            this.deliveryTerm = deliveryTerm;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public Integer getReceiverDistrict() {
            return receiverDistrict;
        }

        public void setReceiverDistrict(Integer receiverDistrict) {
            this.receiverDistrict = receiverDistrict;
        }

        public BigDecimal getGoodsTotalAmount() {
            return goodsTotalAmount;
        }

        public void setGoodsTotalAmount(BigDecimal goodsTotalAmount) {
            this.goodsTotalAmount = goodsTotalAmount;
        }

        public BigDecimal getReceivable() {
            return receivable;
        }

        public void setReceivable(BigDecimal receivable) {
            this.receivable = receivable;
        }

        public String getReceiverMobile() {
            return receiverMobile;
        }

        public void setReceiverMobile(String receiverMobile) {
            this.receiverMobile = receiverMobile;
        }

        public String getSalesmanNo() {
            return salesmanNo;
        }

        public void setSalesmanNo(String salesmanNo) {
            this.salesmanNo = salesmanNo;
        }

        public Integer getPlatformId() {
            return platformId;
        }

        public void setPlatformId(Integer platformId) {
            this.platformId = platformId;
        }

        public String getReceiverAddress() {
            return receiverAddress;
        }

        public void setReceiverAddress(String receiverAddress) {
            this.receiverAddress = receiverAddress;
        }

        public Integer getTradeType() {
            return tradeType;
        }

        public void setTradeType(Integer tradeType) {
            this.tradeType = tradeType;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
        
        public String getFlagName() {
			return flagName;
		}
        
        public void setFlagName(String flagName) {
			this.flagName = flagName;
		}
        
        public String getPrintRemark() {
			return printRemark;
		}
        
        public void setPrintRemark(String printRemark) {
			this.printRemark = printRemark;
		}

        public String getBuyerMessage() {
			return buyerMessage;
		}
        
        public void setBuyerMessage(String buyerMessage) {
			this.buyerMessage = buyerMessage;
		}
        
        public String getCsRemark() {
			return csRemark;
		}
        
        public void setCsRemark(String csRemark) {
			this.csRemark = csRemark;
		}
        
        public String getInvoiceTitle() {
			return invoiceTitle;
		}
        
        public void setInvoiceTitle(String invoiceTitle) {
			this.invoiceTitle = invoiceTitle;
		}
        
        public String getInvoiceContent() {
			return invoiceContent;
		}
        
        public void setInvoiceContent(String invoiceContent) {
			this.invoiceContent = invoiceContent;
		}
        
        public BigDecimal getPackageFee() {
			return packageFee;
		}
        
        public void setPackageFee(BigDecimal packageFee) {
			this.packageFee = packageFee;
		}
        
        public String getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(String tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getStockCheckTime() {
            return stockCheckTime;
        }

        public void setStockCheckTime(String stockCheckTime) {
            this.stockCheckTime = stockCheckTime;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getConsignTime() {
            return consignTime;
        }

        public void setConsignTime(String consignTime) {
            this.consignTime = consignTime;
        }

        public List<OrderDetailInfoDto> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<OrderDetailInfoDto> detailList) {
            this.detailList = detailList;
        }
    }
    public static class OrderDetailInfoDto {

        private Integer recId;
        private Integer stockoutId;
        private Integer specId;
        private String specNo;
        private BigDecimal goodsCount;
        private BigDecimal totalAmount;
        private BigDecimal paid;
        private BigDecimal sellPrice;
        private String remark;
        private String goodsName;
        private String goodsNo;
        private String specName;
        private String specCode;
        private BigDecimal costPrice;
        private BigDecimal weight;
        private Integer goodsId;
        private String prop1;
        private String prop2;
        private String prop3;
        private String prop4;
        private String prop5;
        private String prop6;
        private Integer platformId;
        private Integer refundStatus;
        private BigDecimal marketPrice;
        private BigDecimal discount;
        private BigDecimal shareAmount;
        private BigDecimal taxRate;
        private String barcode;
        private Integer saleOrderId;
        private Integer giftType;
        private String srcOid;
        private String srcTid;
        private Integer fromMask;
        private Integer goodsType;
        private String batchNo;
        private String goodProp1;
        private String goodProp2;
        private String goodProp3;
        private String goodProp4;
        private String goodProp5;
        private String goodProp6;
        private Integer positionId;
        private String positionNo;
        private BigDecimal positionGoodsCount;
        private String snList;

        public Integer getRecId() {
            return recId;
        }

        public void setRecId(Integer recId) {
            this.recId = recId;
        }

        public Integer getStockoutId() {
            return stockoutId;
        }

        public void setStockoutId(Integer stockoutId) {
            this.stockoutId = stockoutId;
        }

        public Integer getSpecId() {
            return specId;
        }

        public void setSpecId(Integer specId) {
            this.specId = specId;
        }

        public String getSpecNo() {
            return specNo;
        }

        public void setSpecNo(String specNo) {
            this.specNo = specNo;
        }

        public BigDecimal getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(BigDecimal goodsCount) {
            this.goodsCount = goodsCount;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public BigDecimal getPaid() {
            return paid;
        }

        public void setPaid(BigDecimal paid) {
            this.paid = paid;
        }

        public BigDecimal getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(BigDecimal sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public BigDecimal getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(BigDecimal costPrice) {
            this.costPrice = costPrice;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public Integer getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(Integer goodsId) {
            this.goodsId = goodsId;
        }

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }

        public String getProp2() {
            return prop2;
        }

        public void setProp2(String prop2) {
            this.prop2 = prop2;
        }

        public String getProp3() {
            return prop3;
        }

        public void setProp3(String prop3) {
            this.prop3 = prop3;
        }

        public String getProp4() {
            return prop4;
        }

        public void setProp4(String prop4) {
            this.prop4 = prop4;
        }

        public String getProp5() {
            return prop5;
        }

        public void setProp5(String prop5) {
            this.prop5 = prop5;
        }

        public String getProp6() {
            return prop6;
        }

        public void setProp6(String prop6) {
            this.prop6 = prop6;
        }

        public Integer getPlatformId() {
            return platformId;
        }

        public void setPlatformId(Integer platformId) {
            this.platformId = platformId;
        }

        public Integer getRefundStatus() {
            return refundStatus;
        }

        public void setRefundStatus(Integer refundStatus) {
            this.refundStatus = refundStatus;
        }

        public BigDecimal getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(BigDecimal marketPrice) {
            this.marketPrice = marketPrice;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }

        public BigDecimal getShareAmount() {
            return shareAmount;
        }

        public void setShareAmount(BigDecimal shareAmount) {
            this.shareAmount = shareAmount;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public Integer getSaleOrderId() {
            return saleOrderId;
        }

        public void setSaleOrderId(Integer saleOrderId) {
            this.saleOrderId = saleOrderId;
        }

        public Integer getGiftType() {
            return giftType;
        }

        public void setGiftType(Integer giftType) {
            this.giftType = giftType;
        }

        public String getSrcOid() {
            return srcOid;
        }

        public void setSrcOid(String srcOid) {
            this.srcOid = srcOid;
        }

        public String getSrcTid() {
            return srcTid;
        }

        public void setSrcTid(String srcTid) {
            this.srcTid = srcTid;
        }

        public Integer getFromMask() {
            return fromMask;
        }

        public void setFromMask(Integer fromMask) {
            this.fromMask = fromMask;
        }

        public Integer getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(Integer goodsType) {
            this.goodsType = goodsType;
        }
        
        public String getBatchNo() {
			return batchNo;
		}
        
        public void setBatchNo(String batchNo) {
			this.batchNo = batchNo;
		}

        public String getGoodProp1() {
            return goodProp1;
        }

        public void setGoodProp1(String goodProp1) {
            this.goodProp1 = goodProp1;
        }

        public String getGoodProp2() {
            return goodProp2;
        }

        public void setGoodProp2(String goodProp2) {
            this.goodProp2 = goodProp2;
        }

        public String getGoodProp3() {
            return goodProp3;
        }

        public void setGoodProp3(String goodProp3) {
            this.goodProp3 = goodProp3;
        }

        public String getGoodProp4() {
            return goodProp4;
        }

        public void setGoodProp4(String goodProp4) {
            this.goodProp4 = goodProp4;
        }

        public String getGoodProp5() {
            return goodProp5;
        }

        public void setGoodProp5(String goodProp5) {
            this.goodProp5 = goodProp5;
        }

        public String getGoodProp6() {
            return goodProp6;
        }

        public void setGoodProp6(String goodProp6) {
            this.goodProp6 = goodProp6;
        }
        
        public Integer getPositionId() {
			return positionId;
		}
        
        public void setPositionId(Integer positionId) {
			this.positionId = positionId;
		}
        
        public String getPositionNo() {
			return positionNo;
		}
        
        public void setPositionNo(String positionNo) {
			this.positionNo = positionNo;
		}
        
        public BigDecimal getPositionGoodsCount() {
			return positionGoodsCount;
		}
        
        public void setPositionGoodsCount(BigDecimal positionGoodsCount) {
			this.positionGoodsCount = positionGoodsCount;
		}
        
        public String getSnList() {
			return snList;
		}
        
        public void setSnList(String snList) {
			this.snList = snList;
		}
    }
}
