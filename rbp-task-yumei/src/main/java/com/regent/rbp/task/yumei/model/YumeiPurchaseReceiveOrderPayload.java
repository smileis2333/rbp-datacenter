package com.regent.rbp.task.yumei.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/23
 * @description
 */
@Data
public class YumeiPurchaseReceiveOrderPayload {
    @NotBlank
    @Length(max = 50)
    private String storeNo;

    private final Integer orderSource = 4;

    @NotBlank
    @Length(max = 60)
    private String requestId;

    @NotEmpty
    @Valid
    private List<YumeiPurchaseReceiveBillOrder> orders;
}
