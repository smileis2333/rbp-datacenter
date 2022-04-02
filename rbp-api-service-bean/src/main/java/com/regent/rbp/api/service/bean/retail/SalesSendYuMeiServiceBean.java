package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailDistributionBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailDistributionBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailDistributionBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillSaveParam;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiGoodsDetailData;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiSaveParam;
import com.regent.rbp.api.service.retail.RetailDistributionBillService;
import com.regent.rbp.api.service.retail.RetailSalesSendBillService;
import com.regent.rbp.api.service.retail.SalesSendYuMeiService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-01
 * @Description
 */
@Service
public class SalesSendYuMeiServiceBean implements SalesSendYuMeiService {

    @Autowired
    private RetailDistributionBillService retailDistributionBillService;

    @Autowired
    private RetailSalesSendBillService retailSalesSendBillService;

    @Transactional
    @Override
    public ModelDataResponse<String> save(SalesSendYuMeiSaveParam param) {

        RetailDistributionBillSaveParam retailDistributionBillSaveParam = new RetailDistributionBillSaveParam();
        BeanUtils.copyProperties(param, retailDistributionBillSaveParam);
        retailDistributionBillSaveParam.setAcceptOrderStatus(1);
        retailDistributionBillSaveParam.setPrintCount(0);
        retailDistributionBillSaveParam.setPrintStatus(0);
        retailDistributionBillSaveParam.setStatus(1);
        // 货品
        List<RetailDistributionBillGoodsDetailData> goodsDetailData = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getGoodsDetailData())) {
            for (SalesSendYuMeiGoodsDetailData yuMeiGoodsDetailData : param.getGoodsDetailData()) {
                RetailDistributionBillGoodsDetailData goods = new RetailDistributionBillGoodsDetailData();
                BeanUtils.copyProperties(yuMeiGoodsDetailData, goods);
                goods.setRetailOrderBillNo(param.getRetailOrderBillNo());
                goodsDetailData.add(goods);
            }
        }
        retailDistributionBillSaveParam.setGoodsDetailData(goodsDetailData);
        // 物流
        List<RetailDistributionBillLogisticsInfoDto> logisticsInfoDtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getLogisticsInfo())) {
            for (SalesSendYuMeiLogisticsInfoDto yuMeiLogisticsInfoDto : param.getLogisticsInfo()) {
                RetailDistributionBillLogisticsInfoDto logisticsInfoDto = new RetailDistributionBillLogisticsInfoDto();
                BeanUtils.copyProperties(yuMeiLogisticsInfoDto, logisticsInfoDto);
                logisticsInfoDtoList.add(logisticsInfoDto);
            }
        }
        retailDistributionBillSaveParam.setLogisticsInfo(logisticsInfoDtoList);
        // 生成配单
        ModelDataResponse<String> distributionResult = retailDistributionBillService.save(retailDistributionBillSaveParam);
        if (distributionResult.getCode() != ResponseCode.OK) {
            return distributionResult;
        }

        RetailSalesSendBillSaveParam retailSalesSendBillSaveParam = new RetailSalesSendBillSaveParam();
        BeanUtils.copyProperties(param, retailSalesSendBillSaveParam);
        retailSalesSendBillSaveParam.setOrigin(2);
        retailSalesSendBillSaveParam.setRetailDistributionBillNo(distributionResult.getData());

        // 物流
        List<RetailSalesSendBillLogisticsInfoDto> salesSendBillLogisticsInfoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getLogisticsInfo())) {
            for (SalesSendYuMeiLogisticsInfoDto yuMeiLogisticsInfoDto : param.getLogisticsInfo()) {
                RetailSalesSendBillLogisticsInfoDto logisticsInfoDto = new RetailSalesSendBillLogisticsInfoDto();
                BeanUtils.copyProperties(yuMeiLogisticsInfoDto, logisticsInfoDto);
                salesSendBillLogisticsInfoList.add(logisticsInfoDto);
            }
        }
        retailSalesSendBillSaveParam.setLogisticsInfo(salesSendBillLogisticsInfoList);

        // 生成发货单
        return retailSalesSendBillService.save(retailSalesSendBillSaveParam);
    }
}
