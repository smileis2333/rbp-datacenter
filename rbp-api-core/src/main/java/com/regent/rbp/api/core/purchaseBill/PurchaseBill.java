package com.regent.rbp.api.core.purchaseBill;

import com.baomidou.mybatisplus.annotation.TableName;
import com.regent.rbp.api.core.base.BillMasterData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: rbp-datacenter
 * @description: 采购单
 * @author: HaiFeng
 * @create: 2021-11-16 16:06
 */
@Data
@ApiModel(description = "采购单")
@TableName(value = "rbp_purchase_bill")
public class PurchaseBill extends BillMasterData {

    @ApiModelProperty(notes = "供应商编码")
    private Long supplierId;

    @ApiModelProperty(notes = "交货日期")
    private Date deliveryDate;

    @ApiModelProperty(notes = "业务类型")
    private Long businessTypeId;

    @ApiModelProperty(notes = "供货类型")
    private Long provideGoodsTypeId;

    @ApiModelProperty(notes = "币种")
    private Long currencyTypeId;


}
