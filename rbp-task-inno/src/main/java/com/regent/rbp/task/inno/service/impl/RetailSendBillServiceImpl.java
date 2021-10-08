package com.regent.rbp.task.inno.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckParam;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsCheckDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsUploadParam;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.retail.BaseRetailSendBillService;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
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
    private static final String SEND_BILL_KEY = "inno.DeliveryOrder";

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
     * @param param
     * @return
     */
    @Override
    public ModelDataResponse<RetailSendBillCheckDto> checkSendBill(RetailSendBillCheckParam param) {
        if (null == param || StringUtil.isEmpty(param.getBillNo())) {
            return ModelDataResponse.errorParameter(LanguageUtil.getMessage("notNull"));
        }
        CheckRetailSendBillReqDto reqDto = new CheckRetailSendBillReqDto();
        reqDto.setApp_key(innoConfig.getAppkey());
        reqDto.setApp_secrept(innoConfig.getAppsecret());
        reqDto.setData(new CheckRetailSendBillDto(param.getBillNo()));
        // 接口调用
        String api_url = String.format("%s%s", innoConfig.getUrl(), Post_CheckOrderCanDelivery);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));
        // 装换
        CheckRetailSendBillRespDto responseDto = JSON.parseObject(result, CheckRetailSendBillRespDto.class);
        if (responseDto != null && SystemConstants.FAIL_CODE.equals(responseDto.getCode())) {
            return ModelDataResponse.errorParameter(responseDto.getMsg());
        }
        RetailSendBillCheckDto resultDto = new RetailSendBillCheckDto();
        // 验证结果转换
        this.convertCheckRespDto(responseDto.getData(), resultDto);

        return ModelDataResponse.Success(resultDto);
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
     * 验证结果转换
     *
     * @param source
     * @param resultDto
     */
    private void convertCheckRespDto(CheckRetailSendBillMainDto source, RetailSendBillCheckDto resultDto) {
        if (null == source || null == resultDto) {
            return;
        }
        resultDto.setBillNo(source.getOrderSn());
        resultDto.setReason(source.getReason());
        resultDto.setCanDelivery(source.getCanDelivery());
        if (CollUtil.isNotEmpty(source.getOrderGoods())) {
            List<RetailSendBillGoodsCheckDto> billGoodsList = new ArrayList<>();
            resultDto.setBillGoodsList(billGoodsList);
            for (CheckRetailSendBillGoodsDto goodsDto : source.getOrderGoods()) {
                billGoodsList.add(new RetailSendBillGoodsCheckDto(goodsDto.getGoodsSn(), goodsDto.getGoodsSn(), goodsDto.getCanDelivery()));
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
