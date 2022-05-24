package com.regent.rbp.task.yumei.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/23
 * @description
 */
@Data
public class YumeiPurchaseReceiveBill {
    @NotBlank
    @Length(max = 50)
    private String storeNo;

    @NotNull
    private Integer orderSource;

    @NotBlank
    @Length(max = 60)
    private String requestId;

    @NotEmpty
    @Valid
    private List<YumeiPurchaseReceiveBillOrder> orders;
}
