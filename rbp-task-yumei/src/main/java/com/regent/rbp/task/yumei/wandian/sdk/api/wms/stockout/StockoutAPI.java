package com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout;


import com.regent.rbp.task.yumei.wandian.sdk.Pager;
import com.regent.rbp.task.yumei.wandian.sdk.api.wms.stockout.dto.*;
import com.regent.rbp.task.yumei.wandian.sdk.impl.Api;

import java.util.List;

public interface StockoutAPI
{
	@Api(value = "wms.stockout.Sales.queryWithDetail", paged = true)
	SalesStockoutResponse querySales(SalesStockoutRequest request, Pager pager);

	@Api(value = "wms.stockout.Other.createOther")
	CreateOtherStockoutResponse createOtherOutOrder(CreateOtherStockoutRequest request);

	@Api(value = "wms.stockout.Transfer.createOrder")
	CreateTransferStockoutResponse createTransferOrder(CreateTransferStockoutRequest.orderInfoDto orderInfo,
													   List<CreateTransferStockoutRequest.detailDto> detailList, boolean isCheck);

	@Api(value = "wms.stockout.Process.queryWithDetail", paged = true)
	ProcessStockoutResponse searchProcess(ProcessStockoutRequest request, Pager pager);

}
