package com.regent.rbp.task.yumei.wandian.sdk.api.wms;


import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.dto.*;
import com.regent.rbp.task.yumei.wandian.sdk.impl.Api;

import java.util.List;
import java.util.Map;

public interface StockAPI {
    @Api(value = "wms.StockSpec.queryAvailableStock", paged = true)
    StockSearchResponse searchAvailable(StockSearchRequest request, Pager pager);

    @Api(value = "wms.StockSpec.search", paged = true)
    StockSearchResponse search(StockSearchRequest request, Pager pager);

    @Api("wms.StockPd.stockSyncByPd")
    Map<String, Object> createPdOrder(PdOrderCreateRequest.OrderDto order, List<PdOrderCreateRequest.DetailDto> detailDtoList);
    
    @Api("wms.stocktransfer.Edit.createOrder")
    TransferOrderCreateResponse createTransferOrder(TransferOrderCreateRequest.orderInfoDto orderInfo, List<TransferOrderCreateRequest.detailDto> detailList, boolean isCheck);
}
