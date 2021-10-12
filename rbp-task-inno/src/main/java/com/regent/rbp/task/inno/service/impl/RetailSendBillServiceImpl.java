package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsUploadParam;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.BaseRetailSendBillService;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.CheckRetailSendBillDto;
import com.regent.rbp.task.inno.model.dto.CheckRetailSendBillGoodsDto;
import com.regent.rbp.task.inno.model.dto.CheckRetailSendBillMainDto;
import com.regent.rbp.task.inno.model.dto.UploadRetailSendBillDto;
import com.regent.rbp.task.inno.model.param.UploadRetailSendBillGoodsParam;
import com.regent.rbp.task.inno.model.param.UploadRetailSendBillParam;
import com.regent.rbp.task.inno.model.req.CheckRetailSendBillReqDto;
import com.regent.rbp.task.inno.model.req.UploadRetailSendBillReqDto;
import com.regent.rbp.task.inno.model.resp.CheckRetailSendBillRespDto;
import com.regent.rbp.task.inno.model.resp.UploadRetailSendBillRespDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/27
 * @description
 */
@Service
public class RetailSendBillServiceImpl implements BaseRetailSendBillService {

    /**
     * 唯一标识
     */
    private static final String SEND_BILL_KEY = "INNO";

    /**
     * 接口
     */
    private static final String Post_ErpDeliveryOrder = "api/DeliveryOrder/Post_ErpDeliveryOrder";
    private static final String Post_CheckOrderCanDelivery = "api/DeliveryOrder/Post_CheckOrderCanDelivery";

    @Autowired
    private InnoConfig innoConfig;

    /**
     * 上传ERP发货单到线上
     *
     * @param list
     * @return
     */
    @Override
    public ListDataResponse<RetailSendBillUploadDto> batchUploadSendBill(List<RetailSendBillUploadParam> list) {
        if (CollUtil.isEmpty(list)) {
            return ListDataResponse.errorParameter(LanguageUtil.getMessage("notNull"));
        }
        if (list.stream().filter(f -> CollUtil.isEmpty(f.getBillGoodsList())).count() > 0) {
            return ListDataResponse.errorParameter(LanguageUtil.getMessageByParams("dataNotNull", new Object[]{LanguageUtil.getMessage("billGoodsList")}));
        }
        List<UploadRetailSendBillParam> billList = new ArrayList<>();
        this.convertUploadReqDto(list, billList);
        UploadRetailSendBillReqDto reqDto = new UploadRetailSendBillReqDto();
        reqDto.setApp_key(innoConfig.getAppkey());
        reqDto.setApp_secrept(innoConfig.getAppsecret());
        reqDto.setData(billList);
        // 接口调用
        String api_url = String.format("%s%s", innoConfig.getUrl(), Post_ErpDeliveryOrder);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
        // 装换
        UploadRetailSendBillRespDto responseDto = JSON.parseObject(result, UploadRetailSendBillRespDto.class);
        if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
            return ListDataResponse.errorParameter(responseDto.getMsg());
        }
        // 上传结果转换
        List<RetailSendBillUploadDto> resultList = new ArrayList<>();
        this.convertUploadRespDto(responseDto.getData(), resultList);

