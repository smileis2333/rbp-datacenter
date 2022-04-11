package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class StockSearchResponse {
    /*
    {
        "status": 0,
        "total": 2,
        "data": [{
            "goods_no": "daba3",
            "goods_name": "阳台桌椅真藤椅茶几三件套五件套组合庭院户外花园客厅阳台藤椅子",
            "spec_code": "藤椅*1",
            "spec_name": "默认规格",
            "barcode": "daba3",
            "weight": 1.2500,
            "img_url": "https://image.suning.cn/uimg/b2c/newcatentries/0070132569-000000000146026238_2.jpg_800w_800h_4e",
            "spec_no": "daba3",
            "defect": 0,
            "stock_num": 100.0000,
            "unpay_num": 0.0000,
            "subscribe_num": 0.0000,
            "order_num": 0.0000,
            "sending_num": 2.0000,
            "purchase_num": 0.0000,
            "transfer_num": 0.0000,
            "to_transfer_num": 0.0000,
            "to_purchase_num": 0.0000,
            "purchase_arrive_num": 0.0000,
            "return_num": 0.0000,
            "return_exch_num": 0.0000,
            "return_onway_num": 0.0000,
            "refund_num": 0.0000,
            "refund_exch_num": 0.0000,
            "refund_onway_num": 0.0000,
            "part_paid_num": 0.0000,
            "wms_sync_stock": 0.0000,
            "wms_stock_diff": 0.0000,
            "wms_preempty_stock": 0.0000,
            "wms_preempty_diff": 0.0000,
            "modified": "2019-11-06 10:21:34",
            "created": "2019-05-14 10:49:37",
            "warehouse_no": "pos",
            "warehouse_name": "POS_TEST",
            "warehouse_type": 2,
            "available_send_stock": 100.0000
        }]
    }
     */
    private int total ;
    @SerializedName("data")
    private List<StockSearchDto> stockSearchDtos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<StockSearchDto> getStockSearchDtos() {
        return stockSearchDtos;
    }

    public void setStockSearchDtos(List<StockSearchDto> stockSearchDtos) {
        this.stockSearchDtos = stockSearchDtos;
    }

    public static class StockSearchDto {
        /**
         * goods_no : daba4
         * goods_name : 40片蚕丝蛋白面膜补水保湿收缩毛孔祛黄痘印淡斑美白男女一件包邮
         * spec_code : 10片
         * spec_name : 默认规格
         * barcode : daba4
         * weight : 0.1
         * img_url : http://shgaocom.37cy.com/project/1710/17-10-31/201710311727168.jpg
         * spec_no : daba4
         * defect : 0
         * stock_num : 100
         * unpay_num : 0
         * subscribe_num : 0
         * order_num : 0
         * sending_num : 2
         * purchase_num : 0
         * transfer_num : 0
         * to_transfer_num : 0
         * to_purchase_num : 0
         * purchase_arrive_num : 0
         * return_num : 0
         * return_exch_num : 0
         * return_onway_num : 0
         * refund_num : 0
         * refund_exch_num : 0
         * refund_onway_num : 0
         * part_paid_num : 0
         * wms_sync_stock : 0
         * wms_stock_diff : 0
         * wms_preempty_stock : 0
         * wms_preempty_diff : 0
         * modified : 2019-09-09 10:11:34
         * created : 2019-05-14 10:49:37
         * warehouse_no : pos
         * warehouse_name : POS_TEST
         * warehouse_type : 2
         * available_send_stock : 100
         */

        private String goodsNo;
        private String goodsName;
        private String specCode;
        private String specName;
        private String barcode;
        private BigDecimal weight;
        private String imgUrl;
        private String specNo;
        private Boolean defect;
        private BigDecimal stockNum;
        private BigDecimal unpayNum;
        private BigDecimal subscribeNum;
        private BigDecimal orderNum;
        private BigDecimal sendingNum;
        private BigDecimal purchaseNum;
        private BigDecimal transferNum;
        private BigDecimal toTransferNum;
        private BigDecimal toPurchaseNum;
        private BigDecimal purchaseArriveNum;
        private BigDecimal returnNum;
        private BigDecimal returnExchNum;
        private BigDecimal returnOnwayNum;
        private BigDecimal refundNum;
        private BigDecimal refundExchNum;
        private BigDecimal refundOnwayNum;
        private BigDecimal partPaidNum;
        private BigDecimal wmsSyncStock;
        private BigDecimal wmsStockDiff;
        private BigDecimal wmsPreemptyStock;
        private BigDecimal wmsPreemptyDiff;
        private String modified;
        private String created;
        private String warehouseNo;
        private String warehouseName;
        private Byte warehouseType;
        private BigDecimal availableSendStock;

        private BigDecimal todayNum;
        private BigDecimal num7days;
        private BigDecimal num14days;
        private BigDecimal numMonth;
        private BigDecimal numAll;

        @Deprecated
        private BigDecimal costPrice;

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

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSpecNo() {
            return specNo;
        }

        public void setSpecNo(String specNo) {
            this.specNo = specNo;
        }

        public Boolean getDefect() {
            return defect;
        }

        public void setDefect(Boolean defect) {
            this.defect = defect;
        }

        public BigDecimal getStockNum() {
            return stockNum;
        }

        public void setStockNum(BigDecimal stockNum) {
            this.stockNum = stockNum;
        }

        public BigDecimal getUnpayNum() {
            return unpayNum;
        }

        public void setUnpayNum(BigDecimal unpayNum) {
            this.unpayNum = unpayNum;
        }

        public BigDecimal getSubscribeNum() {
            return subscribeNum;
        }

        public void setSubscribeNum(BigDecimal subscribeNum) {
            this.subscribeNum = subscribeNum;
        }

        public BigDecimal getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(BigDecimal orderNum) {
            this.orderNum = orderNum;
        }

        public BigDecimal getSendingNum() {
            return sendingNum;
        }

        public void setSendingNum(BigDecimal sendingNum) {
            this.sendingNum = sendingNum;
        }

        public BigDecimal getPurchaseNum() {
            return purchaseNum;
        }

        public void setPurchaseNum(BigDecimal purchaseNum) {
            this.purchaseNum = purchaseNum;
        }

        public BigDecimal getTransferNum() {
            return transferNum;
        }

        public void setTransferNum(BigDecimal transferNum) {
            this.transferNum = transferNum;
        }

        public BigDecimal getToTransferNum() {
            return toTransferNum;
        }

        public void setToTransferNum(BigDecimal toTransferNum) {
            this.toTransferNum = toTransferNum;
        }

        public BigDecimal getToPurchaseNum() {
            return toPurchaseNum;
        }

        public void setToPurchaseNum(BigDecimal toPurchaseNum) {
            this.toPurchaseNum = toPurchaseNum;
        }

        public BigDecimal getPurchaseArriveNum() {
            return purchaseArriveNum;
        }

        public void setPurchaseArriveNum(BigDecimal purchaseArriveNum) {
            this.purchaseArriveNum = purchaseArriveNum;
        }

        public BigDecimal getReturnNum() {
            return returnNum;
        }

        public void setReturnNum(BigDecimal returnNum) {
            this.returnNum = returnNum;
        }

        public BigDecimal getReturnExchNum() {
            return returnExchNum;
        }

        public void setReturnExchNum(BigDecimal returnExchNum) {
            this.returnExchNum = returnExchNum;
        }

        public BigDecimal getReturnOnwayNum() {
            return returnOnwayNum;
        }

        public void setReturnOnwayNum(BigDecimal returnOnwayNum) {
            this.returnOnwayNum = returnOnwayNum;
        }

        public BigDecimal getRefundNum() {
            return refundNum;
        }

        public void setRefundNum(BigDecimal refundNum) {
            this.refundNum = refundNum;
        }

        public BigDecimal getRefundExchNum() {
            return refundExchNum;
        }

        public void setRefundExchNum(BigDecimal refundExchNum) {
            this.refundExchNum = refundExchNum;
        }

        public BigDecimal getRefundOnwayNum() {
            return refundOnwayNum;
        }

        public void setRefundOnwayNum(BigDecimal refundOnwayNum) {
            this.refundOnwayNum = refundOnwayNum;
        }

        public BigDecimal getPartPaidNum() {
            return partPaidNum;
        }

        public void setPartPaidNum(BigDecimal partPaidNum) {
            this.partPaidNum = partPaidNum;
        }

        public BigDecimal getWmsSyncStock() {
            return wmsSyncStock;
        }

        public void setWmsSyncStock(BigDecimal wmsSyncStock) {
            this.wmsSyncStock = wmsSyncStock;
        }

        public BigDecimal getWmsStockDiff() {
            return wmsStockDiff;
        }

        public void setWmsStockDiff(BigDecimal wmsStockDiff) {
            this.wmsStockDiff = wmsStockDiff;
        }

        public BigDecimal getWmsPreemptyStock() {
            return wmsPreemptyStock;
        }

        public void setWmsPreemptyStock(BigDecimal wmsPreemptyStock) {
            this.wmsPreemptyStock = wmsPreemptyStock;
        }

        public BigDecimal getWmsPreemptyDiff() {
            return wmsPreemptyDiff;
        }

        public void setWmsPreemptyDiff(BigDecimal wmsPreemptyDiff) {
            this.wmsPreemptyDiff = wmsPreemptyDiff;
        }

        public String getModified() {
            return modified;
        }

        public void setModified(String modified) {
            this.modified = modified;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getWarehouseNo() {
            return warehouseNo;
        }

        public void setWarehouseNo(String warehouseNo) {
            this.warehouseNo = warehouseNo;
        }

        public String getWarehouseName() {
            return warehouseName;
        }

        public void setWarehouseName(String warehouseName) {
            this.warehouseName = warehouseName;
        }

        public Byte getWarehouseType() {
            return warehouseType;
        }

        public void setWarehouseType(Byte warehouseType) {
            this.warehouseType = warehouseType;
        }

        public BigDecimal getAvailableSendStock() {
            return availableSendStock;
        }

        public void setAvailableSendStock(BigDecimal availableSendStock) {
            this.availableSendStock = availableSendStock;
        }

        public BigDecimal getTodayNum() {
            return todayNum;
        }

        public void setTodayNum(BigDecimal todayNum) {
            this.todayNum = todayNum;
        }

        public BigDecimal getNum7days() {
            return num7days;
        }

        public void setNum7days(BigDecimal num7days) {
            this.num7days = num7days;
        }

        public BigDecimal getNum14days() {
            return num14days;
        }

        public void setNum14days(BigDecimal num14days) {
            this.num14days = num14days;
        }

        public BigDecimal getNumMonth() {
            return numMonth;
        }

        public void setNumMonth(BigDecimal numMonth) {
            this.numMonth = numMonth;
        }

        public BigDecimal getNumAll() {
            return numAll;
        }

        public void setNumAll(BigDecimal numAll) {
            this.numAll = numAll;
        }

        public BigDecimal getCostPrice() {
            return costPrice;
        }

        public void setCostPrice(BigDecimal costPrice) {
            this.costPrice = costPrice;
        }
    }
}
