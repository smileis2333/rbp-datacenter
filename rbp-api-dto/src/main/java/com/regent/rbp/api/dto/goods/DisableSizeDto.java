package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xuxing
 */
@Data
public class DisableSizeDto {
    @JsonIgnore
    private Long goodsId;
    @NotBlank
    private String colorCode;
    @NotBlank
    private String longName;
    @NotBlank
    private String size;
}
