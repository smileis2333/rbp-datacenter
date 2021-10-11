package com.regent.rbp.api.web.retail;

import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;
import com.regent.rbp.api.service.finder.BaseRetailSendBillServiceFinder;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenchungui
 * @date 2021-09-26
 */
@RestController
@RequestMapping(ApiConstants.API_SEND_ORDER)
@Api(tags = "全渠道发货单")
public class RetailSendBillController {

    @ApiOperation(value = "批量上传发货单到线上")
    @PostMapping("/batchUploadSendBill/{key}")
    public ListDataResponse<RetailSendBillUploadDto> batchUploadSendBill(@PathVariable("key") String key, @RequestBody List<RetailSendBillUploadParam> param) {
        ListDataResponse<RetailSendBillUploadDto> result = BaseRetailSendBillServiceFinder.findServiceImpl(key).batchUploadSendBill(param);
        return result;
    }

    @ApiOperation(value = "检查订单是否可以发货")
    @PostMapping("/checkOrderCanDelivery/{key}")
    public ListDataResponse<RetailSendBillCheckRespDto> checkOrderCanDelivery(@PathVariable("key") String key, @RequestBody List<RetailSendBillCheckReqDto> param) {
        ListDataResponse<RetailSendBillCheckRespDto> result = BaseRetailSendBillServiceFinder.findServiceImpl(key).checkOrderCanDelivery(param);
        return result;
    }

}
