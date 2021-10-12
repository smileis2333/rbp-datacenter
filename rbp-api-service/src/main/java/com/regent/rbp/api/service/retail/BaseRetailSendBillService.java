package com.regent.rbp.api.service.retail;

import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021/9/26
 * @description 基础全渠道发货单事件调用接口
 */
public interface BaseRetailSendBillService {

    ListDataResponse<RetailSendBillUploadDto> batchUploadSendBill(List<RetailSendBillUploadParam> list);

    ModelDataResponse<RetailSendBillCheckRespDto> checkOrderCanDelivery(RetailSendBillCheckReqDto param);

}
