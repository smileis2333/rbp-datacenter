package com.regent.rbp.api.service.sale.context;

import com.regent.rbp.api.core.salesOrder.SalesOrderBill;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillGoods;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillPayment;
import com.regent.rbp.api.core.salesOrder.SalesOrderBillSize;
import com.regent.rbp.api.dto.sale.SaleOrderSaveParam;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 销售单保存上下文
 * @author: HaiFeng
 * @create: 2021-11-09 17:36
 */
@Data
public class SalesOrderBillSaveContext {

    private SalesOrderBill salesOrderBill;
    private List<SalesOrderBillGoods> salesOrderBillGoodsList;
    private List<SalesOrderBillSize> salesOrderBillSizeList;
    private List<SalesOrderBillPayment> salesOrderBillPaymentList;

    public SalesOrderBillSaveContext() { this(null); }

    public SalesOrderBillSaveContext(SaleOrderSaveParam param) {
        this.salesOrderBill = new SalesOrderBill();
        Long userId = ThreadLocalGroup.getUserId();
        this.salesOrderBill.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.salesOrderBill.setCreatedBy(userId);
        this.salesOrderBill.setUpdatedBy(userId);
        this.salesOrderBillGoodsList = new ArrayList<>();
        this.salesOrderBillSizeList = new ArrayList<>();
        this.salesOrderBillPaymentList = new ArrayList<>();

        if(param != null) {
            this.readProperties(param);
        }
    }

    public void readProperties(SaleOrderSaveParam param) {
        if (this.salesOrderBill == null) {
            return;
        }
        this.salesOrderBill.setModuleId("700043");
        this.salesOrderBill.setBillNo(param.getBillNo());
        this.salesOrderBill.setManualId(param.getManualId());
        this.salesOrderBill.setNotes(param.getNotes());
        this.salesOrderBill.setBillDate(param.getBillDate());
    }
}
