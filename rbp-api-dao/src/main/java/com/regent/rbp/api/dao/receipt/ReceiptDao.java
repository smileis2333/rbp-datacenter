package com.regent.rbp.api.dao.receipt;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.receipt.Receipt;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryParam;
import com.regent.rbp.api.dto.receipt.ReceiptBillQueryResult;
import org.apache.ibatis.annotations.Param;

/**
 * @author huangjie
 * @date : 2022/05/06
 * @description
 */
public interface ReceiptDao extends BaseMapper<Receipt> {
    IPage<ReceiptBillQueryResult> searchPageData(@Param("pageModel") Page<ReceiptBillQueryResult> pageModel, @Param("param") ReceiptBillQueryParam param);
}
