package com.regent.rbp.api.dto.goods;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xuxing
 */
@Data
public class DisableSizeDto {
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Long goodsId;

    @ApiModelProperty("颜色编号")
    @NotBlank
    private String colorCode;

    @ApiModelProperty("内长")
    @NotBlank
    private String longName;

    @ApiModelProperty("尺码")
    @NotBlank
    private String size;
}
