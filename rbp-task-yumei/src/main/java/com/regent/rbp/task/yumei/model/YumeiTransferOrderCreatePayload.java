package com.regent.rbp.task.yumei.model;

import cn.hutool.core.lang.UUID;
import com.regent.rbp.api.dto.validate.DiscreteRange;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class YumeiTransferOrderCreatePayload {
    private final Integer orderSource = 4;

    private final String requestId = UUID.fastUUID().toString();

    @NotNull
    @DiscreteRange(ranges = {1, 2})
    private Integer transferType;

    @NotEmpty
    @Valid
    private List<YumeiTransferOrderCreate> orders;
}
