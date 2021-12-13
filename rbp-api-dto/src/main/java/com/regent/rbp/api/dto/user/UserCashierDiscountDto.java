package com.regent.rbp.api.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description
 */
@Data
public class UserCashierDiscountDto {

    @ApiModelProperty(notes = "货品类别")
    private String category;

    @ApiModelProperty(notes = "年份")
    private String year;

    @ApiModelProperty(notes = "最低折扣")
    private BigDecimal lowerDiscount;

}
