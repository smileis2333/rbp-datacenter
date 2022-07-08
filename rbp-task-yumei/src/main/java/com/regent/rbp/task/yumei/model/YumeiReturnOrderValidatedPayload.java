package com.regent.rbp.task.yumei.model;

import cn.hutool.core.lang.UUID;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class YumeiReturnOrderValidatedPayload {
    @NotBlank
    private String storeNo;

    private final Integer orderSource = 4;

    private final String requestId = UUID.fastUUID().toString();

    @NotBlank
    private String outOrderNo;

}
