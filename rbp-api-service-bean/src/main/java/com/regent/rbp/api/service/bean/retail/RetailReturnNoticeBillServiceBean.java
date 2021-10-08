package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Barcode;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dao.base.BarcodeDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillDao;
import com.regent.rbp.api.dao.retail.RetailReturnNoticeBillGoodsDao;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.retail.RetailOrderBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillGoodsDetailData;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.api.service.retail.RetailReturnNoticeBillService;
import com.regent.rbp.api.service.retail.context.RetailReturnNoticeBillSaveContext;
import com.regent.rbp.infrastructure.enums.StatusEnum;
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

    @Transactional
    @Override
    public ModelDataResponse<String> save(RetailReturnNoticeBillSaveParam param) {
        boolean createFlag = true;
        RetailReturnNoticeBillSaveContext context = new RetailReturnNoticeBillSaveContext(param);
        // 判断是新增还是更新
        RetailReturnNoticeBill bill = retailReturnNoticeBillDao.selectOne(new QueryWrapper<RetailReturnNoticeBill>().eq("bill_no", param.getBillNo()));
        if (bill != null) {
            context.getBill().setId(bill.getId());
            createFlag = false;
        }
        // 验证数有效性


        return null;
    }

    /**
     * 验证有效性
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(RetailReturnNoticeBillSaveParam param, RetailReturnNoticeBillSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();

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
                RetailReturnNoticeBillGoods goods = RetailReturnNoticeBillGoods.build();
                goods.setGoodsId(barcode.getGoodsId());
                goods.setLongId(barcode.getLongId());
                goods.setColorId(barcode.getColorId());
                goods.setSizeId(barcode.getSizeId());
                goods.setBarcode(item.getBarcode());
                goods.setDiscount(item.getDiscount());
                goods.setBalancePrice(item.getBalancePrice());
                goods.setQuantity(item.getQuantity());
                // TODO 渠道货品吊牌价
                goods.setTagPrice(BigDecimal.ZERO);
                goods.setBillId(bill.getId());
                billGoodsList.add(goods);
            }
        });
        context.setBillGoodsList(billGoodsList);
    }

}
