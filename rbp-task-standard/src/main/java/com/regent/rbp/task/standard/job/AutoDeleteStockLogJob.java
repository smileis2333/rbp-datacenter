package com.regent.rbp.task.standard.job;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 自动删除库存操作日志记录
 * @author: chenchungui
 * @create: 2022-07-11
 */
@Slf4j
@Component
public class AutoDeleteStockLogJob {

    @Autowired
    private BaseDbDao baseDbDao;

    /**
     * 自动数据库删除临时表
     * 入参格式：{ "month": 2,"week": 8,"day": 60}
     * 删除逻辑：删除月/周/天之前数据
     */
    @XxlJob(SystemConstants.AUTO_DELETE_STOCK_LOG)
    public void autoDelete() {
        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            Map<String, Integer> map = JSON.parseObject(param, Map.class);
            String dateStr = getDays(map);
            XxlJobHelper.log("开始删除{}之前库存日志", dateStr);
            //  批量删除
            int index = 0;
            int total = 0;
            while (true) {
                int count = 0;
                if (index == 0 && (count += this.executeSql("rbp_stock_detail_operator_log", dateStr)) == 0) {
                    total += count;
                    XxlJobHelper.log("成功删除实际库存日志记录：{} 条", total);
                    index++;
                    total = 0;
                } else if (index == 1 && (count += this.executeSql("rbp_usable_stock_detail_operator_log", dateStr)) == 0) {
                    total += count;
                    XxlJobHelper.log("成功删除可用库存日志记录：{} 条", total);
                    index++;
                    total = 0;
                } else if (index == 2 && (count += this.executeSql("rbp_forway_stock_detail_operator_log", dateStr)) == 0) {
                    total += count;
                    XxlJobHelper.log("成功删除在途库存日志记录：{} 条", total);
                    break;
                }
            }

            XxlJobHelper.log("自动删除库存日志计划结束");
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    private Integer executeSql(String tableName, String dateStr) {
        return baseDbDao.deleteSql(String.format("delete from %s where operate_time <= '%s' order by operate_time asc limit 1000 ", tableName, dateStr));
    }

    /**
     * 获取天数
     *
     * @param map
     * @return
     */
    private static String getDays(Map<String, Integer> map) {
        Calendar now = Calendar.getInstance();
        now.setTime(new Date());
        if (CollUtil.isNotEmpty(map)) {
            if (null != map.get("month") && map.get("month") > 0) {
                now.add(Calendar.MONTH, -map.get("month"));
            } else if (null != map.get("week") && map.get("week") > 0) {
                now.add(Calendar.DAY_OF_WEEK, -map.get("week"));
            } else if (null != map.get("day") && map.get("day") > 0) {
                now.add(Calendar.DATE, -map.get("day"));
            }
        } else {
            // 默认查询两个月前
            now.add(Calendar.MONTH, -2);
        }

        return DateUtil.getDateStr(now.getTime(), DateUtil.FULL_DATE_FORMAT);
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.put("month", 2);
        String date = getDays(map);
        System.out.println(date);
    }


}
