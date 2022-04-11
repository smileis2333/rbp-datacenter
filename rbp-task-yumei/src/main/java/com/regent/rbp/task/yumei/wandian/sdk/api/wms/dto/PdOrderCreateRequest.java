package com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto;

public class PdOrderCreateRequest {
    public static class OrderDto {

        public static final byte DEFECT_MODE_ALL = 0; //正残品一起盘点
        public static final byte DEFECT_MODE_NORMAL = 1; //只盘正品
        public static final byte DEFECT_MODE_DEFECT = 2; //只盘残次品

        private String warehouseNo;
        private  Byte defectMode;
        private String remark;

        public String getWarehouseNo() {
            return warehouseNo;
        }

        public void setWarehouseNo(String warehouseNo) {
            this.warehouseNo = warehouseNo;
        }

        public Byte getDefectMode() {
            return defectMode;
        }

        public void setDefectMode(Byte defectMode) {
            this.defectMode = defectMode;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class DetailDto {

        private String specNo;
        private String remark;
        private String newNum;
        private Boolean defect;

        public String getSpecNo() {
            return specNo;
        }

        public void setSpecNo(String specNo) {
            this.specNo = specNo;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getNewNum() {
            return newNum;
        }

        public void setNewNum(String newNum) {
            this.newNum = newNum;
        }

        public Boolean getDefect() {
            return defect;
        }

        public void setDefect(Boolean defect) {
            this.defect = defect;
        }
    }
}
