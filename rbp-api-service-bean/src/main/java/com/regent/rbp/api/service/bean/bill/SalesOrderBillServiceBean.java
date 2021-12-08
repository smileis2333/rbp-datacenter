package com.regent.rbp.api.service.bean.bill;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.coupon.RetailPayType;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.salesOrder.SalesOrderBill;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillGoods;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillPayment;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillSize;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillDao;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillGoodsDao;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillPaymentDao;
import com.regent.rbp.api.dao.salesOrder.SalesOrderBillSizeDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.*;
import com.regent.rbp.api.service.sale.SalesOrderBillService;
import com.regent.rbp.api.service.sale.context.SalesOrderBillQueryContext;
import com.regent.rbp.api.service.sale.context.SalesOrderBillSaveContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 销售单 Bean
 * @author: HaiFeng
 * @create: 2021-11-08 14:45
 */
@Service
public class SalesOrderBillServiceBean implements SalesOrderBillService {

    @Autowired
    ChannelDao channelDao;
    @Autowired
    MemberCardDao memberCardDao;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ColorDao colorDao;
    @Autowired
    LongDao longDao;
    @Autowired
    SizeClassDao sizeClassDao;
    @Autowired
    BarcodeDao barcodeDao;
    @Autowired
    RetailPayTypeDao retailPayTypeDao;
    @Autowired
    PosClassDao posClassDao;
    @Autowired
    SalesOrderBillDao salesOrderBillDao;
    @Autowired
    SalesOrderBillGoodsDao salesOrderBillGoodsDao;
    @Autowired
    SalesOrderBillSizeDao salesOrderBillSizeDao;
    @Autowired
    SalesOrderBillPaymentDao salesOrderBillPaymentDao;


    @Override
    public PageDataResponse<SalesOrderBillQueryResult> query(SaleOrderQueryParam param) {
        SalesOrderBillQueryContext context = new SalesOrderBillQueryContext();

        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);

