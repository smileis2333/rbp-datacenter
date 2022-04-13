package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.ModuleBusinessType;
import com.regent.rbp.api.core.retail.*;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.retail.*;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailOrderBillSaveParam;
import com.regent.rbp.api.dto.retail.RetailOrderBillUpdateParam;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.enums.BaseModuleEnum;
import com.regent.rbp.api.service.retail.RetailOrderBillService;
import com.regent.rbp.api.service.retail.context.RetailOrderBillSaveContext;
import com.regent.rbp.api.service.retail.context.RetailOrderBillUpdateContext;
import com.regent.rbp.common.dao.UserDao;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/9/14
 * @description 全渠道订单 实现类
 */
@Service
public class RetailOrderBillServiceBean extends ServiceImpl<RetailOrderBillDao, RetailOrderBill> implements RetailOrderBillService {

    @Autowired
    private RetailOrderBillDao retailOrderBillDao;
    @Autowired
    private RetailOrderBillPaymentInfoDao retailOrderBillPaymentInfoDao;
    @Autowired
    private RetailOrderBillCustomInfoDao retailOrderBillCustomInfoDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BarcodeDao barcodeDao;
    @Autowired
    private SystemCommonService systemCommonService;
    @Autowired
    private RetailOrderBillGoodsDao retailOrderBillGoodsDao;
    @Autowired
    private RetailOrderBillDstbDao retailOrderBillDstbDao;

