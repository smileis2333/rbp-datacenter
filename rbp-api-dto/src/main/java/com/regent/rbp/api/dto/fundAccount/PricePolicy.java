package com.regent.rbp.api.dto.fundAccount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 价格政策
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class PricePolicy {
    @ApiModelProperty("价格类型名称")
    private String priceTypeName;

    @ApiModelProperty("折扣")
    private BigDecimal discount;
}
