package com.regent.rbp.task.inno.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@Data
public class UploadRetailSendBillGoodsParam {

    @ApiModelProperty(notes = "ERP发货单号（ERP发货单主键）")
    private String ErpDeliveryOrderSn;

    @ApiModelProperty(notes = "条码")
    private String Barcode;

    @ApiModelProperty(notes = "数量")
    private Integer Qty;

    public UploadRetailSendBillGoodsParam() {
    }

    public UploadRetailSendBillGoodsParam(String ErpDeliveryOrderSn, String Barcode, Integer Qty) {
        this.ErpDeliveryOrderSn = ErpDeliveryOrderSn;
        this.Barcode = Barcode;
        this.Qty = Qty;
    }

}