    /**
     * 创建
     * @param param
     * @return
     */
    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailOrderBillSaveParam param) {


        RetailOrderBillSaveContext context = new RetailOrderBillSaveContext();
        // 转换
        this.convertSaveContext(context, param);
        RetailOrderBill bill = context.getBill();
        RetailOrderBillCustomerInfo customerInfo = context.getBillCustomerInfo();
        List<RetailOrderBillGoods> billGoodsList = context.getBillGoodsList();
        List<RetailOrderBillPaymentInfo> paymentInfoList = context.getBillPaymentInfoList();
        List<RetailOrderBillDstb> dstbList = context.getBillDstbList();
        // 参数验证
        String msg = this.verificationProperty(bill, customerInfo, billGoodsList);
        if (StringUtil.isNotEmpty(msg)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{msg}));
        }
        // 获取单号
        bill.setBillNo(systemCommonService.getBillNo(bill.getModuleId()));
        // 新增订单
        retailOrderBillDao.insert(bill);
        // 新增支付方式
        if (CollUtil.isNotEmpty(paymentInfoList)) {
            paymentInfoList.forEach(item -> retailOrderBillPaymentInfoDao.insert(item));
        }
        // TODO 单据自定义字段
        // 新增客户信息
        retailOrderBillCustomInfoDao.insert(customerInfo);
        // 批量新增货品明细
        List<RetailOrderBillGoods> list = new ArrayList<>();
        int i = 0;
        for (RetailOrderBillGoods goods : billGoodsList) {
            i++;
            list.add(goods);
            if (i % SystemConstants.BATCH_SIZE == 0 || i == billGoodsList.size()) {
                retailOrderBillDao.batchInsertGoodsList(list);
                list.clear();
            }
        }
        // 批量新增 分销信息
        if (CollUtil.isNotEmpty(dstbList)) {
            for (RetailOrderBillDstb dstb : dstbList) {
                retailOrderBillDstbDao.insert(dstb);
            }
        }

        return ModelDataResponse.Success(bill.getBillNo());
    }

    /**
     * 修改状态
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse updateStatus(RetailOrderBillUpdateParam param) {
        RetailOrderBillUpdateContext context = new RetailOrderBillUpdateContext();
        this.convertUpdateContext(context, param);
        List<String> msgList = new ArrayList<>();
        // 参数验证
        if (StringUtil.isEmpty(context.getBillNo())) {
            msgList.add(getNotNullMessage("billNo"));
        }
        if (null == context.getStatus()) {
            msgList.add(getNotNullMessage("status"));
        }
        if (null == context.getOnlineStatus()) {
            msgList.add(getNotNullMessage("onlineStatus"));
        }
        // 查询
        RetailOrderBill bill = retailOrderBillDao.selectOne(new QueryWrapper<RetailOrderBill>().select("id").eq("bill_no", context.getBillNo()));
        if (null == bill) {
            msgList.add(getMessageByParams("dataNotExist", new String[]{context.getBillNo()}));
        }
        if (msgList.size() > 0) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, msgList)}));
        }
        // 修改状态
        bill.preUpdate();
        bill.setStatus(context.getStatus());
        bill.setOnlineStatus(context.getOnlineStatus());
        retailOrderBillDao.updateById(bill);
        return DataResponse.success();
    }

    /**
     * 修改装换器
     *
     * @param context
     * @param param
     */
    private void convertUpdateContext(RetailOrderBillUpdateContext context, RetailOrderBillUpdateParam param) {
        context.setBillNo(param.getBillNo());
        context.setStatus(param.getStatus());
        context.setOnlineStatus(param.getOnlineStatus());
    }

    /**
     * 创建转换器
     *
     * @param context
     * @param param
     */
    private void convertSaveContext(RetailOrderBillSaveContext context, RetailOrderBillSaveParam param) {
        /****************   订单主体    ******************/
        RetailOrderBill bill = RetailOrderBill.build();
        context.setBill(bill);
        // 初始化状态
        bill.setAfterSaleProcessStatus(StatusEnum.NONE.getStatus());
        bill.setSendStatus(StatusEnum.NONE.getStatus());
        bill.setDistributionStatus(StatusEnum.NONE.getStatus());
        bill.setRefundStatus(StatusEnum.NONE.getStatus());
        // 获取模块业务类型
        ModuleBusinessType moduleBusinessType = baseDbDao.getOneModuleBusinessType(BaseModuleEnum.RETAIL_ORDER_BILL.getBaseModuleId(), SystemConstants.DEFAULT_RETAIL_ORDER_BASE_BUSINESS_TYPE_ID);
        bill.setModuleId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getModuleId));
        bill.setStatus(param.getStatus());
        bill.setBusinessTypeId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getBusinessTypeId));
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualNo());
        bill.setAcceptGoodsCode(param.getAcceptGoodsCode());
        bill.setOnlineOrderCode(param.getOnlineOrderCode());
        bill.setOnlinePlatformTypeId(param.getOnlinePlatformTypeId());
        bill.setOnlineStatus(param.getOnlineStatus());
        bill.setBuyerNotes(param.getBuyerNotes());
        bill.setSellerNotes(param.getSellerNotes());
        bill.setPrintNotes(param.getPrintNotes());
        bill.setNotes(param.getNotes());
        bill.setPayTime(param.getPayTime());
        // 支付状态
        if (null != bill.getOnlineStatus() && (bill.getOnlineStatus().equals(1) || bill.getOnlineStatus().equals(3))) {
            bill.setPayStatus(1);
        } else {
            bill.setPayStatus(0);
        }
        // 电商平台
        if (StringUtil.isNotEmpty(param.getOnlinePlatformCode())) {
            bill.setOnlinePlatformId(baseDbDao.getLongDataBySql(String.format("SELECT id FROM rbp_online_platform WHERE  status = 1 and code = '%s'", param.getOnlinePlatformCode())));
        }
        // 渠道编码
        if (StringUtil.isNotEmpty(param.getRetailChannelNo())) {
            bill.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getRetailChannelNo())));
        }
        // 营业员编码
        if (StringUtil.isNotEmpty(param.getEmployeeName())) {
            bill.setEmployeeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_employee where work_status != 2 and (name = '%s' or code = '%s' ) limit 1", param.getEmployeeName(), param.getEmployeeName())));
        }
        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
        }
        /****************   支付方式    ******************/
        RetailOrderBillPaymentInfo paymentInfo = RetailOrderBillPaymentInfo.build();
        paymentInfo.setBillId(bill.getId());
        paymentInfo.setAmount(param.getAmount());
        paymentInfo.setCardNo(param.getCardNo());
        paymentInfo.setTransactionNo(param.getTransactionNo());
        if (StringUtil.isNotEmpty(param.getPayCode())) {
            paymentInfo.setRetailPaymentId(baseDbDao.getLongDataBySql(String.format("select id from rbp_retail_pay_type where code = '%s' limit 1", param.getPayCode())));
        }
        if (null != paymentInfo.getRetailPaymentId()) {
            context.setBillPaymentInfoList(Arrays.asList(paymentInfo));
        }

        /****************   顾客信息    ******************/
        RetailOrderBillCustomerInfo billCustomerInfo = RetailOrderBillCustomerInfo.build();
        context.setBillCustomerInfo(billCustomerInfo);
        billCustomerInfo.setBillId(bill.getId());
        billCustomerInfo.setBuyerNickname(param.getBuyerNickname());
        billCustomerInfo.setBuyerAccount(param.getBuyerAccount());
        billCustomerInfo.setBuyerEmail(param.getBuyerEmail());
        billCustomerInfo.setNation(param.getNation());
        billCustomerInfo.setProvince(param.getProvince());
        billCustomerInfo.setCity(param.getCity());
        billCustomerInfo.setCounty(param.getCounty());
        billCustomerInfo.setAddress(param.getAddress());
        billCustomerInfo.setContactsPerson(param.getContactsPerson());
        billCustomerInfo.setMobile(param.getMobile());
        billCustomerInfo.setPostCode(param.getPostCode());
        billCustomerInfo.setLogisticsAmount(param.getLogisticsAmount());
        billCustomerInfo.setNotes(param.getNote());
        // 会员
        if (StringUtil.isNotEmpty(param.getMemberCardCode())) {
            billCustomerInfo.setMemberCardId(baseDbDao.getLongDataBySql(String.format("select id from rbp_member_card where status = 1 and code = '%s'", param.getMemberCardCode())));
        }
        // 物流公司
        if (StringUtil.isNotEmpty(param.getLogisticsCompanyCode())) {
            billCustomerInfo.setLogisticsCompanyId(baseDbDao.getLongDataBySql(String.format("select id from rbp_logistics_company where status = 100 and code = '%s'", param.getLogisticsCompanyCode())));
        }
        /****************   货品明细    ******************/
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            return;
        }
        List<RetailOrderBillGoods> billGoodsList = new ArrayList<>();
        context.setBillGoodsList(billGoodsList);
        // 根据条形码获取货品尺码信息
        List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", param.getGoodsDetailData().stream().map(RetailOrderBillGoodsDetailData::getBarcode).collect(Collectors.toSet())));
        Map<String, Barcode> barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        param.getGoodsDetailData().forEach(item -> {
            Barcode barcode = barcodeMap.get(item.getBarcode());
            if (null != barcode) {
                RetailOrderBillGoods goods = RetailOrderBillGoods.build();
                goods.setGoodsId(barcode.getGoodsId());
                goods.setLongId(barcode.getLongId());
                goods.setColorId(barcode.getColorId());
                goods.setSizeId(barcode.getSizeId());
                goods.setBarcode(item.getBarcode());
                goods.setSaleType(item.getSaleType());
                goods.setRetailGoodsType(item.getRetailGoodsType());
                goods.setDiscount(item.getDiscount());
                goods.setBalancePrice(item.getBalancePrice());
                goods.setTagPrice(item.getTagPrice());
                goods.setQuantity(item.getQuantity());
                goods.setRemark(item.getRemark());
                // 初始化状态
                goods.setAfterSaleProcessStatus(StatusEnum.NONE.getStatus());
                goods.setRefundStatus(StatusEnum.NONE.getStatus());
                goods.setProcessStatus(StatusEnum.NONE.getStatus());
                goods.setReturnStatus(StatusEnum.NONE.getStatus());
                goods.setOnlineStatus(bill.getOnlineStatus());
                goods.setBillId(bill.getId());
                billGoodsList.add(goods);
            }
        });
        /****************   分销信息    ******************/
        if (CollUtil.isEmpty(param.getDstbInfo())) {
            return;
        }
        List<RetailOrderBillDstb> billDstbList = new ArrayList<>();
        context.setBillDstbList(billDstbList);
        param.getDstbInfo().forEach(item -> {

            RetailOrderBillDstb dstb = RetailOrderBillDstb.build();
            dstb.setBillId(bill.getId());
            dstb.setLevel(item.getLevel());
            dstb.setPhone(item.getPhone());
            dstb.setCommType(item.getCommType());
            // 分销员
            if (StringUtil.isNotEmpty(item.getDstbCode())) {
                dstb.setDstbId(baseDbDao.getLongDataBySql(String.format("select id from rbp_user where code = '%s' limit 1", item.getDstbCode(), item.getDstbCode())));
            }
            // 关联卡号
            if (StringUtil.isNotEmpty(item.getMemberCode())) {
                dstb.setMemberId(baseDbDao.getLongDataBySql(String.format("select id from rbp_logistics_company where status = 100 and code = '%s'", item.getMemberCode())));
            }
            billDstbList.add(dstb);
        });
    }

    /**
     * 验证
     *
     * @param bill
     * @param customerInfo
     * @param billGoodsList
     * @return
     */
    private String verificationProperty(RetailOrderBill bill, RetailOrderBillCustomerInfo customerInfo, List<RetailOrderBillGoods> billGoodsList) {
        List<String> messageList = new ArrayList<>();
        // 订单主体
        if (null == bill.getBillDate()) {
            messageList.add(getNotNullMessage("buildDate"));
        }
        if (null == bill.getOnlineStatus()) {
            messageList.add(getNotNullMessage("onlineStatus"));
        }
        if (null == bill.getBusinessTypeId()) {
            messageList.add(getNotNullMessage("businessTypeId"));
        }
        if (StringUtil.isEmpty(bill.getManualId())) {
            messageList.add(getNotNullMessage("manualNo"));
        }
        if (null == bill.getOnlinePlatformTypeId()) {
            messageList.add(getNotNullMessage("onlinePlatformTypeId"));
        }
        if (StringUtil.isEmpty(bill.getOnlineOrderCode())) {
            messageList.add(getNotNullMessage("onlineOrderCode"));
        }
        if (null == bill.getChannelId()) {
            messageList.add(getNotNullMessage("retailChannelNo"));
        }
        if (null == bill.getStatus()) {
            messageList.add(getNotNullMessage("status"));
        }
        // 顾客信息
        if (null == customerInfo) {
            messageList.add(getNotNullMessage("customerInfo"));
        }
        if (StringUtil.isEmpty(customerInfo.getNation())) {
            messageList.add(getNotNullMessage("nation"));
        }
        if (StringUtil.isEmpty(customerInfo.getProvince())) {
            messageList.add(getNotNullMessage("province"));
        }
        if (StringUtil.isEmpty(customerInfo.getCity())) {
            messageList.add(getNotNullMessage("city"));
        }
        if (StringUtil.isEmpty(customerInfo.getAddress())) {
            messageList.add(getNotNullMessage("address"));
        }
        if (StringUtil.isEmpty(customerInfo.getContactsPerson())) {
            messageList.add(getNotNullMessage("contactsPerson"));
        }
        if (StringUtil.isEmpty(customerInfo.getMobile())) {
            messageList.add(getNotNullMessage("mobile"));
        }
        // 货品明细
        if (CollUtil.isEmpty(billGoodsList)) {
            messageList.add(getNotNullMessage("goodsDetailData"));
        }
        int i = 0;
        for (RetailOrderBillGoods goods : billGoodsList) {
            i++;
            if (null == goods.getSaleType()) {
                messageList.add(getNotNullMessage(i, "saleType"));
            }
            if (null == goods.getRetailGoodsType()) {
                messageList.add(getNotNullMessage(i, "retailGoodsType"));
            }
            if (null == goods.getQuantity()) {
                messageList.add(getNotNullMessage(i, "quantity"));
            }
            if (null == goods.getBalancePrice()) {
                messageList.add(getNotNullMessage(i, "balancePrice"));
            }
            if (StringUtil.isEmpty(goods.getBarcode())) {
                messageList.add(getNotNullMessage(i, "barcode"));
            }
        }
        // 判断手工单号是否重复
        if (messageList.size() == 0) {
            Integer count = retailOrderBillDao.selectCount(new QueryWrapper<RetailOrderBill>().eq("manual_id", bill.getManualId()));
            if (null != count && count > 0) {
                messageList.add(getMessageByParams("dataExist", new String[]{LanguageUtil.getMessage("manualNo")}));
            }
        }
        return String.join(StrUtil.COMMA, messageList);
    }

    /**
     * 返回非空信息
     *
     * @param key
     * @return
     */
    private static String getNotNullMessage(String key) {
        return getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotNullMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotNullMessage(key)});
    }

    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }



}
