package com.regent.rbp.api.dto.sale;

import com.regent.rbp.api.dto.validate.FromTo;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;

/**
 * @program: rbp-datacenter
 * @description: 销售单付款方式
 * @author: HaiFeng
 * @create: 2021-11-09 16:24
 */
@FromTo(fromField = "retailPayTypeCode", toField = "retailPayTypeId")
@Data
public class SalesOrderBillPaymentResult {

    @NotNull
    private String retailPayTypeCode;

    @NotNull
    private BigDecimal payMoney;

    @NotNull
    private BigDecimal returnMoney;

    @Null
    private Long retailPayTypeId;
}
