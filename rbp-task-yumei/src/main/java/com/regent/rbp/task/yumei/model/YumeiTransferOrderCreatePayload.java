package com.regent.rbp.task.yumei.model;

import cn.hutool.core.lang.UUID;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class YumeiTransferOrderCreatePayload {
    private final Integer orderSource = 4;

    private final String requestId = UUID.fastUUID().toString();

    @NotBlank
    private String transferType;

    @NotEmpty
    @Valid
    private List<YumeiTransferOrderCreate> orders;
}
