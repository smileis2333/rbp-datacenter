package com.regent.rbp.api.dto.base;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author huangjie
 * @date : 2022/02/23
 * @description
 */
@Data
public class BoxDetailData {
    @NotBlank
    private String boxCode;
}
