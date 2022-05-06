package com.regent.rbp.task.yumei.model;

import com.regent.rbp.api.dto.receipt.ReceiptBillSaveParam;
import com.regent.rbp.api.dto.validate.GoodsInfo;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class YumeiReceiptBillSaveParam extends ReceiptBillSaveParam {
    @Valid
    @NotEmpty
    @GoodsInfo
    private List<YumeiReceiptBillGoodsDetailData> goodsDetailData;
}
