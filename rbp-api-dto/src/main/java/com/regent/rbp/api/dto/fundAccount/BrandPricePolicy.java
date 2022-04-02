package com.regent.rbp.api.dto.fundAccount;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 品牌价格政策
 * @Author shaoqidong
 * @Date 2021/12/1
 **/
@Data
public class BrandPricePolicy {
    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("折扣类别名称")
    private String discountCategoryName;

    @ApiModelProperty("价格类型名称")
    private String priceTypeName;

    @ApiModelProperty("折扣")
    private BigDecimal discount;
}
