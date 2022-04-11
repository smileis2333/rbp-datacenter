package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin;


import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockin.dto.*;
import com.regent.rbp.task.yumei.wandian.sdk.impl.Api;

import java.util.List;

public interface StockinAPI {
    @Api(value = "wms.stockin.Refund.queryWithDetail", paged = true)
    RefundStockinResponse searchRefund(RefundStockinRequest request, Pager pager);

    @Api(value = "wms.stockin.Other.createOtherOrder")
    CreateOtherStockinResponse createOtherOrder(CreateOtherStockinRequest request);
    
    @Api(value = "wms.stockin.Other.queryWithDetail", paged = true)
    OtherStockinResponse queryWithDetail(OtherStockinRequest request, Pager pager);

    @Api(value = "wms.stockin.Process.queryWithDetail", paged = true)
    ProcessStockinResponse searchProcess(ProcessStockinRequest request, Pager pager);
    
    @Api(value = "wms.stockin.Transfer.createOrder")
    CreateTransferStockinResponse createTransferOrder(CreateTransferStockinRequest.orderInfoDto orderInfo, List<CreateTransferStockinRequest.detailDto> detailList,boolean isCheck);
    
}
