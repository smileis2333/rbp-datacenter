package com.regent.rbp.api.web.retail;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.eum.OnlinePlatformTypeEnum;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;
import com.regent.rbp.api.service.finder.BaseRetailSendBillServiceFinder;
import com.regent.rbp.api.web.constants.ApiConstants;
import com.regent.rbp.infrastructure.annotation.PassToken;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OnlinePlatformDao onlinePlatformDao;

    @ApiOperation(value = "批量上传发货单到线上")
    @PostMapping("/batchUploadSendBill/{key}")
    @PassToken
    public ListDataResponse<RetailSendBillUploadDto> batchUploadSendBill(@PathVariable("key") String key, @RequestBody List<RetailSendBillUploadParam> param) {
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().eq("code", key));
        if (null == onlinePlatform) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{key});
        }
        String serviceKey = OnlinePlatformTypeEnum.getKeyById(onlinePlatform.getOnlinePlatformTypeId());
        ListDataResponse<RetailSendBillUploadDto> result = BaseRetailSendBillServiceFinder.findServiceImpl(serviceKey).batchUploadSendBill(param, onlinePlatform);
        return result;
    }

    @ApiOperation(value = "检查订单是否可以发货")
    @PostMapping("/checkOrderCanDelivery/{key}")
    @PassToken
    public ModelDataResponse<RetailSendBillCheckRespDto> checkOrderCanDelivery(@PathVariable("key") String key, @RequestBody RetailSendBillCheckReqDto param) {
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().eq("code", key));
        if (null == onlinePlatform) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{key});
        }
        String serviceKey = OnlinePlatformTypeEnum.getKeyById(onlinePlatform.getOnlinePlatformTypeId());
        ModelDataResponse<RetailSendBillCheckRespDto> result = BaseRetailSendBillServiceFinder.findServiceImpl(serviceKey).checkOrderCanDelivery(param, onlinePlatform);
        return result;
    }

}
