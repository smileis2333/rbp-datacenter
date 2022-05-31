package com.regent.rbp.task.yumei.model;

import cn.hutool.core.lang.UUID;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/26
 * @description
 */
@Data
public class YumeiOfflineSaleOrderPayload {
    @Length(max = 50)
    @NotBlank
    private String storeNo;

    private final Integer orderSource = 4;

    private final String requestId = UUID.fastUUID().toString();

    @NotEmpty
    @Valid
    private List<YumeiOfflineSaleOrder> orders;
}
