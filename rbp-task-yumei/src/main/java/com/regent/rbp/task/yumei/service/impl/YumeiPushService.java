package com.regent.rbp.task.yumei.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.common.dao.DbDao;
import com.xxl.job.core.context.XxlJobHelper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
public class YumeiPushService {
    @Autowired
    private DbDao dbDao;

    /**
     * 是否已经推送
     *
     * @param billNo
     * @return
     */
    public boolean ifPush(String billNo) {
        String check = String.format("select count(*) from yumei_push_log ypl where bill_no  = '%s'", billNo);
        return dbDao.selectCount(check) != 0;
    }

    public List<Map<String, Object>> getWaitPushBill(String tableName, String moduleId) {
        return getWaitPushBill(tableName, moduleId, null);
    }

    /**
     * @param tableName
     * @param moduleId
     * @param hookSql
     * @return
     */
    public List<Map<String, Object>> getWaitPushBill(String tableName, String moduleId, String hookSql) {
        String querySql = String.format("select\n" +
                "\ta.id,\n" +
                "\ta.bill_no,\n" +
                "\ta.check_time\n" +
                "from\n" +
                "\t%s a\n" +
                "left join yumei_push_log ypl on\n" +
                "\typl.bill_id = a.id\n" +
                "where\n" +
                "\tmodule_id = '%s'\n" +
                "\tand a.status = 1\n" +
                "\tand ypl.bill_id is null\n", tableName, moduleId);
        if (StrUtil.isNotBlank(hookSql)) {
            querySql += hookSql;
        }
        querySql += "order by a.check_time asc";
        return dbDao.selectList(querySql);
    }

    /**
     * @param billId
     * @param billNo
     * @param pushMethodRef
     * @param tableName
     */
    public void processPush(Long billId, String billNo, Consumer<String> pushMethodRef, String tableName) {
        Date startTime = new Date();
        try {
            pushMethodRef.accept(billNo);
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
                    "values(%s,'%s','%s','%s','%s')", billId, billNo, tableName, DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss"), DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss"));
            dbDao.insert(insertStatement);
            XxlJobHelper.log(String.format("billId: %s, billNo: %s push yumei success", billId, billNo));
        } catch (FeignException e) {
            String msg = String.format("billId: %s, billNo: %s push yumei fail, please see `rbp_third_party_invoke_log` table", billId, billNo);
            log.error(msg);
            e.printStackTrace();
            XxlJobHelper.log(msg);
            XxlJobHelper.handleFail();
        } catch (Exception e) {
            String msg = String.format("billId: %s, billNo: %s push yumei fail, msg %s", billId, billNo, e.getMessage());
            log.error(msg);
            e.printStackTrace();
            XxlJobHelper.log(msg);
            XxlJobHelper.handleFail();
        }
    }
}
