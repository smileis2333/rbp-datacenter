package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author xuxing
 */
@Data
public class DisableSizeDto {
    @JsonIgnore
    private Long goodsId;
    private String colorCode;
    private String longName;
    private String size;
}
