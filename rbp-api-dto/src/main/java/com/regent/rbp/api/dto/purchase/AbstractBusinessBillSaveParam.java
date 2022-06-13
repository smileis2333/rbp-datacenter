package com.regent.rbp.api.dto.purchase;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author huangjie
 * @date : 2022/04/01
 * @description
 */
@Data
public abstract class AbstractBusinessBillSaveParam extends AbstractBillSaveParam {
    @ApiModelProperty(notes = "业务类型名称")
    @NotBlank
    private String businessType;
}
