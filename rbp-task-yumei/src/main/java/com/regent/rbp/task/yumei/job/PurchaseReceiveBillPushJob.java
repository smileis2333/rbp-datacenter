
package com.regent.rbp.task.yumei.job;

import cn.hutool.core.date.DateUtil;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.yumei.service.YumeiPurchaseService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author huangjie
 * @date : 2022/05/30
 * @description
 */
@Slf4j
@Component
public class PurchaseReceiveBillPushJob {

    @Autowired
    private YumeiPurchaseService yumeiPurchaseService;
    @Autowired
    private DbDao dbDao;

    /**
     * 推送采购入库单到玉美
     */
    @XxlJob("yumei.pushPurchaseReceiveBill")
    public void orderPushRetryHandler() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);

        // 未推送且货品明细都已设置条形码
        String querySql = "select rrb.id,rrb.bill_no from rbp_receive_bill rrb left join rbp_send_bill rsb on rrb.send_id  = rsb.id where rrb.module_id  = '701014'  and rrb.business_type_id  = 1120000000000031 and rrb.status = 1 " +
                " \tand rsb.manual_id is not null and rsb.manual_id != '' " +
                " and not exists (select * from yumei_push_log ypl where ypl.bill_id = rrb.id and ypl.target_table = 'rbp_receive_bill') " +
                " and not exists (select * from rbp_receive_bill_size rrbs left join rbp_barcode rb  on rrbs.goods_id  = rb.goods_id where rrbs.bill_id  = rrb.id and rb.goods_id is null) " +
                " and (select count(*) from rbp_receive_bill_size rrbs2 where rrbs2.bill_id = rrb.id) >0 " +
                " order by rrb.check_time asc";
        List<Map<String, Object>> params = dbDao.selectList(querySql);
        for (Map<String, Object> param : params) {
            String billNo = (String) param.get("bill_no");
            Long billId = ((Long) param.get("id"));
            Date startTime = new Date();
            try {
                yumeiPurchaseService.createPurchaseReceive(billNo);
                Date endTime = new Date();
                String insertStatement = String.format("" +
                        "insert\n" +
                        "\tinto\n" +
                        "\tyumei_push_log(\n" +
                        "\tbill_id,\n" +
                        "\tbill_no,\n" +
                        "\ttarget_table,\n" +
                        "\tpush_start_time,\n" +
                        "\tpush_end_time)\n" +
                        "values(%s,'%s','%s','%s','%s')", billId, billNo, "rbp_receive_bill", DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
                dbDao.insert(insertStatement);
                XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei success", billId, billNo));
            } catch (FeignException e) {
                String msg = String.format("billId: %s, billNo: %s push yumei fail, please see `rbp_third_party_invoke_log` table", billId, billNo);
                log.error(msg);
                e.printStackTrace();
                XxlJobHelper.log(msg);
            } catch (Exception e) {
                String msg = String.format("billId: %s, billNo: %s push yumei fail, msg %s", billId, billNo, e.getMessage());
                log.error(msg);
                e.printStackTrace();
                XxlJobHelper.log(msg);
            }
        }

    }

}