        return ListDataResponse.Success(resultList);
    }

    /**
     * 检查订单是否可以发货
     *
     * @return
     */
    @Override
    public ModelDataResponse<RetailSendBillCheckRespDto> checkOrderCanDelivery(RetailSendBillCheckReqDto orderBill) {
        if (null == orderBill || CollUtil.isEmpty(orderBill.getBillGoodsList())) {
            return ModelDataResponse.errorParameter(LanguageUtil.getMessage("notNull"));
        }
        CheckRetailSendBillReqDto reqDto = new CheckRetailSendBillReqDto();
        reqDto.setApp_key(innoConfig.getAppkey());
        reqDto.setApp_secrept(innoConfig.getAppsecret());
        // 循环验证
        RetailSendBillCheckRespDto checkRespDto = new RetailSendBillCheckRespDto();
        checkRespDto.setBillNo(orderBill.getBillNo());
        reqDto.setData(new CheckRetailSendBillDto(orderBill.getBillNo()));
        // 接口调用
        String api_url = String.format("%s%s", innoConfig.getUrl(), Post_CheckOrderCanDelivery);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
        // 装换
        CheckRetailSendBillRespDto responseDto = JSON.parseObject(result, CheckRetailSendBillRespDto.class);
        if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
            checkRespDto.setReason(responseDto.getMsg());
            checkRespDto.setCanDelivery(0);
        } else {
            // 验证结果转换
            CheckRetailSendBillMainDto onlineBill = responseDto.getData();
            checkRespDto.setReason(onlineBill.getReason());
            checkRespDto.setCanDelivery(onlineBill.getCanDelivery());
            // 判断对应货品是否可以发货
            if (CollUtil.isNotEmpty(onlineBill.getOrderGoods())) {
                List<RetailSendBillGoodsCheckRespDto> billGoodsList = new ArrayList<>();
                checkRespDto.setBillGoodsList(billGoodsList);
                Map<String, CheckRetailSendBillGoodsDto> onlineGoodsMap = onlineBill.getOrderGoods().stream().collect(Collectors.toMap(CheckRetailSendBillGoodsDto::getSingleCode, Function.identity()));
                for (RetailSendBillGoodsCheckReqDto orderBillGoods : orderBill.getBillGoodsList()) {
                    Integer canDelivery = OptionalUtil.ofNullable(onlineGoodsMap.get(orderBillGoods.getSingleCode()), CheckRetailSendBillGoodsDto::getCanDelivery);
                    RetailSendBillGoodsCheckRespDto goodsDto = new RetailSendBillGoodsCheckRespDto(orderBillGoods.getGoodsCode(), orderBillGoods.getBarcode(), canDelivery);
                    // 存在不能发货货品，则当前订单不能发货
                    if (null == canDelivery) {
                        checkRespDto.setCanDelivery(0);
                        checkRespDto.setReason(LanguageUtil.getMessage("checkOrderError0"));
                        goodsDto.setCanDelivery(0);
                        goodsDto.setReason(LanguageUtil.getMessage("notExist"));
                    } else if (canDelivery.equals(0)) {
                        checkRespDto.setCanDelivery(canDelivery);
                        checkRespDto.setReason(LanguageUtil.getMessage("checkOrderError1"));
                    }
                    billGoodsList.add(goodsDto);
                }
            }
        }

        return ModelDataResponse.Success(checkRespDto);
    }

    /**
     * 上传实体转换
     *
     * @param sourceList
     * @param targetList
     */
    private void convertUploadReqDto(List<RetailSendBillUploadParam> sourceList, List<UploadRetailSendBillParam> targetList) {
        if (CollUtil.isEmpty(sourceList) || null == targetList) {
            return;
        }
        for (RetailSendBillUploadParam param : sourceList) {
            // 单据主体
            UploadRetailSendBillParam bill = new UploadRetailSendBillParam();
            targetList.add(bill);
            bill.setErpDeliveryOrderSn(param.getBillNo());
            bill.setErpOrderSn(param.getRetailOrderBillNo());
            bill.setOrderSn(param.getOnlineOrderNo());
            bill.setAddTime(param.getBillDate());
            bill.setShippingName(param.getLogisticsCompanyName());
            bill.setShipping_Code(param.getLogisticsCompanyCode());
            bill.setConsignee(param.getContactsPerson());
            bill.setAddress(param.getAddress());
            bill.setCountry(param.getNation());
            bill.setProvince(param.getProvince());
            bill.setCity(param.getCity());
            bill.setDistrict(param.getCounty());
            bill.setEmail(param.getEmail());
            bill.setZipcode(param.getPostCode());
            bill.setTel(param.getTel());
            bill.setMobile(param.getMobile());
            bill.setInsureFee(null != param.getInsureFee() ? param.getInsureFee().toString() : StrUtil.EMPTY);
            bill.setShippingFee(null != param.getLogisticsAmount() ? param.getLogisticsAmount().toString() : StrUtil.EMPTY);
            bill.setChannalCode(param.getChannelCode());
            bill.setDelivery_store_code(param.getDeliveryStoreCode());
            bill.setDelivery_type(param.getDeliveryType());
            // 货品明细
            if (CollUtil.isNotEmpty(param.getBillGoodsList())) {
                List<UploadRetailSendBillGoodsParam> deliveryGoodsList = new ArrayList<>();
                bill.setDeliveryGoodsList(deliveryGoodsList);
                for (RetailSendBillGoodsUploadParam goods : param.getBillGoodsList()) {
                    deliveryGoodsList.add(new UploadRetailSendBillGoodsParam(goods.getBillNo(), goods.getBarcode(), goods.getQuantity().intValue()));
                }
            }
        }
    }


    /**
     * 上传结果转换
     *
     * @param billDtoList
     * @param resultList
     */
    private void convertUploadRespDto(List<UploadRetailSendBillDto> billDtoList, List<RetailSendBillUploadDto> resultList) {
        if (CollUtil.isEmpty(billDtoList) || null == resultList) {
            return;
        }
        for (UploadRetailSendBillDto billDto : billDtoList) {
            resultList.add(new RetailSendBillUploadDto(billDto.getRowUniqueKey(), billDto.getCode(), billDto.getMsg()));
        }
    }

}
