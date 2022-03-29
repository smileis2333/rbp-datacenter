package com.regent.rbp.api.dto.sourcing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全渠道寻源规则 新品保护 dto
 * @author Moruins
 * @date 2022-03-28
 */
@Data
public class NewGoodsGuardOptionDto {
    @ApiModelProperty(notes = "规则id")
    private Long billId;
    @ApiModelProperty(notes = "设置值")
    private Integer value;


}
