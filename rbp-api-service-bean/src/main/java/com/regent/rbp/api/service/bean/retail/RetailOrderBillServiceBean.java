package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.base.ModuleBusinessType;
import com.regent.rbp.api.core.retail.RetailOrderBill;
import com.regent.rbp.api.core.retail.RetailOrderBillCustomerInfo;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.core.retail.RetailOrderBillPaymentInfo;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.DbDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillCustomInfoDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillOperatorLogDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillPaymentInfoDao;
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
import com.regent.rbp.infrastructure.enums.CheckEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RetailOrderBillGoodsDao retailOrderBillGoodsDao;
    @Autowired
    private RetailOrderBillOperatorLogDao retailOrderBillOperatorLogDao;
    @Autowired
    private RetailOrderBillPaymentInfoDao retailOrderBillPaymentInfoDao;
    @Autowired
    private RetailOrderBillCustomInfoDao retailOrderBillCustomInfoDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private DbDao dbDao;
    @Autowired
    private BarcodeDao barcodeDao;

    /**
     * 创建
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailOrderBillSaveParam param) {
        RetailOrderBillSaveContext context = new RetailOrderBillSaveContext();
        this.convertSaveContext(context, param);
        RetailOrderBill bill = context.getBill();
        RetailOrderBillCustomerInfo customerInfo = context.getBillCustomerInfo();
        List<RetailOrderBillGoods> billGoodsList = context.getBillGoodsList();
        List<RetailOrderBillPaymentInfo> paymentInfoList = context.getBillPaymentInfoList();
        // 参数验证
        // TODO 获取单号
        bill.setBillNo(bill.getId().toString());
        // 新增订单
        retailOrderBillDao.insert(bill);
        // 新增客户信息
        retailOrderBillCustomInfoDao.insert(customerInfo);
        // 新增货品明细

        return ModelDataResponse.Success(bill.getBillNo());
    }

    /**
     * 修改
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse update(RetailOrderBillUpdateParam param) {
        RetailOrderBillUpdateContext context = new RetailOrderBillUpdateContext();

        return null;
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
        bill.setStatus(CheckEnum.NOCHECK.getStatus());
        bill.setAfterSaleProcessStatus(StatusEnum.NONE.getStatus());
        bill.setSendStatus(StatusEnum.NONE.getStatus());
        bill.setDistributionStatus(StatusEnum.NONE.getStatus());
        bill.setRefundStatus(StatusEnum.NONE.getStatus());
        // 获取模块业务类型
        ModuleBusinessType moduleBusinessType = dbDao.getOneModuleBusinessType(BaseModuleEnum.RETAIL_ORDER_BILL.getBaseModuleId(), SystemConstants.DEFAULT_RETAIL_ORDER_BASE_BUSINESS_TYPE_ID);
        bill.setModuleId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getModuleId));
        bill.setBusinessTypeId(OptionalUtil.ofNullable(moduleBusinessType, ModuleBusinessType::getBusinessTypeId));
        bill.setBillDate(param.getBillDate());
        bill.setManualId(param.getManualId());
        bill.setOnlineOrderCode(param.getOnlineOrderCode());
        bill.setOnlinePlatformTypeId(param.getOnlinePlatformTypeId());
        bill.setOnlineStatus(param.getOnlineStatus());
        bill.setBuyerNotes(param.getBuyerNotes());
        bill.setSellerNotes(param.getSellerNotes());
        bill.setPrintNotes(param.getPrintNotes());
        bill.setNotes(param.getNotes());
        // 电商平台
        if (null != bill.getOnlinePlatformTypeId()) {
            bill.setOnlinePlatformId(dbDao.getLongDataBySql(String.format("SELECT id FROM rbp_online_platform WHERE status = 1 AND online_platform_type_id = %s LIMIT 1", bill.getOnlinePlatformTypeId())));
        }
        // 渠道编码
        if (StringUtil.isNotEmpty(param.getRetailChannelNo())) {
            bill.setChannelId(dbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = %s", param.getRetailChannelNo())));
        }
        // 营业员编码
        if (StringUtil.isNotEmpty(param.getEmployeeName())) {
            bill.setEmployeeId(dbDao.getLongDataBySql(String.format("select id from rbp_employee where work_status != 2 and name = %s", param.getEmployeeName())));
        }
        // 自定义字段
        if (CollUtil.isNotEmpty(param.getCustomizeData())) {
            Map<String, Object> customFieldMap = new HashMap<>();
            param.getCustomizeData().forEach(item -> customFieldMap.put(item.getCode(), item.getValue()));
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
            billCustomerInfo.setMemberCardId(dbDao.getLongDataBySql(String.format("select id from rbp_member_card where status = 1 and code = %s", param.getMemberCardCode())));
        }
        // 物流公司
        if (StringUtil.isNotEmpty(param.getLogisticsCompanyName())) {
            billCustomerInfo.setLogisticsCompanyId(dbDao.getLongDataBySql(String.format("select id from rbp_logistics_company where status = 100 and name = %s", param.getLogisticsCompanyName())));
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
                goods.setSaleType(item.getSaleType());
                goods.setRetailGoodsType(item.getRetailGoodsType());
                goods.setDiscount(item.getDiscount());
                goods.setBalancePrice(item.getBalancePrice());
                goods.setQuantity(item.getQuantity());
                // 初始化状态
                goods.setAfterSaleProcessStatus(StatusEnum.NONE.getStatus());
                goods.setRefundStatus(StatusEnum.NONE.getStatus());
                goods.setProcessStatus(StatusEnum.NONE.getStatus());
                goods.setReturnStatus(StatusEnum.NONE.getStatus());
                goods.setBillId(bill.getId());
            }
        });
    }
}
