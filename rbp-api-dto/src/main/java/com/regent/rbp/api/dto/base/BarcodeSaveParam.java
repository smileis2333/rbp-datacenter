package com.regent.rbp.api.dto.base;

import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillGoodsDetailData;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/28
 * @description
 */
@Data
public class BarcodeSaveParam {
    @NotEmpty(message = "{javax.NotEmpty.barcodeData}")
    @UniqueElements(message = "{hv.UniqueElements.barcode.message}")
    @Valid
    private List<BarcodeItem> barcodeData;

    public void setBarcodeData(List<BarcodeItem> barcodeData) {
        int i = 1;
        for (BarcodeItem barcodeItem : barcodeData) {
            barcodeItem.setIdx(i++);
        }

        this.barcodeData = barcodeData;
    }
}
