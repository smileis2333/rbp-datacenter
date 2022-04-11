package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.eum.OnlinePlatformTypeEnum;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.retail.RetailDistributionBill;
import com.regent.rbp.api.core.retail.RetailDistributionBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailDistributionBillGoods;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.core.retail.RetailSalesSendBill;
import com.regent.rbp.api.core.retail.SalesSendBillCustomerInfo;
import com.regent.rbp.api.core.retail.SalesSendBillGoods;
import com.regent.rbp.api.core.retail.SalesSendBillLogisticsInfo;
import com.regent.rbp.api.core.retail.SalesSendBillSize;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeDetailDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillCustomerInfoDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailDistributionBillLogisticsInfoDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailSalesSendBillDao;
import com.regent.rbp.api.dao.retail.SalesSendBillCustomerInfoDao;
import com.regent.rbp.api.dao.retail.SalesSendBillSizeDao;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderSampleDto;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillLogisticsInfoDto;
import com.regent.rbp.api.dto.retail.RetailSalesSendBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillCheckRespDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsCheckReqDto;
import com.regent.rbp.api.dto.retail.RetailSendBillGoodsUploadParam;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadDto;
import com.regent.rbp.api.dto.retail.RetailSendBillUploadParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.finder.BaseRetailSendBillServiceFinder;
import com.regent.rbp.api.service.retail.RetailDistributionBillGoodsService;
import com.regent.rbp.api.service.retail.RetailOrderBillGoodsService;
import com.regent.rbp.api.service.retail.RetailSalesSendBillService;
import com.regent.rbp.api.service.retail.SalesSendBillGoodsService;
import com.regent.rbp.api.service.retail.SalesSendBillLogisticsInfoService;
import com.regent.rbp.api.service.retail.SalesSendBillSizeService;
import com.regent.rbp.api.service.retail.context.RetailSalesSendBillSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.bill.entity.LogisticsCompany;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2022-03-30
 * @Description
 */
@Slf4j
@Service
public class RetailSalesSendBillServiceBean extends ServiceImpl<RetailSalesSendBillDao, RetailSalesSendBill> implements RetailSalesSendBillService {

    @Autowired
    private RetailSalesSendBillDao retailSalesSendBillDao;

    @Autowired
    private BaseDbDao baseDbDao;

    @Autowired
    private SystemCommonService systemCommonService;

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;

    @Autowired
    private RetailOrderBillGoodsDao retailOrderBillGoodsDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private LongDao longDao;

    @Autowired
    private ColorDao colorDao;

    @Autowired
    private SizeDetailDao sizeDetailDao;

    @Autowired
    private RetailDistributionBillDao retailDistributionBillDao;

    @Autowired
    private RetailDistributionBillGoodsDao retailDistributionBillGoodsDao;

    @Autowired
    private RetailDistributionBillCustomerInfoDao retailDistributionBillCustomerInfoDao;

    @Autowired
    private RetailDistributionBillLogisticsInfoDao retailDistributionBillLogisticsInfoDao;

    @Autowired
    private BarcodeDao barcodeDao;

    @Autowired
    private DbService dbService;

    @Autowired
    private OnlinePlatformDao onlinePlatformDao;

    @Autowired
    private SalesSendBillGoodsService salesSendBillGoodsService;

    @Autowired
    private SalesSendBillSizeService salesSendBillSizeService;

    @Autowired
    private SalesSendBillLogisticsInfoService salesSendBillLogisticsInfoService;

    @Autowired
    private SalesSendBillCustomerInfoDao salesSendBillCustomerInfoDao;

    @Autowired
    private RetailDistributionBillGoodsService retailDistributionBillGoodsService;

    @Autowired
    private SalesSendBillSizeDao salesSendBillSizeDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private RetailOrderBillGoodsService retailOrderBillGoodsService;

