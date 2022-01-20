package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.coupon.RetailPayType;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.core.goods.Goods;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.salesOrder.*;
import com.regent.rbp.api.dao.base.*;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dao.goods.GoodsDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.salesOrder.*;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.sale.*;
import com.regent.rbp.api.dto.validate.group.Complex;
import com.regent.rbp.api.service.sale.SalesOrderBillService;
import com.regent.rbp.api.service.sale.context.SalesOrderBillQueryContext;
import com.regent.rbp.api.service.sale.context.SalesOrderBillSaveContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
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
    @Autowired
    private Validator validator;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    EmployeeBillAchievementDao employeeBillAchievementDao;
    @Autowired
    EmployeeGoodsAchievementDao employeeGoodsAchievementDao;

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
     *
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
        if (StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if (StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        // 修改日期
        if (StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        // 审核日期
        if (StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if (StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateEnd);
        }
    }

    /**
     * 查询渠道数据
     *
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
     *
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

        if (context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if (context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if (context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if (context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if (context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if (context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }
        return queryWrapper;
    }

    /**
     * 处理查询结果的属性
     *
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
    @Transactional
    public DataResponse save(SaleOrderSaveParam param) {
        SalesOrderBillSaveContext context = new SalesOrderBillSaveContext(param);

        // 验证属性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        // 写入销售单
        this.save(context);
        return DataResponse.success();
    }

    /**
     * 验证属性
     *
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

        Channel saleChannel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getSaleChannelCode()));
        salesOrderBill.setSaleChannelId(saleChannel.getId());

        Channel channel = channelDao.selectOne(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
        salesOrderBill.setChannelId(channel.getId());

        PosClass posClass = posClassDao.selectOne(new LambdaQueryWrapper<PosClass>().eq(PosClass::getCode, param.getShiftNo()));
        if (posClass == null) {
            errorMsgList.add("班次编号(shiftNo)不存在");
        } else {
            salesOrderBill.setShiftId(posClass.getId());
        }

        if (StrUtil.isNotBlank(param.getMemberCode())) {
            MemberCard memberCard = memberCardDao.selectOne(new LambdaQueryWrapper<MemberCard>().eq(MemberCard::getCode, param.getMemberCode()));
            if (memberCard == null) {
                errorMsgList.add("会员编号(memberCode)不存在");
            } else {
                salesOrderBill.setMemberId(memberCard.getId());
            }
        }

        if (StrUtil.isNotBlank(param.getOriginBillNo())) {
            SalesOrderBill sale = salesOrderBillDao.selectOne(new LambdaQueryWrapper<SalesOrderBill>().eq(SalesOrderBill::getBillNo, param.getOriginBillNo()));
            salesOrderBill.setOriginBillId(sale.getId());
            salesOrderBill.setOriginBillNo(sale.getBillNo());
        }

        // inject config
        List<String> employeeCodes = param.getAllEmployeeCodes();
        Map<String, Long> employeeCodeIdMap = CollUtil.isEmpty(employeeCodes) ? Collections.emptyMap() : employeeDao.selectList(new QueryWrapper<Employee>().in(CollUtil.isNotEmpty(employeeCodes), "code", employeeCodes)).stream().collect(Collectors.toMap(Employee::getCode, Employee::getId));
        List<SalesOrderBillGoodsResult> goodsDetailData = param.getGoodsDetailData();
        if (CollUtil.isNotEmpty(param.getEmployeeBillAchievement())) {
            param.getEmployeeBillAchievement().forEach(em -> em.setEmployeeId(employeeCodeIdMap.get(em.getEmployeeCode())));
        } else {
            goodsDetailData.stream().map(SalesOrderBillGoodsResult::getEmployeeGoodsAchievement).filter(ObjectUtil::isNotNull).flatMap(Collection::stream).forEach(em -> {
                em.setEmployeeId(employeeCodeIdMap.get(em.getEmployeeCode()));
            });
        }

        // inject employee info
        if (CollUtil.isNotEmpty(param.getRetailPayTypeData())) {
            Map<String, Long> payTypeIdMap = retailPayTypeDao.selectList(new LambdaQueryWrapper<RetailPayType>().in(RetailPayType::getCode, CollUtil.distinct(CollUtil.map(param.getRetailPayTypeData(), SalesOrderBillPaymentResult::getRetailPayTypeCode, true)))).stream().collect(Collectors.toMap(RetailPayType::getCode, RetailPayType::getId));
            param.getRetailPayTypeData().forEach(e -> {
                e.setRetailPayTypeId(payTypeIdMap.get(e.getRetailPayTypeCode()));
            });
        }

        // inject goods info
        boolean runBarcodeAsGoodInfoSource = StringUtils.isNotBlank(goodsDetailData.get(0).getBarcode());
        if (runBarcodeAsGoodInfoSource) {
            List<String> barcodes = goodsDetailData.stream().map(SalesOrderBillGoodsResult::getBarcode).filter(ObjectUtil::isNotNull).distinct().collect(Collectors.toList());
            Map<String, Barcode> barcodeMap = CollUtil.isEmpty(barcodes) ? Collections.emptyMap() : barcodeDao.selectList(new QueryWrapper<Barcode>().in("barcode", barcodes)).stream().collect(Collectors.toMap(Barcode::getBarcode, Function.identity()));
            goodsDetailData.forEach(e -> {
                Barcode barcode;
                if ((barcode = barcodeMap.get(e.getBarcode())) != null) {
                    e.setGoodsId(barcode.getGoodsId());
                    e.setColorId(barcode.getColorId());
                    e.setLongId(barcode.getLongId());
                    e.setSizeId(barcode.getSizeId());
                    e.setBarcodeId(barcode.getId());
                }
            });
        } else {
            // 货品+颜色+内长+尺码
            Map<String, Long> goodsCodeIdMap = goodsDao.selectList(new LambdaQueryWrapper<Goods>().in(Goods::getCode, goodsDetailData.stream().map(SalesOrderBillGoodsResult::getGoodsCode).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(Goods::getCode, Goods::getId));
            Map<String, Long> colorCodeIdMap = colorDao.selectList(new LambdaQueryWrapper<Color>().in(Color::getCode, goodsDetailData.stream().map(SalesOrderBillGoodsResult::getColorCode).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(Color::getCode, Color::getId));
            Map<String, Long> longNameIdMap = longDao.selectList(new LambdaQueryWrapper<LongInfo>().in(LongInfo::getName, goodsDetailData.stream().map(SalesOrderBillGoodsResult::getLongName).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(LongInfo::getName, LongInfo::getId));
            Map<String, Long> sizeIdMap = sizeClassDao.selectList(new LambdaQueryWrapper<SizeClass>().in(SizeClass::getName, goodsDetailData.stream().map(SalesOrderBillGoodsResult::getSize).distinct().collect(Collectors.toList()))).stream().collect(Collectors.toMap(SizeClass::getName, SizeClass::getId));

            goodsDetailData.forEach(e -> {
                e.setGoodsId(goodsCodeIdMap.get(e.getGoodsCode()));
                e.setColorId(colorCodeIdMap.get(e.getColorCode()));
                e.setLongId(longNameIdMap.get(e.getLongName()));
                e.setSizeId(sizeIdMap.get(e.getSize()));
            });
        }

        // manual trigger validate
        if (!ValidateMessageUtil.pass(validator.validate(param, Complex.class), errorMsgList)) return errorMsgList;

        // process convert
        if (CollUtil.isNotEmpty(param.getRetailPayTypeData())) {
            param.getRetailPayTypeData().forEach(e -> {
                SalesOrderBillPayment payment = new SalesOrderBillPayment();
                payment.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                payment.setBillId(salesOrderBill.getId());
                payment.setRetailPayTypeId(e.getRetailPayTypeId());
                payment.setPayMoney(e.getPayMoney());
                payment.setReturnMoney(e.getReturnMoney());
                payment.setCreatedBy(salesOrderBill.getCreatedBy());
                payment.setCreatedTime(salesOrderBill.getCreatedTime());
                payment.setUpdatedBy(salesOrderBill.getUpdatedBy());
                payment.setUpdatedTime(salesOrderBill.getUpdatedTime());
                salesOrderBillPaymentList.add(payment);
            });
        }

        if (runBarcodeAsGoodInfoSource) {
            // 条码
            Integer rowIndex = 1;
            for (SalesOrderBillGoodsResult goodsResult : goodsDetailData) {
                Barcode realBarcode = BeanUtil.copyProperties(goodsResult, Barcode.class);
                realBarcode.setId(goodsResult.getBarcodeId());
                SalesOrderBillGoods billGoods = giveSalesOrderBillGoods(salesOrderBill, goodsResult, realBarcode.getGoodsId());
                salesOrderBillGoodsList.add(billGoods);
                SalesOrderBillSize billSize = giveSalesOrderBillSize(salesOrderBill, realBarcode);
                billSize.setBillGoodsId(billGoods.getId());
                billSize.setQuantity(goodsResult.getQuantity());
                billSize.setRowIndex(rowIndex);
                salesOrderBillSizeList.add(billSize);
                goodsResult.setRowIndex(rowIndex);
                rowIndex++;
            }
        } else {
            Integer rowIndex = 1;
            for (SalesOrderBillGoodsResult goodsResult : goodsDetailData) {
                Barcode fakeBarcode = BeanUtil.copyProperties(goodsResult, Barcode.class);
                SalesOrderBillGoods billGoods = giveSalesOrderBillGoods(salesOrderBill, goodsResult, fakeBarcode.getGoodsId());
                salesOrderBillGoodsList.add(billGoods);
                SalesOrderBillSize billSize = giveSalesOrderBillSize(salesOrderBill, fakeBarcode);
                billSize.setBillGoodsId(billGoods.getId());
                billSize.setQuantity(goodsResult.getQuantity());
                billSize.setRowIndex(rowIndex);
                salesOrderBillSizeList.add(billSize);
                goodsResult.setRowIndex(rowIndex);
                rowIndex++;
            }
        }

        if (CollUtil.isNotEmpty(param.getEmployeeBillAchievement())) {
            context.setEmployeeBillAchievements(
                    param.getEmployeeBillAchievement().stream().map(e -> {
                        EmployeeBillAchievement employeeBillAchievement = new EmployeeBillAchievement();
                        employeeBillAchievement.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                        employeeBillAchievement.setEmployeeId(e.getEmployeeId());
                        employeeBillAchievement.setBillId(salesOrderBill.getId());
                        employeeBillAchievement.setBillType(1);
                        employeeBillAchievement.setShareAmount(e.getShareAmount());
                        employeeBillAchievement.setShareRate(e.getShareRate());
                        return employeeBillAchievement;
                    }).collect(Collectors.toList()));
        }

        context.setEmployeeGoodsAchievements(goodsDetailData.stream().map(gds -> {
            List<EmployeeAchievement> employeeGoodsAchievements = gds.getEmployeeGoodsAchievement();
            return CollUtil.isEmpty(employeeGoodsAchievements) ? null : employeeGoodsAchievements.stream().map(e -> {
                EmployeeGoodsAchievement employeeGoodsAchievement = BeanUtil.copyProperties(e, EmployeeGoodsAchievement.class);
                employeeGoodsAchievement.setGoodsId(gds.getGoodsId());
                employeeGoodsAchievement.setColorId(gds.getColorId());
                employeeGoodsAchievement.setSizeId(gds.getSizeId());
                employeeGoodsAchievement.setLongId(gds.getLongId());
                employeeGoodsAchievement.setBillId(salesOrderBill.getId());
                employeeGoodsAchievement.setBillType(1);
                employeeGoodsAchievement.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                employeeGoodsAchievement.setQuantity(gds.getQuantity());
                employeeGoodsAchievement.setEmployeeId(e.getEmployeeId());
                employeeGoodsAchievement.setRowIndex(gds.getRowIndex());
                employeeGoodsAchievement.setShareAmount(e.getShareAmount());
                employeeGoodsAchievement.setShareRate(e.getShareRate());
                return employeeGoodsAchievement;
            }).collect(Collectors.toList());
        }).filter(ObjectUtil::isNotNull).flatMap(Collection::stream).collect(Collectors.toList()));

        // 计算汇总数据
        salesOrderBill.setSumSkuQuantity(goodsDetailData.stream().map(SalesOrderBillGoodsResult::getQuantity).collect(summingUp()));
        salesOrderBill.setSumItemQuantity(new BigDecimal(goodsDetailData.size()));
        salesOrderBill.setSumRetailAmount(goodsDetailData.stream().map(e->e.getRetailPrice().multiply(e.getQuantity())).collect(summingUp()));
        salesOrderBill.setSumOriginalAmount(goodsDetailData.stream().map(e->e.getOriginalPrice().multiply(e.getQuantity())).collect(summingUp()));
        salesOrderBill.setSumTagAmount(goodsDetailData.stream().map(e->e.getTagPrice().multiply(e.getQuantity())).collect(summingUp()));
        salesOrderBill.setSumSalesAmount(goodsDetailData.stream().map(e -> e.getSalesPrice().multiply(e.getQuantity())).collect(summingUp()));
        return errorMsgList;
    }

    private static Collector<BigDecimal, ?, BigDecimal> summingUp() {
        return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
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
        billGoods.setPoint(goodsResult.getPoint());
        billGoods.setCreatedBy(salesOrderBill.getCreatedBy());
        billGoods.setCreatedTime(salesOrderBill.getCreatedTime());
        billGoods.setUpdatedBy(salesOrderBill.getUpdatedBy());
        billGoods.setUpdatedTime(salesOrderBill.getUpdatedTime());
        billGoods.setSalesPrice(goodsResult.getSalesPrice());
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
     *
     * @param context
     */
    private void save(SalesOrderBillSaveContext context) {
        salesOrderBillDao.insert(context.getSalesOrderBill());
        if (CollUtil.isNotEmpty(context.getSalesOrderBillGoodsList())) {
            context.getSalesOrderBillGoodsList().forEach(salesOrderBillGoodsDao::insert);
        }
        if (CollUtil.isNotEmpty(context.getSalesOrderBillSizeList())) {
            context.getSalesOrderBillSizeList().forEach(salesOrderBillSizeDao::insert);
        }
        if (CollUtil.isNotEmpty(context.getSalesOrderBillPaymentList())) {
            context.getSalesOrderBillPaymentList().forEach(salesOrderBillPaymentDao::insert);
        }
        if (CollUtil.isNotEmpty(context.getEmployeeBillAchievements())) {
            context.getEmployeeBillAchievements().forEach(employeeBillAchievementDao::insert);
        }
        if (CollUtil.isNotEmpty(context.getEmployeeGoodsAchievements())) {
            context.getEmployeeGoodsAchievements().forEach(employeeGoodsAchievementDao::insert);
        }
    }

}