        //查询数据
        PageDataResponse<SalesOrderBillQueryResult> response = searchPage(context);
        return response;
    }

    /**
     * 将查询参数转换成 查询的上下文
     * @param param
     * @param context
     */
    private void convertQueryContext(SaleOrderQueryParam param, SalesOrderBillQueryContext context) {
        context.setPageNo(param.getPageNo());
        context.setPageSize(param.getPageSize());

        context.setManualId(param.getManualId());
        context.setBillNo(param.getBillNo());
        context.setStatus(param.getStatus());
        context.setNotes(param.getNotes());
        context.setOrigin(param.getOrigin());
        context.setOriginBillNo(param.getOriginBillNo());
        context.setBillType(param.getBillType());

        // 销售渠道
        if (param.getSaleChannelCode() != null && param.getSaleChannelCode().length > 0) {
            List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getSaleChannelCode()));
            if (channelList != null && channelList.size() > 0) {
                long[] ids = channelList.stream().mapToLong(map -> map.getId()).toArray();
                context.setSaleChannelCode(ids);
            }
        }
        // 渠道编号
        if (param.getChannelCode() != null && param.getChannelCode().length > 0) {
            List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getCode, param.getChannelCode()));
            if (channelList != null && channelList.size() > 0) {
                long[] ids = channelList.stream().mapToLong(map -> map.getId()).toArray();
                context.setChannelCode(ids);
            }
        }
        // 会员编号
        if (param.getMemberCode() != null && param.getMemberCode().length > 0) {
            List<MemberCard> memberCardList = memberCardDao.selectList(new LambdaQueryWrapper<MemberCard>().in(MemberCard::getCode, param.getMemberCode()));
            if (memberCardList != null && memberCardList.size() > 0) {
                long[] ids = memberCardList.stream().mapToLong(map -> map.getId()).toArray();
                context.setMemberCode(ids);
            }
        }
        // 单据日期
        if (StringUtil.isNotEmpty(param.getBillDate())) {
            Date billDate = DateUtil.getDate(param.getBillDate(), DateUtil.FULL_DATE_FORMAT);
            context.setBillDate(billDate);
        }
        // 创建日期
        if(StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if(StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        // 修改日期
        if(StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        // 审核日期
        if(StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if(StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateEnd);
        }
    }

    /**
     * 查询渠道数据
     * @param context
     * @return
     */
    private PageDataResponse<SalesOrderBillQueryResult> searchPage(SalesOrderBillQueryContext context) {

        PageDataResponse<SalesOrderBillQueryResult> result = new PageDataResponse<SalesOrderBillQueryResult>();
        Page<SalesOrderBill> pageModel = new Page<SalesOrderBill>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);

        IPage<SalesOrderBill> salesPageData = salesOrderBillDao.selectPage(pageModel, queryWrapper);
        List<SalesOrderBillQueryResult> list = convertQueryResult(salesPageData.getRecords());

        result.setTotalCount(salesPageData.getTotal());
        result.setData(list);

        return result;
    }

    /**
     * 整理查询条件构造器
     * @param context
     * @return
     */
    private QueryWrapper processQueryWrapper(SalesOrderBillQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<SalesOrderBill>();

        if (StringUtil.isNotEmpty(context.getManualId()))
            queryWrapper.eq("manual_id", context.getManualId());
        if (StringUtil.isNotEmpty(context.getBillNo()))
            queryWrapper.eq("bill_no", context.getBillNo());
        if (context.getBillDate() != null)
            queryWrapper.eq("bill_date", context.getBillDate());
        if (context.getSaleChannelCode() != null)
            queryWrapper.in("sale_channel_id", context.getSaleChannelCode());
        if (context.getChannelCode() != null)
            queryWrapper.in("channel_id", context.getChannelCode());
        if (context.getMemberCode() != null)
            queryWrapper.in("member_id", context.getMemberCode());
        if (context.getStatus() != null)
            queryWrapper.in("status", context.getStatus());
        if (StringUtil.isNotEmpty(context.getNotes()))
            queryWrapper.eq("notes", context.getNotes());
        if (context.getOrigin() != null)
            queryWrapper.in("origin", context.getOrigin());
        if (StringUtil.isNotEmpty(context.getOriginBillNo()))
            queryWrapper.eq("origin_bill_no", context.getOriginBillNo());
        if (context.getBillType() != null)
            queryWrapper.in("bill_type", context.getBillType());

        if(context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if(context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if(context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if(context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if(context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if(context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }
        return queryWrapper;
    }

    /**
     * 处理查询结果的属性
     * @param list
     * @return
     */
    private List<SalesOrderBillQueryResult> convertQueryResult(List<SalesOrderBill> list) {
        List<SalesOrderBillQueryResult> queryResults = new ArrayList<>(list.size());

        List<Long> saleIds = list.stream().map(SalesOrderBill::getId).distinct().collect(Collectors.toList());

        // 销售明细,付款方式
        List<SalesOrderBillSize> salesOrderBillSizeList = salesOrderBillSizeDao.selectList(new LambdaQueryWrapper<SalesOrderBillSize>().in(SalesOrderBillSize::getBillId, saleIds));
        List<SalesOrderBillGoods> salesOrderBillGoodsList = salesOrderBillGoodsDao.selectList(new LambdaQueryWrapper<SalesOrderBillGoods>().in(SalesOrderBillGoods::getBillId, saleIds));
        List<SalesOrderBillPayment> salesOrderBillPaymentList = salesOrderBillPaymentDao.selectList(new LambdaQueryWrapper<SalesOrderBillPayment>().in(SalesOrderBillPayment::getBillId, saleIds));

        Map<Long, List<SalesOrderBillSize>> saleSizeMap = salesOrderBillSizeList.stream().collect(Collectors.groupingBy(SalesOrderBillSize::getBillId));
        Map<Long, List<SalesOrderBillGoods>> saleGoodsMap = salesOrderBillGoodsList.stream().collect(Collectors.groupingBy(SalesOrderBillGoods::getBillId));
        Map<Long, List<SalesOrderBillPayment>> salePayMap = salesOrderBillPaymentList.stream().collect(Collectors.groupingBy(SalesOrderBillPayment::getBillId));

        // 货品
        List<Long> goodsIds = salesOrderBillSizeList.stream().map(SalesOrderBillSize::getGoodsId).distinct().collect(Collectors.toList());
        List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getId, goodsIds));
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getCode));

        // 颜色
        List<Long> colorIds = salesOrderBillSizeList.stream().map(SalesOrderBillSize::getColorId).distinct().collect(Collectors.toList());
        List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getId, colorIds));
        Map<Long, String> colorMap = colorList.stream().collect(Collectors.toMap(Color::getId, Color::getName));

        // 内长
        List<Long> longIds = salesOrderBillSizeList.stream().map(SalesOrderBillSize::getLongId).distinct().collect(Collectors.toList());
        List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getId, longIds));
        Map<Long, String> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getId, LongInfo::getName));

        // 尺码
        List<Long> sizeIds = salesOrderBillSizeList.stream().map(SalesOrderBillSize::getSizeId).distinct().collect(Collectors.toList());
        List<SizeClass> sizeList = sizeClassDao.selectList(new LambdaQueryWrapper<SizeClass>().in(SizeClass::getId, sizeIds));
        Map<Long, String> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeClass::getId, SizeClass::getName));

        // 条码
        List<Long> barcodeIds = salesOrderBillSizeList.stream().map(SalesOrderBillSize::getBarcodeId).distinct().collect(Collectors.toList());
        List<Barcode> barcodeList = barcodeDao.selectList(new LambdaQueryWrapper<Barcode>().in(Barcode::getId, barcodeIds));
        Map<Long, String> barcodeMap = barcodeList.stream().collect(Collectors.toMap(Barcode::getId, Barcode::getBarcode));

        // 零售付款方式
        List<Long> payIds = salesOrderBillPaymentList.stream().map(SalesOrderBillPayment::getRetailPayTypeId).collect(Collectors.toList());
        List<RetailPayType> retailPayTypeList = retailPayTypeDao.selectList(new LambdaQueryWrapper<RetailPayType>().in(RetailPayType::getId, payIds));
        Map<Long, String> payMap = retailPayTypeList.stream().collect(Collectors.toMap(RetailPayType::getId, RetailPayType::getCode));

        // 销售渠道&渠道
        List<Long> channelIds = list.stream().map(SalesOrderBill::getSaleChannelId).distinct().collect(Collectors.toList());
        channelIds.addAll(list.stream().map(SalesOrderBill::getChannelId).distinct().collect(Collectors.toList()));
        List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().in(Channel::getId, channelIds));
        Map<Long, String> channelMap = channelList.stream().collect(Collectors.toMap(Channel::getId, Channel::getCode));

        // 会员
        List<Long> memberIds = list.stream().map(SalesOrderBill::getMemberId).distinct().collect(Collectors.toList());
        List<MemberCard> memberCardList = memberCardDao.selectList(new LambdaQueryWrapper<MemberCard>().in(MemberCard::getId, memberIds));
        Map<Long, String> memberCardMap = memberCardList.stream().collect(Collectors.toMap(MemberCard::getId, MemberCard::getCode));

        
        for (SalesOrderBill bill : list) {
            SalesOrderBillQueryResult queryResult = new SalesOrderBillQueryResult();
            queryResult.setManualId(bill.getManualId());
            queryResult.setBillNo(bill.getBillNo());
            queryResult.setStatus(bill.getStatus());
            queryResult.setNotes(bill.getNotes());
            queryResult.setOrigin(bill.getOrigin());
            queryResult.setOriginBillNo(bill.getOriginBillNo());
            queryResult.setBillType(bill.getBillType());

            // 单据日期
            String billDate = DateUtil.getDateStr(bill.getBillDate(), DateUtil.SHORT_DATE_FORMAT);
            queryResult.setBillDate(billDate);

            // 销售渠道
            if (bill.getSaleChannelId() != null && channelMap.containsKey(bill.getSaleChannelId())) {
                queryResult.setSaleChannelCode(channelMap.get(bill.getSaleChannelId()));
            }
            // 渠道
            if (bill.getChannelId() != null && channelMap.containsKey(bill.getChannelId())) {
                queryResult.setChannelCode(channelMap.get(bill.getChannelId()));
            }
            // 会员
            if (bill.getMemberId() != null && memberCardMap.containsKey(bill.getMemberId())) {
                queryResult.setMemberCode(memberCardMap.get(bill.getMemberId()));
            }
            // 创建日期
            if (bill.getCreatedTime() != null) {
                String date = DateUtil.getDateStr(bill.getCreatedTime(), DateUtil.FULL_DATE_FORMAT);
                queryResult.setCreatedTime(date);
            }
            // 更新日期
            if (bill.getUpdatedTime() != null) {
                String date = DateUtil.getDateStr(bill.getUpdatedTime(), DateUtil.FULL_DATE_FORMAT);
                queryResult.setUpdatedTime(date);
            }
            // 审核日期
            if (bill.getCheckTime() != null) {
                String date = DateUtil.getDateStr(bill.getCheckTime(), DateUtil.FULL_DATE_FORMAT);
                queryResult.setCheckTime(date);
            }

            List<SalesOrderBillGoodsResult> goodsQueryResultList = new ArrayList<>();
            // 货品明细
            if (saleSizeMap.containsKey(bill.getId())) {
                List<SalesOrderBillGoods> salesGoodsList = saleGoodsMap.get(bill.getId());
                for (SalesOrderBillGoods billGoods : salesGoodsList) {
                    List<SalesOrderBillSize> salesSizeList = saleSizeMap.get(bill.getId()).
                            stream().filter(f -> f.getBillGoodsId().equals(billGoods.getId())).collect(Collectors.toList());
                    for (SalesOrderBillSize billSize : salesSizeList) {
                        SalesOrderBillGoodsResult goodsQueryResult = new SalesOrderBillGoodsResult();

                        goodsQueryResult.setSaleType(billGoods.getSaleType());
                        goodsQueryResult.setTagPrice(billGoods.getTagPrice());
                        goodsQueryResult.setRetailPrice(billGoods.getRetailPrice());
                        goodsQueryResult.setSalesPrice(billGoods.getSalesPrice());
                        goodsQueryResult.setOriginalPrice(billGoods.getOriginalPrice());
                        goodsQueryResult.setQuantity(billSize.getQuantity());
                        goodsQueryResult.setPoint(billGoods.getPoint());
                        goodsQueryResult.setTagDiscount(billGoods.getTagDiscount());
                        goodsQueryResult.setRetailDiscount(billGoods.getRetailDiscount());
                        goodsQueryResult.setBalanceDiscount(billGoods.getBalanceDiscount());

                        // 条码
                        if (billSize.getBarcodeId() != null && barcodeMap.containsKey(billSize.getBarcodeId())) {
                            goodsQueryResult.setBarcode(barcodeMap.get(billSize.getBarcodeId()));
                        }
                        // 货号
                        if (billSize.getGoodsId() != null && goodsMap.containsKey(billSize.getGoodsId())) {
                            goodsQueryResult.setGoodsCode(goodsMap.get(billSize.getGoodsId()));
                        }
                        // 颜色
                        if (billSize.getColorId() != null && colorMap.containsKey(billSize.getColorId())) {
                            goodsQueryResult.setColorCode(colorMap.get(billSize.getColorId()));
                        }
                        // 内长
                        if (billSize.getLongId() != null && longMap.containsKey(billSize.getLabelId())) {
                            goodsQueryResult.setLongName(longMap.get(billSize.getLongId()));
                        }
                        // 尺码
                        if (billSize.getSizeId() != null && sizeMap.containsKey(billSize.getSizeId())) {
                            goodsQueryResult.setSize(sizeMap.get(billSize.getSizeId()));
                        }
                        goodsQueryResultList.add(goodsQueryResult);
                    }
                }
            }
            queryResult.setGoodsDetailData(goodsQueryResultList);

            // 付款方式
            List<SalesOrderBillPaymentResult> paymentQueryResultList = new ArrayList<>();
            if (salePayMap.containsKey(bill.getId())) {
                List<SalesOrderBillPayment> paymentList = salePayMap.get(bill.getId());
                for (SalesOrderBillPayment pay : paymentList) {
                    SalesOrderBillPaymentResult paymentQueryResult = new SalesOrderBillPaymentResult();
                    if (pay.getRetailPayTypeId() != null && payMap.containsKey(pay.getRetailPayTypeId())) {
                        paymentQueryResult.setRetailPayTypeCode(payMap.get(pay.getRetailPayTypeId()));
                    }
                    paymentQueryResult.setPayMoney(pay.getPayMoney());
                    paymentQueryResult.setReturnMoney(pay.getReturnMoney());
                    paymentQueryResultList.add(paymentQueryResult);
                }
            }
            queryResult.setRetailPayTypeData(paymentQueryResultList);
            queryResults.add(queryResult);
        }
        return queryResults;
    }

    @Override
    public DataResponse save(SaleOrderSaveParam param) {
        SalesOrderBillSaveContext context = new SalesOrderBillSaveContext(param);

        // 验证属性
        List<String> errorMsgList = verificationProperty(param, context);
        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        // 写入销售单
        this.save(context);
        return DataResponse.success();
    }

    /**
     * 验证属性
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(SaleOrderSaveParam param, SalesOrderBillSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        SalesOrderBill salesOrderBill = context.getSalesOrderBill();
        List<SalesOrderBillGoods> salesOrderBillGoodsList = context.getSalesOrderBillGoodsList();
        List<SalesOrderBillSize> salesOrderBillSizeList = context.getSalesOrderBillSizeList();
        List<SalesOrderBillPayment> salesOrderBillPaymentList = context.getSalesOrderBillPaymentList();

        if (param.getBillNo() == null || param.getBillNo().length() == 0) {
            errorMsgList.add("单号(billNo)不能为空");
        }
        if (param.getBillDate() == null || param.getBillDate().length() == 0) {
            errorMsgList.add("单据日期(billDate)不能为空");
        }
        if (param.getSaleChannelCode() == null || param.getSaleChannelCode().length() == 0) {
            errorMsgList.add("销售渠道编号(saleChannelCode)不能为空");
        } else {
            Channel channel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getSaleChannelCode()));
            if (channel == null) {
                errorMsgList.add("销售渠道编号(saleChannelCode)不存在");
            } else {
                salesOrderBill.setSaleChannelId(channel.getId());
            }
        }
        if (param.getChannelCode() != null && param.getChannelCode().length() > 0) {
            Channel channel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
            if (channel == null) {
                errorMsgList.add("渠道编号(channelCode)不存在");
            } else {
                salesOrderBill.setChannelId(channel.getId());
            }
        }
        if (param.getShiftNo() == null || param.getShiftNo().length() == 0) {
            errorMsgList.add("班次编号(shiftNo)不能为空");
        } else {
            PosClass posClass = posClassDao.selectOne(new LambdaQueryWrapper<PosClass>().eq(PosClass::getCode, param.getShiftNo()));
            if (posClass == null) {
                errorMsgList.add("班次编号(shiftNo)不存在");
            } else {
                salesOrderBill.setShiftId(posClass.getId());
            }
        }

        if (param.getMemberCode() != null && param.getMemberCode().length() > 0) {
            MemberCard memberCard = memberCardDao.selectOne(new LambdaQueryWrapper<MemberCard>().eq(MemberCard::getCode, param.getMemberCode()));
            if (memberCard == null) {
                errorMsgList.add("会员编号(memberCode)不存在");
            } else {
                salesOrderBill.setMemberId(memberCard.getId());
            }
        }
        if (param.getOriginBillNo() != null && param.getOriginBillNo().length() > 0) {
            SalesOrderBill sale = salesOrderBillDao.selectOne(new LambdaQueryWrapper<SalesOrderBill>().eq(SalesOrderBill::getBillNo, param.getOriginBillNo()));
            if (sale == null) {
                errorMsgList.add("原单号(originBillNo)不存在");
            } else {
                salesOrderBill.setOriginBillId(sale.getId());
                salesOrderBill.setOriginBillNo(sale.getBillNo());
            }
        }
        if (param.getOrigin() == null) {
            errorMsgList.add("来源(origin)不能为空");
        } else {
            List<Integer> originList = new ArrayList<>();
            originList.add(0);
            originList.add(1);
            originList.add(2);
            if (originList.stream().filter(f -> f.equals(param.getOrigin())).count() == 0) {
                errorMsgList.add("来源(origin)内容必须是 (0.Pos;1.后台;2.第三方平台)");
            }
        }
        if (param.getStatus() == null) {
            errorMsgList.add("单据状态(status)不能为空");
        } else {
            List<Integer> statusList = new ArrayList<>();
            statusList.add(0);
            statusList.add(1);
            statusList.add(2);
            statusList.add(3);
            if (statusList.stream().filter(f -> f.equals(param.getStatus())).count() == 0) {
                errorMsgList.add("单据状态(status)内容必须是 (0.未审核,1.已审核,2.反审核,3.已作废)");
            }
        }
        if (param.getBillType() == null) {
            errorMsgList.add("单据类型(billType)不能为空");
        } else {
            List<Integer> billTypeList = new ArrayList<>();
            billTypeList.add(0);
            billTypeList.add(1);
            billTypeList.add(2);
            billTypeList.add(3);
            billTypeList.add(4);
            if (billTypeList.stream().filter(f -> f.equals(param.getBillType())).count() == 0) {
                errorMsgList.add("单据类型(billType)内容必须是 (0.线下销售 1.全渠道发货 2.线上发货 3.线上退货 4.定金)");
            }
        }
        if (param.getGoodsDetailData() == null || param.getGoodsDetailData().size() == 0) {
            errorMsgList.add("货品明细(goodsDetailData)不能为空");
        } else {
            if (StringUtils.isBlank(param.getGoodsDetailData().get(0).getBarcode())) {
                // 货品+颜色+内长+尺码
                List<String> goodsNos = param.getGoodsDetailData().stream().map(SalesOrderBillGoodsResult::getGoodsCode).distinct().collect(Collectors.toList());
                List<Goods> goodsList = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getCode, goodsNos));
                Map<String, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getCode, t->t));

                List<String> colorNos = param.getGoodsDetailData().stream().map(SalesOrderBillGoodsResult::getColorCode).distinct().collect(Collectors.toList());
                List<Color> colorList = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getCode, colorNos));
                Map<String, Color> colorMap = colorList.stream().collect(Collectors.toMap(Color::getCode, t->t));

                List<String> longNos = param.getGoodsDetailData().stream().map(SalesOrderBillGoodsResult::getLongName).distinct().collect(Collectors.toList());
                List<LongInfo> longList = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getName, longNos));
                Map<String, LongInfo> longMap = longList.stream().collect(Collectors.toMap(LongInfo::getName, t->t));

                List<String> sizeNos = param.getGoodsDetailData().stream().map(SalesOrderBillGoodsResult::getSize).distinct().collect(Collectors.toList());
                List<SizeClass> sizeList = sizeClassDao.selectList(new LambdaQueryWrapper<SizeClass>().in(SizeClass::getName, sizeNos));
                Map<String, SizeClass> sizeMap = sizeList.stream().collect(Collectors.toMap(SizeClass::getName, t->t));

                Integer rowIndex = 1;
                for (SalesOrderBillGoodsResult goodsResult : param.getGoodsDetailData()) {
                    Barcode barcode = new Barcode();
                    // 货品
                    if (!goodsMap.containsKey(goodsResult.getGoodsCode())) {
                        errorMsgList.add(String.format("货号(goodsCode) %s 不存在", goodsResult.getGoodsCode()));
                    } else {
                        barcode.setGoodsId(goodsMap.get(goodsResult.getGoodsCode()).getId());
                    }
                    // 颜色
                    if (!colorMap.containsKey(goodsResult.getColorCode())) {
                        errorMsgList.add(String.format("颜色编号(colorCode) %s 不存在", goodsResult.getColorCode()));
                    } else {
                        barcode.setColorId(colorMap.get(goodsResult.getColorCode()).getId());
                    }
                    // 内长
                    if (!longMap.containsKey(goodsResult.getLongName())) {
                        errorMsgList.add(String.format("内长(longName) %s 不存在", goodsResult.getLongName()));
                    } else {
                        barcode.setColorId(longMap.get(goodsResult.getLongName()).getId());
                    }
                    // 尺码
                    if (!sizeMap.containsKey(goodsResult.getSize())) {
                        errorMsgList.add(String.format("尺码(size) %s 不存在", goodsResult.getSize()));
                    } else {
                        barcode.setColorId(sizeMap.get(goodsResult.getSize()).getId());
                    }

                    SalesOrderBillGoods billGoods = this.giveSalesOrderBillGoods(salesOrderBill, goodsResult, barcode.getGoodsId());
                    salesOrderBillGoodsList.add(billGoods);

                    SalesOrderBillSize billSize = this.giveSalesOrderBillSize(salesOrderBill, barcode);
                    billSize.setBillGoodsId(billGoods.getId());
                    billSize.setQuantity(goodsResult.getQuantity());
                    billSize.setRowIndex(rowIndex);
                    salesOrderBillSizeList.add(billSize);
                }
            } else {
                // 条码
                List<String> barcodes =  param.getGoodsDetailData().stream().map(SalesOrderBillGoodsResult::getBarcode).distinct().collect(Collectors.toList());
                List<Barcode> barcodeList = barcodeDao.selectList(new LambdaQueryWrapper<Barcode>().in(Barcode::getBarcode, barcodes));
                Map<String, Barcode> barcodeMap = barcodeList.stream().collect(Collectors.toMap(Barcode::getBarcode, t -> t));
                Integer rowIndex = 1;
                for (SalesOrderBillGoodsResult goodsResult : param.getGoodsDetailData()) {
                    if (!barcodeMap.containsKey(goodsResult.getBarcode())) {
                        errorMsgList.add(String.format("条形码(barcode) %s 不存在", goodsResult.getBarcode()));
                    } else {
                        Barcode barcode = barcodeMap.get(goodsResult.getBarcode());
                        SalesOrderBillGoods billGoods = this.giveSalesOrderBillGoods(salesOrderBill, goodsResult, barcode.getGoodsId());
                        salesOrderBillGoodsList.add(billGoods);

                        SalesOrderBillSize billSize = this.giveSalesOrderBillSize(salesOrderBill, barcode);
                        billSize.setBillGoodsId(billGoods.getId());
                        billSize.setQuantity(goodsResult.getQuantity());
                        billSize.setRowIndex(rowIndex);
                        salesOrderBillSizeList.add(billSize);
                    }
                    rowIndex++;
                }
            }
        }
        if (param.getRetailPayTypeData() != null ) {
            for (SalesOrderBillPaymentResult paymentResult : param.getRetailPayTypeData()) {
                RetailPayType payType = retailPayTypeDao.selectOne(new LambdaQueryWrapper<RetailPayType>().eq(RetailPayType::getCode, paymentResult.getRetailPayTypeCode()));
                if (payType == null) {
                    errorMsgList.add(String.format("付款方式(retailPayTypeCode) %s 不存在", paymentResult.getRetailPayTypeCode()));
                } else {
                    SalesOrderBillPayment payment = new SalesOrderBillPayment();
                    payment.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                    payment.setBillId(salesOrderBill.getId());
                    payment.setRetailPayTypeId(payType.getId());
                    payment.setPayMoney(paymentResult.getPayMoney());
                    payment.setReturnMoney(paymentResult.getReturnMoney());
                    payment.setCreatedBy(salesOrderBill.getCreatedBy());
                    payment.setCreatedTime(salesOrderBill.getCreatedTime());
                    payment.setUpdatedBy(salesOrderBill.getUpdatedBy());
                    payment.setUpdatedTime(salesOrderBill.getUpdatedTime());

                    salesOrderBillPaymentList.add(payment);
                }
            }
        }
        
        return errorMsgList;
    }

    private SalesOrderBillGoods giveSalesOrderBillGoods(SalesOrderBill salesOrderBill, SalesOrderBillGoodsResult goodsResult, Long goodsId) {
        SalesOrderBillGoods billGoods = new SalesOrderBillGoods();
        billGoods.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billGoods.setBillId(salesOrderBill.getId());
        billGoods.setGoodsId(goodsId);
        billGoods.setStatus(goodsResult.getStatus());
        billGoods.setSaleType(goodsResult.getSaleType());
        billGoods.setTagPrice(goodsResult.getTagPrice());
        billGoods.setTagDiscount(goodsResult.getTagDiscount());
        billGoods.setRetailPrice(goodsResult.getRetailPrice());
        billGoods.setRetailDiscount(goodsResult.getRetailDiscount());
        billGoods.setBalancePrice(goodsResult.getSalesPrice());
        billGoods.setBalanceDiscount(goodsResult.getBalanceDiscount());
        billGoods.setOriginalPrice(goodsResult.getOriginalPrice());
        billGoods.setQuantity(goodsResult.getQuantity());
        billGoods.setCreatedBy(salesOrderBill.getCreatedBy());
        billGoods.setCreatedTime(salesOrderBill.getCreatedTime());
        billGoods.setUpdatedBy(salesOrderBill.getUpdatedBy());
        billGoods.setUpdatedTime(salesOrderBill.getUpdatedTime());
        return billGoods;
    }

    private SalesOrderBillSize giveSalesOrderBillSize(SalesOrderBill salesOrderBill, Barcode barcode) {
        SalesOrderBillSize billSize = new SalesOrderBillSize();
        billSize.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        billSize.setBillId(salesOrderBill.getId());
        billSize.setGoodsId(barcode.getGoodsId());
        billSize.setColorId(barcode.getColorId());
        billSize.setLabelId(barcode.getLongId());
        billSize.setSizeId(barcode.getSizeId());
        if (barcode.getId() != null) {
            billSize.setBarcodeId(barcode.getId());
        }
        billSize.setCreatedBy(salesOrderBill.getCreatedBy());
        billSize.setCreatedTime(salesOrderBill.getCreatedTime());
        billSize.setUpdatedBy(salesOrderBill.getUpdatedBy());
        billSize.setUpdatedTime(salesOrderBill.getUpdatedTime());
        return billSize;
    }


    /**
     * 写入销售单
     * @param context
     */
    private void save(SalesOrderBillSaveContext context) {
        salesOrderBillDao.insert(context.getSalesOrderBill());
        for (SalesOrderBillGoods goods : context.getSalesOrderBillGoodsList()) {
            salesOrderBillGoodsDao.insert(goods);
        }
        for (SalesOrderBillSize size : context.getSalesOrderBillSizeList()) {
            salesOrderBillSizeDao.insert(size);
        }
        for (SalesOrderBillPayment payment : context.getSalesOrderBillPaymentList()) {
            salesOrderBillPaymentDao.insert(payment);
        }
    }



}