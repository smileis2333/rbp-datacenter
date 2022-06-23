package com.regent.rbp.task.yumei.model;

import cn.hutool.core.lang.UUID;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateOtherStockPayload {
    private final Integer orderSource = 4;

    private final String requestId = UUID.fastUUID().toString();

    @NotBlank
    private String storeNo;

    @NotBlank
    private String reason;

    private final Integer bizType = 1;

    @NotEmpty
    @Valid
    private List<CreateOtherStockOrder> orders;
}
