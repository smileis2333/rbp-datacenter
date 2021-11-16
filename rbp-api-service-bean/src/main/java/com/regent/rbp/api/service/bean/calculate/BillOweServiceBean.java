package com.regent.rbp.api.service.bean.calculate;

import com.regent.rbp.api.dao.calculate.BillOweDao;
import com.regent.rbp.api.dto.calculate.SalePlanBillOweDetail;
import com.regent.rbp.api.service.calculate.BillOweService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-11-15 16:14
 */
public class BillOweServiceBean implements BillOweService {

    @Autowired
    BillOweDao billOweDao;


    @Override
    public List<SalePlanBillOweDetail> calculateSalePlanBillOwe(Long channelId, String channelTable, Long goodsId, String goodsTable, Long salePlanId) {
        String id = SnowFlakeUtil.getDefaultSnowFlakeId().toString();
        String resultTable = String.format("SalePlanBillOwe_%s", id);

        HashMap<String, Object> map = new HashMap<>();
        map.put("v_channel_id", channelId);
        map.put("v_channel_table", channelTable);
        map.put("v_goods_id", goodsId);
        map.put("v_goods_table", goodsTable);
        map.put("v_sale_plan_id", salePlanId);
        map.put("v_result_table", resultTable);
        billOweDao.calculateSalePlanBillOwe(map);

        List<SalePlanBillOweDetail> list = billOweDao.getSalePlanBillOweDetail(resultTable);

        billOweDao.dropTemporaryTable(resultTable);
        return list;
    }
}
