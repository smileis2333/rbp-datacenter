package com.regent.rbp.api.service.retail.context;

import com.regent.rbp.api.core.retail.RetailReturnNoticeBill;
import com.regent.rbp.api.core.retail.RetailReturnNoticeBillGoods;
import com.regent.rbp.api.dto.retail.RetailReturnNoticeBillSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 全渠道退货通知 保存上下文对象
 * @author: HaiFeng
 * @create: 2021-09-27 15:53
 */
@Data
public class RetailReturnNoticeBillSaveContext {

    private RetailReturnNoticeBill bill;

    private List<RetailReturnNoticeBillGoods> billGoodsList;

    public RetailReturnNoticeBillSaveContext(RetailReturnNoticeBillSaveParam param) {
        this.bill = new RetailReturnNoticeBill();
        Long userId = ThreadLocalGroup.getUserId();
        this.bill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.bill.setCreatedBy(userId);
        this.bill.setUpdatedBy(userId);

        if(param != null) {
            this.readProperties(param);
        }
    }

    public void readProperties(RetailReturnNoticeBillSaveParam param) {
        this.bill.setManualId(param.getManualId());
        this.bill.setBillNo(param.getBillNo());
        this.bill.setBillDate(param.getBillDate());
        this.bill.setLogisticsBillCode(param.getLogisticsBillCode());
        this.bill.setStatus(param.getStatus());
        this.bill.setNotes(param.getNotes());
    }
}
