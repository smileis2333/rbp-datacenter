package com.regent.rbp.api.service.bean.bill;

import cn.hutool.core.bean.BeanUtil;
import com.regent.rbp.api.core.receipt.Receipt;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.receipt.ReceiptDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.receipt.ReceiptBillSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.receipt.ReceiptBillService;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
