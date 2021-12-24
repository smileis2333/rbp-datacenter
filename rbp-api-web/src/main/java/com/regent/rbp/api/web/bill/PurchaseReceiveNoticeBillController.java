package com.regent.rbp.api.web.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.regent.rbp.api.core.purchaseReceiveNoticeBill.PurchaseReceiveNoticeBill;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillQueryResult;
import com.regent.rbp.api.dto.purchase.PurchaseReceiveNoticeBillSaveParam;
import com.regent.rbp.api.service.purchase.PurchaseReceiveNoticeBillService;
import com.regent.rbp.api.web.bill.validate.*;
import com.regent.rbp.api.web.constants.ApiConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@RestController
@RequestMapping(ApiConstants.API_PURCHASE_RECEIVE_NOTICE_BILL)
@Api(tags = "采购到货通知单")
public class PurchaseReceiveNoticeBillController {

    @Autowired
    private PurchaseReceiveNoticeBillService purchaseReceiveNoticeBillService;

    @ApiOperation(value = "查询")
    @PostMapping("/query")
    public PageDataResponse<PurchaseReceiveNoticeBillQueryResult> query(@RequestBody @Valid PurchaseReceiveNoticeBillQueryParamCheckWrapper param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new PageDataResponse<>(0, Collections.emptyList());
        }
        return purchaseReceiveNoticeBillService.query(param);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public DataResponse save(@RequestBody @Valid PurchaseReceiveNoticeBillSaveParamCheckWrapper param, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return purchaseReceiveNoticeBillService.save(param);
        }
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        String messages = allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","));
        return ModelDataResponse.errorParameter(messages);
    }
}

@Data
class PurchaseReceiveNoticeBillSaveParamCheckWrapper extends PurchaseReceiveNoticeBillSaveParam {
    @NotBlank(message = "{javax.NotBlank.moduleId}")
    private String moduleId;

    @NotBlank(message = "{javax.NotBlank.manualId}")
    @ConflictManualIdCheck(value = PurchaseReceiveNoticeBill.class,message = "{regent.conflictManualIdCheck}")
    private String manualId;

    @NotNull(message = "{javax.NotNull.billDate}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    @NotBlank(message = "{javax.NotBlank.businessType}")
    @BusinessTypeCheck(message = "{regent.businessTypeCheck.businessTypeNotExist}")
    private String businessType;

    @NotBlank(message = "{javax.NotBlank.supplierCode}")
    @SupplierCodeCheck(message = "{regent.supplierCodeCheck.supplierCodeNotExist}")
    private String supplierCode;

    @NotBlank(message = "{javax.NotBlank.toChannelCode}")
    @ChannelCodeCheck(message = "{regent.channelCodeCheck.toChannelCodeNotExist}")
    private String toChannelCode;

    @NotNull(message = "{javax.NotBlank.status}")
    @BillStatus(message = "{regent.billStatus.statusNotExist}")
    private Integer status;

}

@Data
class PurchaseReceiveNoticeBillQueryParamCheckWrapper extends PurchaseReceiveNoticeBillQueryParam {
    @BusinessTypeCheck
    private List<String> businessType;

    @SupplierCodeCheck
    private List<String> supplierCode;

    @ChannelCodeCheck
    private List<String> toChannelCode;

    @CurrencyTypeCheck
    private List<String> currencyType;

    @BillStatus
    private List<Integer> status;

}
