package com.regent.rbp.api.dto.receipt;

import com.regent.rbp.api.dto.base.BillDefaultParam;
import lombok.Data;

import java.util.List;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Data
public class ReceiptBillQueryParam extends BillDefaultParam {
    private List<String> channelCode;
    private List<String> fundAccountCode;
    private String fundAccountBank;
    private String receiptAccount;
    private List<String> receiptType;
    private List<String> currencyType;
}
