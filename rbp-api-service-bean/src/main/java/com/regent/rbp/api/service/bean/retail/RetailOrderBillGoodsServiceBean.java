package com.regent.rbp.api.service.bean.retail;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.retail.RetailOrderBillGoods;
import com.regent.rbp.api.dao.retail.RetailOrderBillGoodsDao;
import com.regent.rbp.api.service.retail.RetailOrderBillGoodsService;
import org.springframework.stereotype.Service;

/**
 * @author liuzhicheng
 * @createTime 2022-04-01
 * @Description
 */
@Service
public class RetailOrderBillGoodsServiceBean extends ServiceImpl<RetailOrderBillGoodsDao, RetailOrderBillGoods> implements RetailOrderBillGoodsService {
}
