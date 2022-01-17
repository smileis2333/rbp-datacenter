package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author xuxing
 */
@Data
public class GoodsTagPriceDto {
    @JsonIgnore
    private Long goodsId;
    private String name;
    @NotBlank
    private String code;
    @NotNull
    private BigDecimal value;
}
