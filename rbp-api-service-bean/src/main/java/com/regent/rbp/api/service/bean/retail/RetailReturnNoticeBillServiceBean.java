package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.retail.*;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.base.ColorDao;
import com.regent.rbp.api.dao.base.LongDao;
import com.regent.rbp.api.dao.base.SizeClassDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillDao;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.retail.RetailReturnNoticeBillService;
import com.regent.rbp.api.service.retail.context.RetailReturnNoticeBillSaveContext;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知单 Bean
 * @author: HaiFeng
 * @create: 2021-09-27 15:50
 */
@Service
public class RetailReturnNoticeBillServiceBean extends ServiceImpl<RetailReturnNoticeBillDao, RetailReturnNoticeBill> implements RetailReturnNoticeBillService {

    @Autowired
    BarcodeDao barcodeDao;
    @Autowired
    RetailReturnNoticeBillDao retailReturnNoticeBillDao;
    @Autowired
    RetailReturnNoticeBillGoodsDao retailReturnNoticeBillGoodsDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    RetailOrderBillDao retailOrderBillDao;
    @Autowired
    RetailOrderBillGoodsDao retailOrderBillGoodsDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    LongDao longDao;
    @Autowired
    SizeClassDao sizeClassDao;

    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailReturnNoticeBillSaveParam param) {
        RetailReturnNoticeBillSaveContext context = new RetailReturnNoticeBillSaveContext(param);
        // 验证数有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if(errorMsgList.size() > 0 ) {
            String message = LanguageUtil.getMessage(LanguageUtil.ZH, "paramVerifyError", new String[]{StringUtil.join(errorMsgList, ",")});
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, message);
        }

        // 写入 全渠道退货通知单
        retailReturnNoticeBillDao.insert(context.getBill());
        // 更新 全渠道订单 售后状态 and 退款状态
        RetailOrderBill orderBill = retailOrderBillDao.selectById(context.getBill().getRetailOrderBillId());
        orderBill.setAfterSaleProcessStatus(2);// 同意退货
        orderBill.setRefundStatus(2);   // 待退货
        retailOrderBillDao.updateById(orderBill);

        for (RetailReturnNoticeBillGoods billGoods : context.getBillGoodsList()) {
            retailReturnNoticeBillGoodsDao.insert(billGoods);
            // 更新 全渠道订单 货品售后状态 and 货品退款状态
            RetailOrderBillGoods orderBillGoods = retailOrderBillGoodsDao.selectById(billGoods.getRetailOrderBillGoodsId());
            orderBillGoods.setRefundStatus(2);
            orderBillGoods.setAfterSaleProcessStatus(3);
            retailOrderBillGoodsDao.updateById(orderBillGoods);
        }
        return ModelDataResponse.Success(context.getBill().getBillNo());
    }

    /**
     * 验证有效性
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(RetailReturnNoticeBillSaveParam param, RetailReturnNoticeBillSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        RetailReturnNoticeBill bill = context.getBill();

        if (StringUtils.isBlank(param.getBillNo())) {
            errorMsgList.add("单号(billNo)不能为空");
        }

        if (StringUtils.isBlank(param.getSaleChannelCode())) {
            errorMsgList.add("销售渠道编号(saleChannelCode)不能为空");
        } else {
            Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getSaleChannelCode()));
            if (channel != null) {
                bill.setSaleChannelId(channel.getId());
            } else {
                errorMsgList.add("销售渠道编号(saleChannelCode)不存在");
            }
        }
        if (StringUtils.isBlank(param.getReceiveChannelCode())) {
            errorMsgList.add("收货渠道编号(receiveChannelCode)不能为空");
        } else {
            Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getReceiveChannelCode()));
            if (channel != null) {
                bill.setReceiveChannelId(channel.getId());
            } else {
                errorMsgList.add("收货渠道编号(receiveChannelCode)不存在");
            }
        }
        if (StringUtils.isBlank(param.getRetailOrdereBillNo())) {
            errorMsgList.add("全渠道订单号(retailOrdereBillNo)不能为空");
        } else {
            RetailOrderBill orderBill = retailOrderBillDao.selectOne(new QueryWrapper<RetailOrderBill>().eq("bill_no", param.getRetailOrdereBillNo()));
            if (orderBill != null) {
                bill.setRetailOrderBillId(orderBill.getId());
            } else {
                errorMsgList.add("全渠道订单号(retailOrdereBillNo)不存在");
            }
        }
        if (param.getStatus() == null) {
            errorMsgList.add("单据状态(status)不能为空");
        }
        if (param.getGoodsDetailData() == null) {
            errorMsgList.add("货品明细(goodsDetailData)不能为空");
        } else {
            List<RetailReturnNoticeBillGoods> goodsList = new ArrayList<>();
            for (RetailReturnNoticeBillGoodsDetailData data : param.getGoodsDetailData()) {

                List<RetailOrderBillGoods> orderDetailList = retailOrderBillGoodsDao.selectList(new QueryWrapper<RetailOrderBillGoods>().eq("bill_id", bill.getRetailOrderBillId()));

                // 一行一件
                BigDecimal quantity = data.getQuantity();
                while (quantity.compareTo(BigDecimal.ZERO) == 1) {
                    RetailReturnNoticeBillGoods detail = RetailReturnNoticeBillGoods.build();
                    detail.setBillId(bill.getId());

                    if (StringUtils.isNotBlank(data.getBarcode())) {
                        Barcode barcode = barcodeDao.selectOne(new QueryWrapper<Barcode>().eq("barcode", data.getBarcode()));
                        if (barcode == null) {
                            errorMsgList.add(String.format("条码(barcode)： %s 不存在", data.getBarcode()));
                            continue;
                        }
                        // 验证当前款是否已经退货
                        List<RetailOrderBillGoods> orderList = orderDetailList.stream().filter(f -> f.getBarcode().equals(data.getBarcode()) &&
                                f.getBalancePrice().compareTo(data.getBalancePrice()) == 0 && f.getReturnStatus().equals(0)).collect(Collectors.toList());
                        if (orderList == null || orderList.size() == 0) {
                            errorMsgList.add(String.format("条码(barcode)： %s 不存在或已退货", data.getBarcode()));
                            continue;
                        }

                        RetailOrderBillGoods orderBillGoods = orderList.get(0);
                        detail.setRetailOrderBillGoodsId(orderBillGoods.getId());
                        orderDetailList.remove(orderBillGoods);

                        detail.setBarcode(barcode.getBarcode());
                        detail.setGoodsId(barcode.getGoodsId());
                        detail.setColorId(barcode.getColorId());
                        detail.setLongId(barcode.getLongId());
                        detail.setSizeId(barcode.getSizeId());
                        detail.setTagPrice(data.getTagPrice());
                        detail.setBalancePrice(data.getBalancePrice());
                        detail.setDiscount(data.getDiscount());
                        detail.setQuantity(BigDecimal.ONE);
                    } else {
                        // 暂时为空 先不处理货品写入
                    }

                    goodsList.add(detail);
                    quantity = quantity.subtract(BigDecimal.ONE);
                }

            }
            context.setBillGoodsList(goodsList);
        }
        return errorMsgList;
    }

    /**
     * 转换
     * @param context
     * @param param
     */
    private void convertSaveContext(RetailReturnNoticeBillSaveContext context, RetailReturnNoticeBillSaveParam param) {
        RetailReturnNoticeBill bill = RetailReturnNoticeBill.build();
        bill.setManualId(param.getManualId());
        bill.setBillNo(param.getBillNo());
        bill.setBillDate(param.getBillDate());
        bill.setLogisticsBillCode(param.getLogisticsBillCode());
        bill.setStatus(param.getStatus());
        bill.setNotes(param.getNotes());
        context.setBill(bill);

        // 货品明细
        if (CollUtil.isEmpty(param.getGoodsDetailData())) {
            return;
        }
        List<RetailReturnNoticeBillGoods> billGoodsList = new ArrayList<>();
        // 根据条形码获取货品尺码信息
        List<String> barcodeList = param.getGoodsDetailData().stream().map(RetailReturnNoticeBillGoodsDetailData::getBarcode).distinct().collect(Collectors.toList());
        List<Barcode> barcodes = barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodeList));
        Map<String, Barcode> barcodeMap = barcodes.stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
        param.getGoodsDetailData().forEach(item -> {
            Barcode barcode = barcodeMap.get(item.getBarcode());
            if (null != barcode) {
                // 一行一件
                BigDecimal quantity = item.getQuantity();
                while (quantity.compareTo(BigDecimal.ZERO) == 1) {
                    RetailReturnNoticeBillGoods goods = RetailReturnNoticeBillGoods.build();
                    goods.setGoodsId(barcode.getGoodsId());
                    goods.setLongId(barcode.getLongId());
                    goods.setColorId(barcode.getColorId());
                    goods.setSizeId(barcode.getSizeId());
                    goods.setBarcode(item.getBarcode());
                    goods.setDiscount(item.getDiscount());
                    goods.setBalancePrice(item.getBalancePrice());
                    goods.setQuantity(BigDecimal.ONE);
                    // TODO 渠道货品吊牌价
                    goods.setTagPrice(BigDecimal.ZERO);
                    goods.setBillId(bill.getId());
                    billGoodsList.add(goods);

                    quantity = quantity.subtract(BigDecimal.ONE);
                }
            }
        });
        context.setBillGoodsList(billGoodsList);
    }

}
