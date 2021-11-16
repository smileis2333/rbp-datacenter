package com.regent.rbp.api.service.bean.calculate;

import com.regent.rbp.api.dao.calculate.BillOweDao;
import com.regent.rbp.api.dto.calculate.NoticeBillOweDetail;
import com.regent.rbp.api.dto.calculate.PurchaseBillOweDetail;
import com.regent.rbp.api.dto.calculate.SalePlanBillOweDetail;
import com.regent.rbp.api.service.calculate.BillOweService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @program: rbp-datacenter
 * @description:
 * @author: HaiFeng
 * @create: 2021-11-15 16:14
 */
@Service
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

    @Override
    public List<NoticeBillOweDetail> calculateNoticeBillOwe(Long channelId, String channelTable, Long goodsId, String goodsTable, Long noticeId) {
        String id = SnowFlakeUtil.getDefaultSnowFlakeId().toString();
        String resultTable = String.format("NoticeBillOwe_%s", id);

        HashMap<String, Object> map = new HashMap<>();
        map.put("v_channel_id", channelId);
        map.put("v_channel_table", channelTable);
        map.put("v_goods_id", goodsId);
        map.put("v_goods_table", goodsTable);
        map.put("v_sale_plan_id", noticeId);
        map.put("v_result_table", resultTable);
        billOweDao.calculateNoticeBillOwe(map);

        List<NoticeBillOweDetail> list = billOweDao.getNoticeBillOweDetail(resultTable);

        billOweDao.dropTemporaryTable(resultTable);
        return list;
    }

    @Override
    public List<PurchaseBillOweDetail> calculatePurchaseBillOwe(Long channelId, String channelTable, Long goodsId, String goodsTable, Long noticeId) {
        String id = SnowFlakeUtil.getDefaultSnowFlakeId().toString();
        String resultTable = String.format("PurchaseBillOwe_%s", id);

        HashMap<String, Object> map = new HashMap<>();
        map.put("v_channel_id", channelId);
        map.put("v_channel_table", channelTable);
        map.put("v_goods_id", goodsId);
        map.put("v_goods_table", goodsTable);
        map.put("v_sale_plan_id", noticeId);
        map.put("v_result_table", resultTable);
        billOweDao.calculatePurchaseBillOwe(map);

        List<PurchaseBillOweDetail> list = billOweDao.getPurchaseBillOweDetail(resultTable);

        billOweDao.dropTemporaryTable(resultTable);
        return list;
    }
}
