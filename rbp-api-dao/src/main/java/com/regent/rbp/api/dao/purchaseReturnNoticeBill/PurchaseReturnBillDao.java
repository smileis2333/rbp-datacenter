package com.regent.rbp.api.dao.purchaseReturnNoticeBill;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.purchaseReturnNoticeBill.PurchaseReturnBill;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryParam;
import com.regent.rbp.api.dto.purchase.PurchaseReturnBillQueryResult;
import org.apache.ibatis.annotations.Param;


public interface PurchaseReturnBillDao extends BaseMapper<PurchaseReturnBill> {
    IPage<PurchaseReturnBillQueryResult> searchPageData(@Param("pageModel") Page<PurchaseReturnBill> pageModel, @Param("param") PurchaseReturnBillQueryParam param);
}
