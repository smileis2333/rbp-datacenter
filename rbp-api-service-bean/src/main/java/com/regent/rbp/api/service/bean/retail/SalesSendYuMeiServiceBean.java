package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regent.rbp.api.core.eum.OnlinePlatformTypeEnum;
import com.regent.rbp.api.core.retail.LogisticsCompany;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailDistributionBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailDistributionBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailDistributionBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillSaveParam;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiGoodsDetailData;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.SalesSendYuMeiSaveParam;
import com.regent.rbp.api.service.retail.LogisticsCompanyPlatformMappingService;
import com.regent.rbp.api.service.retail.RetailDistributionBillService;
import com.regent.rbp.api.service.retail.RetailSalesSendBillService;
import com.regent.rbp.api.service.retail.SalesSendYuMeiService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class SalesSendYuMeiServiceBean implements SalesSendYuMeiService {

    @Autowired
    private RetailDistributionBillService retailDistributionBillService;

    @Autowired
    private RetailSalesSendBillService retailSalesSendBillService;

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Autowired
    private LogisticsCompanyPlatformMappingService logisticsCompanyPlatformMappingService;

    @Transactional
    @Override
    public ModelDataResponse<String> save(SalesSendYuMeiSaveParam param) {
        if (null == param) {
            return ModelDataResponse.errorParameter("参数不能为空");
        }
        log.info("全渠道发货单(玉美) 请求参数:" + param.toString());
        RetailOrderBill retailOrderBill = null;
        if (StrUtil.isEmpty(param.getRetailOrderBillNo())) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "线上订单号(retailOrderBillNo)不能为空");
        }
        retailOrderBill = retailOrderBillDao.selectOne(new LambdaQueryWrapper<RetailOrderBill>()
                .eq(RetailOrderBill::getManualId, param.getRetailOrderBillNo()));
        if (null == retailOrderBill) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "线上订单号(retailOrderBillNo)不存在");
        }

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
                goods.setRetailOrderBillNo(retailOrderBill.getBillNo());
                goods.setOnlineOrderCode(param.getRetailOrderBillNo());
                goodsDetailData.add(goods);
            }
        }
        retailDistributionBillSaveParam.setGoodsDetailData(goodsDetailData);
        // 物流
        List<RetailDistributionBillLogisticsInfoDto> logisticsInfoDtoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(param.getLogisticsInfo())) {
            for (SalesSendYuMeiLogisticsInfoDto yuMeiLogisticsInfoDto : param.getLogisticsInfo()) {
                // 旺店通平台物流编号转为系统物流编号
                LogisticsCompany logisticsCompany = logisticsCompanyPlatformMappingService.getLogisticsCompanyCode(yuMeiLogisticsInfoDto.getLogisticsCompanyCode(), OnlinePlatformTypeEnum.WDT.getId());
                yuMeiLogisticsInfoDto.setLogisticsCompanyCode(OptionalUtil.ofNullable(logisticsCompany, LogisticsCompany::getCode, yuMeiLogisticsInfoDto.getLogisticsCompanyCode()));
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