    @Autowired
    private BaseDbService baseDbService;

    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailSalesSendBillSaveParam param) {
        if (null == param) {
            return ModelDataResponse.errorParameter("参数不能为空");
        }
        log.info("全渠道发货单 请求参数:" + param.toString());
        RetailSalesSendBillSaveContext context = new RetailSalesSendBillSaveContext();

        // 参数验证
        String msg = this.convertSaveContext(context, param);
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, msg);
        }
        // 检查订单是否可以发货
        msg = this.checkOrderCanDelivery(context.getDistributionBillGoodsList());
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, msg);
        }
        // 按配单生成发货单
        this.saveRetailSalesSendBill(context);
        // 上传inno
        ListDataResponse<RetailSendBillUploadDto> result = this.uploadSendBill(context);
        log.info(result.toString());
        for (RetailSendBillUploadDto retailSendBillUploadDto : result.getData()) {
            if (!"1".equals(retailSendBillUploadDto.getCode())) {
                msg += " " +  retailSendBillUploadDto.getMessage();
            }
        }
        if (StringUtil.isNotEmpty(msg)) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, msg);
        }
        return ModelDataResponse.Success(context.getBill().getBillNo());
    }

    /**
     * 上传发货信息
     *
     */
    public ListDataResponse<RetailSendBillUploadDto> uploadSendBill(RetailSalesSendBillSaveContext context) {
        if (null == context.getBill()) {
            return new ListDataResponse<>();
        }
        // 参数转换
        List<RetailSendBillUploadParam> uploadParamList = new ArrayList<>();
        // 发货单对应电商平台
        Map<String, List<String>> platformMap = new HashMap<>();

        Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().select("id,code,business_format_id").eq("id", context.getBill().getChannelId()));
        String type = baseDbDao.getStringDataBySql("select format_type_code as formatTypeCode from rbp_channel_business_format \n" +
                "where id in (select business_format_id from rbp_channel where id = " + channel.getId() + ") limit 1");
        RetailSendBillUploadParam item = new RetailSendBillUploadParam();
        uploadParamList.add(item);
        // 获取其中一个全渠道订单
        RetailOrderBill retailOrderBill = retailOrderBillDao.selectOne(new QueryWrapper<RetailOrderBill>().select("id,bill_no,online_order_code")
                .exists(String.format("select 1 from rbp_retail_distribution_bill_goods a where a.bill_id = %s and a.retail_order_bill_id = rbp_retail_order_bill.id", context.getBill().getRetailDistributionBillId()))
                .last("limit 1"));
        List<RetailOrderSampleDto> orderSampleDtoList = retailOrderBillDao.getDataCenterRetailOrderSampleList(Collections.singleton(OptionalUtil.ofNullable(retailOrderBill, RetailOrderBill::getId)));
        // 订单必须全部满足
        if (CollUtil.isEmpty(orderSampleDtoList)) {
            log.warn(String.format("发货单[%s]:全渠道订单存在平台类型非微商城", JSONObject.toJSONString(context.getBill())));
            return new ListDataResponse<>();
        }
        // 电商平台编号
        String onlinePlatformCode = orderSampleDtoList.get(0).getOnlinePlatformCode();
        // 单据主体
        item.setBillNo(context.getBill().getBillNo());
        item.setBillDate(DateUtil.getDateStr(context.getBill().getBillDate()));
        item.setChannelCode(OptionalUtil.ofNullable(channel, Channel::getCode));
        item.setRetailOrderBillNo(OptionalUtil.ofNullable(retailOrderBill, RetailOrderBill::getBillNo));
        item.setOnlineOrderNo(OptionalUtil.ofNullable(retailOrderBill, RetailOrderBill::getOnlineOrderCode));
        // 客户信息
        SalesSendBillCustomerInfo customerInfo = context.getBillCustomerInfo();
        if (CollUtil.isNotEmpty(context.getBillLogisticsInfoList())) {
            // 物流公司信息
            List<IdNameCodeDto> logisticsCompanyList = dbService.selectIdNameCodeList(new QueryWrapper<LogisticsCompany>()
                    .in("id", StreamUtil.toSet(context.getBillLogisticsInfoList(), SalesSendBillLogisticsInfo::getLogisticsCompanyId)), LogisticsCompany.class);
            if (CollUtil.isNotEmpty(logisticsCompanyList)) {
                IdNameCodeDto logisticsCompany = logisticsCompanyList.get(0);
                item.setLogisticsCompanyName(OptionalUtil.ofNullable(logisticsCompany, IdNameCodeDto::getName));
                item.setLogisticsCompanyCode(OptionalUtil.ofNullable(logisticsCompany, IdNameCodeDto::getCode));
            }
            item.setLogisticsNo(context.getBillLogisticsInfoList().get(0).getLogisticsBillCode());
            item.setLogisticsAmount(context.getBillLogisticsInfoList().get(0).getLogisticsAmount());
        }
        item.setContactsPerson(customerInfo.getContactsPerson());
        item.setAddress(customerInfo.getAddress());
        item.setNation(customerInfo.getNation());
        item.setProvince(customerInfo.getProvince());
        item.setCity(customerInfo.getCity());
        item.setCounty(customerInfo.getCounty());
        item.setEmail(customerInfo.getBuyerEmail());
        item.setPostCode(customerInfo.getPostCode());
        item.setTel(customerInfo.getMobile());
        item.setMobile(customerInfo.getMobile());
        item.setInsureFee(BigDecimal.ZERO);
        item.setDeliveryStoreCode(StrUtil.EMPTY);
        item.setDeliveryType("001".equals(type) ? 1 : 0);
        // 货品明细
        List<RetailSendBillGoodsUploadParam> billGoodsList = new ArrayList<>();
        item.setBillGoodsList(billGoodsList);
        List<SalesSendBillSize> billSizeList = context.getBillSizeList();
        // 条形码
        List<Barcode> barcodeList = barcodeDao.selectList(new QueryWrapper<Barcode>().in("goods_id", StreamUtil.toSet(billSizeList, SalesSendBillSize::getGoodsId)).in("long_id", StreamUtil.toSet(billSizeList, SalesSendBillSize::getLongId))
                .in("color_id", StreamUtil.toSet(billSizeList, SalesSendBillSize::getColorId)).in("size_id", StreamUtil.toSet(billSizeList, SalesSendBillSize::getSizeId))
                .orderByAsc("rule_id").groupBy("goods_id,long_id,color_id,size_id"));
        for (SalesSendBillSize size : billSizeList) {
            RetailSendBillGoodsUploadParam goodsUploadParam = new RetailSendBillGoodsUploadParam();
            goodsUploadParam.setBillNo(context.getBill().getBillNo());
            goodsUploadParam.setQuantity(size.getQuantity());
            // 获取货品条形码
            Optional<Barcode> barcodeOptional = barcodeList.stream().filter(f -> size.getGoodsId().equals(f.getGoodsId()) && size.getLongId().equals(f.getLongId())
                    && size.getColorId().equals(f.getColorId()) && size.getSizeId().equals(f.getSizeId())).findFirst();
            goodsUploadParam.setBarcode(OptionalUtil.ofNullable(barcodeOptional.get(), Barcode::getBarcode));
            billGoodsList.add(goodsUploadParam);
        }

        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().eq("code", onlinePlatformCode));
        if (null == onlinePlatform) {
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{onlinePlatformCode});
        }
        String serviceKey = OnlinePlatformTypeEnum.getKeyById(onlinePlatform.getOnlinePlatformTypeId());
        return BaseRetailSendBillServiceFinder.findServiceImpl(serviceKey).batchUploadSendBill(uploadParamList, onlinePlatform);
    }

    private void saveRetailSalesSendBill(RetailSalesSendBillSaveContext context) {
        if (CollUtil.isNotEmpty(context.getBillSizeList())) {
            salesSendBillSizeService.saveBatch(context.getBillSizeList());
        }
        if (CollUtil.isNotEmpty(context.getBillGoodsList())) {
            salesSendBillGoodsService.saveBatch(context.getBillGoodsList());
        }
        if (CollUtil.isNotEmpty(context.getBillLogisticsInfoList())) {
            salesSendBillLogisticsInfoService.saveBatch(context.getBillLogisticsInfoList());
        }
        if (context.getBillCustomerInfo() != null) {
            salesSendBillCustomerInfoDao.insert(context.getBillCustomerInfo());
        }
        // 单据自定义字段
        baseDbService.saveOrUpdateCustomFieldData(context.getBill().getModuleId(), TableConstants.SALES_SEND_BILL, context.getBill().getId(), context.getBill().getCustomFieldMap());
        if (context.getBill() != null) {
            retailSalesSendBillDao.insert(context.getBill());
        }

        // 更新配单状态
        RetailDistributionBill retailDistributionBill = new RetailDistributionBill();
        retailDistributionBill.setId(context.getRetailDistributionBill().getId());
        retailDistributionBill.setStatus(5);
        retailDistributionBill.setAcceptOrderStatus(1);
        retailDistributionBill.preUpdate();
        retailDistributionBillDao.updateById(retailDistributionBill);

        // 更新订单货品状态
        if (CollUtil.isNotEmpty(context.getDistributionBillGoodsList())) {
            List<RetailOrderBillGoods> retailOrderBillGoodsList = new ArrayList<>();
            for (RetailDistributionBillGoods goods : context.getDistributionBillGoodsList()) {
                RetailOrderBillGoods retailOrderBillGoods = new RetailOrderBillGoods();
                retailOrderBillGoods.setId(goods.getRetailOrderBillGoodsId());
                retailOrderBillGoods.setProcessStatus(4);
                retailOrderBillGoods.setOnlineStatus(5);
                retailOrderBillGoods.preUpdate();
                retailOrderBillGoodsList.add(retailOrderBillGoods);
            }
            retailOrderBillGoodsService.updateBatchById(retailOrderBillGoodsList);

            // TODO 更新订单状态
            for (Long orderId : context.getDistributionBillGoodsList().stream().map(RetailDistributionBillGoods::getRetailOrderBillId).filter(Objects::nonNull).distinct().collect(Collectors.toList())) {
                retailOrderBillDao.updateSendStatus(orderId, ThreadLocalGroup.getUserId());
            }
        }
    }

    /**
     * 验证订单是否可以发货
     */
    private String checkOrderCanDelivery(List<RetailDistributionBillGoods> billGoodsList) {
        String errorMsg = null;
        if (CollUtil.isEmpty(billGoodsList)) {
            return errorMsg;
        }
        // 查询订单电商平台类型是否为Inno微商城：1,inno订单号
        Set<Long> billIds = StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getRetailOrderBillId);
        List<RetailOrderSampleDto> orderSampleDtoList = retailOrderBillDao.getDataCenterRetailOrderSampleList(billIds);
        // 订单必须全部满足
        if (CollUtil.isEmpty(orderSampleDtoList) || orderSampleDtoList.size() != billIds.size()) {
            log.error(String.format("配货单货品明细[%s]:全渠道订单存在平台类型非微商城", JSONObject.toJSONString(billGoodsList)));
            return errorMsg;
        }
        // 条形码
        List<Barcode> barcodeList = barcodeDao.selectList(new QueryWrapper<Barcode>().in("goods_id", StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getGoodsId)).in("long_id", StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getLongId))
                .in("color_id", StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getColorId)).in("size_id", StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getSizeId))
                .orderByAsc("rule_id").groupBy("goods_id,long_id,color_id,size_id"));
        // 货品信息
        List<IdNameCodeDto> goodsDtoList = dbService.selectIdNameCodeList(new QueryWrapper<Goods>().in("id", StreamUtil.toSet(billGoodsList, RetailDistributionBillGoods::getGoodsId)), Goods.class);
        Map<Long, String> goodsMap = goodsDtoList.stream().collect(Collectors.toMap(IdNameCodeDto::getId, v -> v.getCode()));

        Map<Long, RetailOrderSampleDto> orderBillMap = orderSampleDtoList.stream().collect(Collectors.toMap(RetailOrderSampleDto::getId, Function.identity()));

        // 根据订单分组，循环验证，必须全部满足
        for (Map.Entry<Long, List<RetailDistributionBillGoods>> entry : billGoodsList.stream().collect(Collectors.groupingBy(RetailDistributionBillGoods::getRetailOrderBillId)).entrySet()) {
            Long billId = entry.getKey();
            List<RetailDistributionBillGoods> goodsList = entry.getValue();
            RetailOrderSampleDto sampleDto = orderBillMap.get(billId);
            // 参数转换
            RetailSendBillCheckReqDto reqDto = new RetailSendBillCheckReqDto();
            reqDto.setBillNo(sampleDto.getOnlineOrderCode());
            List<RetailSendBillGoodsCheckReqDto> goodsCheckReqDtoList = new ArrayList<>();
            reqDto.setBillGoodsList(goodsCheckReqDtoList);
            goodsList.forEach(item -> {
                RetailSendBillGoodsCheckReqDto goodsCheckReqDto = new RetailSendBillGoodsCheckReqDto();
                // 获取货品条形码
                Optional<Barcode> barcodeOptional = barcodeList.stream().filter(f -> item.getGoodsId().equals(f.getGoodsId()) && item.getLongId().equals(f.getLongId())
                        && item.getColorId().equals(f.getColorId()) && item.getSizeId().equals(f.getSizeId())).findFirst();
                goodsCheckReqDto.setBarcode(OptionalUtil.ofNullable(barcodeOptional.get(), Barcode::getBarcode));
                goodsCheckReqDto.setGoodsCode(goodsMap.get(item.getGoodsId()));
                goodsCheckReqDto.setQuantity(item.getQuantity());
                goodsCheckReqDto.setRowUniqueKey(OptionalUtil.ofNullable(item.getRetailOrderBillGoodsId(), v -> v.toString()));
                goodsCheckReqDtoList.add(goodsCheckReqDto);
            });

            try {
                OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().eq("code", sampleDto.getOnlinePlatformCode()));
                if (null == onlinePlatform) {
                    throw new BusinessException(ResponseCode.PARAMS_ERROR, "dataNotExist", new Object[]{sampleDto.getOnlinePlatformCode()});
                }
                String serviceKey = OnlinePlatformTypeEnum.getKeyById(onlinePlatform.getOnlinePlatformTypeId());
                ModelDataResponse<RetailSendBillCheckRespDto> result = BaseRetailSendBillServiceFinder.findServiceImpl(serviceKey).checkOrderCanDelivery(reqDto, onlinePlatform);
                if (result.getData().getCanDelivery() == 0) {
                    errorMsg = result.getData().getReason();
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return errorMsg;
    }

    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private String convertSaveContext(RetailSalesSendBillSaveContext context, RetailSalesSendBillSaveParam param) {
        List<String> messageList = new ArrayList<>();

        RetailSalesSendBill bill = new RetailSalesSendBill();
        if (ObjectUtil.isEmpty(param.getBillDate())) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (StrUtil.isEmpty(param.getManualId())) {
            messageList.add(getNotNullMessage("manualNo"));
        } else {
            Integer count = retailSalesSendBillDao.selectCount(new LambdaQueryWrapper<RetailSalesSendBill>().eq(RetailSalesSendBill::getManualId, param.getManualId()));
            if (count != null && count > 0) {
                messageList.add(getMessageByParams("dataRepeated", new String[]{LanguageUtil.getMessage("manualNo")}));
            }
        }
        Long userId = ThreadLocalGroup.getUserId();
        bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        bill.setCreatedBy(userId);
        bill.setUpdatedBy(userId);
        bill.setManualId(param.getManualId());
        bill.setBillDate(param.getBillDate());
        bill.setBillTime(new Date());
        // 渠道编码
        if (StrUtil.isNotEmpty(param.getChannelCode())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
            if (ObjectUtil.isEmpty(bill.getChannelId())) {
                messageList.add(getNotExistMessage("channelCode"));
            }
        } else {
            messageList.add(getNotNullMessage("channelCode"));
        }
        if (ObjectUtil.isEmpty(param.getStatus())) {
            messageList.add(getNotNullMessage("status"));
        }
        bill.setStatus(param.getStatus());
        // TODO 状态值
        bill.setNotes(param.getNotes());
        bill.setOrigin(param.getOrigin());

        // 班次，取序号最小
        bill.setShiftId(baseDbDao.getLongDataBySql("SELECT id FROM rbp_pos_class ORDER BY `index` ASC LIMIT 1"));
        // TODO 模块id
//        ModuleBusinessType moduleBusinessType = baseDbDao.getOneModuleBusinessType(InformationConstants.ModuleConstants.RETAIL_SEND_BILL, InformationConstants.SystemConstants.DEFAULT_BUSINESS_TYPE_ID);
//        bill.setModuleId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getModuleId));
        bill.setModuleId("701073");
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));

        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
            bill.setCustomFieldMap(customFieldMap);
        }

        RetailDistributionBill retailDistributionBill = null;
        // 全渠道配单单据编号
        if (StrUtil.isNotEmpty(param.getRetailDistributionBillNo())) {
            retailDistributionBill = retailDistributionBillDao.selectOne(new LambdaQueryWrapper<RetailDistributionBill>()
                    .eq(RetailDistributionBill::getBillNo, param.getRetailDistributionBillNo()));

        }
        if (null == retailDistributionBill) {
            messageList.add(getNotExistMessage("retailDistributionBillNo"));
            return String.join(StrUtil.COMMA, messageList);
        }
        bill.setRetailDistributionBillId(retailDistributionBill.getId());
        // 配单明细
        List<RetailDistributionBillGoods> distributionBillGoodsList = retailDistributionBillGoodsDao.selectList(new LambdaQueryWrapper<RetailDistributionBillGoods>()
                .eq(RetailDistributionBillGoods::getBillId, retailDistributionBill.getId()));

        List<SalesSendBillGoods> billGoodsList = new ArrayList<>();
        List<SalesSendBillSize> billSizeList = new ArrayList<>();
        this.generateSendGoods(bill.getId(), distributionBillGoodsList, billGoodsList, billSizeList);

        bill.setSumSkuQuantity(BigDecimal.valueOf(billGoodsList.size()));
        BigDecimal totalTagPrice = billGoodsList.stream().map(SalesSendBillGoods::getTagPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        bill.setSumTagAmount(totalTagPrice);
        BigDecimal totalAmount = billGoodsList.stream().map(SalesSendBillGoods::getBalancePrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        bill.setSumItemQuantity(totalAmount);
        bill.setSumOriginalAmount(totalAmount);
        bill.setSumRetailAmount(totalAmount);
        bill.setSumSalesAmount(totalAmount);

        // 顾客信息
        SalesSendBillCustomerInfo billCustomerInfo = new SalesSendBillCustomerInfo();
        RetailDistributionBillCustomerInfo retailDistributionBillCustomerInfo = retailDistributionBillCustomerInfoDao.selectOne(new LambdaQueryWrapper<RetailDistributionBillCustomerInfo>()
                .eq(RetailDistributionBillCustomerInfo::getBillId, retailDistributionBill.getId()));
        BeanUtils.copyProperties(retailDistributionBillCustomerInfo, billCustomerInfo);
        billCustomerInfo.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billCustomerInfo.setBillId(bill.getId());
        billCustomerInfo.preInsert();

        // 物流信息
        if (CollUtil.isEmpty(param.getLogisticsInfo())) {
            messageList.add(getNotNullMessage("logisticsInfo"));
            return String.join(StrUtil.COMMA, messageList);
        }
        List<SalesSendBillLogisticsInfo> billLogisticsInfoList = new ArrayList<>();
        for (RetailSalesSendBillLogisticsInfoDto logisticsInfoDto : param.getLogisticsInfo()) {
            SalesSendBillLogisticsInfo logisticsInfo = new SalesSendBillLogisticsInfo();
            logisticsInfo.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            logisticsInfo.setBillId(bill.getId());
            if (StrUtil.isEmpty(logisticsInfoDto.getLogisticsBillCode())) {
                messageList.add(getNotNullMessage("logisticsBillCode"));
                continue;
            }
            logisticsInfo.setLogisticsBillCode(logisticsInfoDto.getLogisticsBillCode());
            if (StrUtil.isEmpty(logisticsInfoDto.getLogisticsCompanyCode())) {
                messageList.add(getNotNullMessage("logisticsCompanyCode"));
                continue;
            }
            // 物流公司信息
            List<IdNameCodeDto> logisticsCompanyList = dbService.selectIdNameCodeList(new QueryWrapper<LogisticsCompany>().eq("code", logisticsInfoDto.getLogisticsCompanyCode()), LogisticsCompany.class);
            if (CollUtil.isNotEmpty(logisticsCompanyList)) {
                logisticsInfo.setLogisticsCompanyId(logisticsCompanyList.get(0).getId());
            } else {
                messageList.add(getNotExistMessage("logisticsCompanyCode"));
                continue;
            }
            logisticsInfo.setLogisticsAmount(logisticsInfoDto.getLogisticsAmount());
            logisticsInfo.preInsert();
            billLogisticsInfoList.add(logisticsInfo);
        }

        context.setBill(bill);
        context.setBillGoodsList(billGoodsList);
        context.setBillSizeList(billSizeList);
        context.setBillCustomerInfo(billCustomerInfo);
        context.setBillLogisticsInfoList(billLogisticsInfoList);
        context.setRetailDistributionBill(retailDistributionBill);
        context.setDistributionBillGoodsList(distributionBillGoodsList);
        return String.join(StrUtil.COMMA, messageList);
    }

    private void generateSendGoods(Long salesSendBillId, List<RetailDistributionBillGoods> distributionBillGoodsList,
                                   List<SalesSendBillGoods> billGoodsList, List<SalesSendBillSize> billSizeList) {
        // 根据货品ID分组
        AtomicInteger atomicInteger = new AtomicInteger(0);
        distributionBillGoodsList.stream().collect(Collectors.groupingBy(RetailDistributionBillGoods::getGoodsId)).forEach((goodsId, sizeList) -> {
            // 货品
            SalesSendBillGoods salesSendBillGoods = new SalesSendBillGoods();
            billGoodsList.add(salesSendBillGoods);
            RetailDistributionBillGoods item = sizeList.get(0);
            salesSendBillGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            salesSendBillGoods.setBillId(salesSendBillId);
            salesSendBillGoods.setPoint(BigDecimal.ZERO);
            salesSendBillGoods.preInsert();
            // 3-发货
            salesSendBillGoods.setType(3);
            // 0-现货
            salesSendBillGoods.setAttribute(0);
            // 0-库存
            salesSendBillGoods.setDeliveryMethod(0);
            // 0-本店
            salesSendBillGoods.setStockModel(0);
            salesSendBillGoods.setGoodsId(goodsId);
            salesSendBillGoods.setQuantity(BigDecimal.valueOf(sizeList.size()));
            salesSendBillGoods.setBalancePrice(item.getBalancePrice());
            salesSendBillGoods.setSalesPrice(item.getBalancePrice());
            salesSendBillGoods.setPlanCostPrice(item.getBalancePrice());
            salesSendBillGoods.setCostPrice(item.getBalancePrice());
            salesSendBillGoods.setRetailPrice(item.getBalancePrice());
            salesSendBillGoods.setOriginalPrice(item.getBalancePrice());
            BigDecimal tagPrice = item.getTagPrice();
            salesSendBillGoods.setTagPrice(tagPrice);
            BigDecimal discount = item.getBalancePrice().divide(tagPrice, 4, BigDecimal.ROUND_HALF_UP);
            salesSendBillGoods.setBalanceDiscount(discount);
            salesSendBillGoods.setRetailDiscount(discount);
            salesSendBillGoods.setTagDiscount(discount);
            // 尺码
            sizeList.forEach(obj -> {
                SalesSendBillSize salesSendBillSize = new SalesSendBillSize();
                billSizeList.add(salesSendBillSize);

                salesSendBillSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                salesSendBillSize.setRowIndex(atomicInteger.getAndIncrement());
                salesSendBillSize.setBillId(salesSendBillId);
                salesSendBillSize.setBillGoodsId(salesSendBillGoods.getId());
                salesSendBillSize.setGoodsId(goodsId);
                salesSendBillSize.setColorId(obj.getColorId());
                salesSendBillSize.setLongId(obj.getLongId());
                salesSendBillSize.setSizeId(obj.getSizeId());
                salesSendBillSize.setLabelId(obj.getLabelId());
                salesSendBillSize.setQuantity(obj.getQuantity());
                salesSendBillSize.preInsert();
            });
        });
    }

    private static String getNotExistMessage(String key) {
        return getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotNullMessage(String key) {
        return getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage(key)});
    }
    
    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }
}
