package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.receipt.Receipt;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.receipt.ReceiptDao;
import com.regent.rbp.api.dto.base.CustomizeColumnDto;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryParam;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryResult;
import com.regent.rbp.api.dto.receipt.ReceiptBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.receipt.ReceiptBillService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
@Service
public class ReceiptBillServiceBean implements ReceiptBillService {
    @Autowired
    private ReceiptDao receiptDao;
    @Autowired
    private BaseDbDao baseDbDao;
    @Autowired
    private BaseDbService baseDbService;
    @Autowired
    private SystemCommonService systemCommonService;

    @Transactional
    @Override
    public DataResponse save(ReceiptBillSaveParam param) {
        Receipt receipt = BeanUtil.copyProperties(param, Receipt.class);
        if (StringUtil.isNotEmpty(param.getChannelCode())) {
            receipt.setChannelId(baseDbDao.getLongDataBySql(String.format("select id from rbp_channel where status = 1 and code = '%s'", param.getChannelCode())));
        }
        receipt.setFundAccountId(baseDbDao.getLongDataBySql(String.format("select id from rbp_fund_account rfa where code = '%s' and status  = 1",param.getFundAccountCode())));
        receipt.setFundAccountBankId(baseDbDao.getLongDataBySql(String.format("select id from rbp_fund_account_bank rfab where fund_account_id  = '%s' and account  = '%s'",receipt.getFundAccountId(),param.getFundAccountBank())));
        receipt.setReceiptAccount(param.getReceiptAccount());
        receipt.setReceiptTypeId(baseDbDao.getLongDataBySql(String.format("select id from rbp_receipt_type rrt where name = '%s' ",param.getReceiptType())));
        if (StringUtil.isNotEmpty(param.getCurrencyType())) {
            receipt.setCurrencyTypeId(baseDbDao.getLongDataBySql(String.format("select * from rbp_currency_type rct where name = '%s'", param.getCurrencyType())));
        }
        receipt.setBillNo(systemCommonService.getBillNo(receipt.getModuleId()));

        receiptDao.insert(receipt);
        baseDbService.saveOrUpdateCustomFieldData(receipt.getModuleId(), TableConstants.RECEIPT_BILL, receipt.getId(), param.getCustomizeData());
        return ModelDataResponse.Success(receipt.getBillNo());
    }

    @Override
    public PageDataResponse<ReceiptBillQueryResult> query(ReceiptBillQueryParam param) {
        Page<ReceiptBillQueryResult> page = new Page<>(param.getPageNo(), param.getPageSize());
        IPage<ReceiptBillQueryResult> raw = receiptDao.searchPageData(page, param);
        PageDataResponse<ReceiptBillQueryResult> result = new PageDataResponse<>();
        result.setData(convertQueryResult(raw.getRecords()));
        result.setTotalCount(raw.getTotal());
        return result;
    }

    private List<ReceiptBillQueryResult> convertQueryResult(List<ReceiptBillQueryResult> records) {
        if (CollUtil.isEmpty(records)){
            return Collections.emptyList();
        }
        Map<String, List<CustomizeColumnDto>> moduleCustomizeMap = baseDbService.getModuleCustomizeColumnListMap(CollUtil.map(records, ReceiptBillQueryResult::getModuleId, true));
        List<Long> billIds = CollUtil.map(records, ReceiptBillQueryResult::getBillId, true);
        Map<Long, List<CustomizeDataDto>> billCustomMap = baseDbService.getCustomizeColumnMap(TableConstants.RECEIPT_BILL, billIds);
        for (ReceiptBillQueryResult record : records) {
            // 模块自定义字段定义
            List<CustomizeColumnDto> moduleColumnDtoList = moduleCustomizeMap.get(record.getModuleId());
            // 过滤未启用的自定义字段，格式化单选类型字段
            record.setCustomizeData(baseDbService.getAfterFillCustomizeDataList(moduleColumnDtoList, billCustomMap.get(record.getBillId())));
        }
        return records;
    }
}
