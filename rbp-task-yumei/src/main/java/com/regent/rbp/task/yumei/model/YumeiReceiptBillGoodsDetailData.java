package com.regent.rbp.task.yumei.model;

import com.regent.rbp.api.dto.base.GoodsDetailData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class YumeiReceiptBillGoodsDetailData extends GoodsDetailData {
    @NotNull
    @ApiModelProperty("数量")
    private BigDecimal quantity;
    @ApiModelProperty("折扣")
    private BigDecimal discount;
    @ApiModelProperty("吊牌价")
    private BigDecimal tagPrice;
    @ApiModelProperty("结算价")
    private BigDecimal balancePrice;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("金额")
    private BigDecimal amount;

}
