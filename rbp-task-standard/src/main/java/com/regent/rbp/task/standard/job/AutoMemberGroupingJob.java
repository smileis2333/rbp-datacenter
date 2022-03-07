package com.regent.rbp.task.standard.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.AppendSqlUtil;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @description: 会员自动分群
 * @author: chenchungui
 * @create: 2022-03-04
 */
@Slf4j
@Component
public class AutoMemberGroupingJob {

    @Autowired
    private BaseDbDao baseDbDao;

    @Resource(name = "applicationTaskExecutor")
    private Executor executor;

    /**
     * 会员自动分群,根据启用的会员分群进行分群
     * 入参格式：{ "memberGroupingIds":"1,2,3" }
     * memberGroupingIds:指定会员群ID
     */
    @XxlJob(SystemConstants.AUTO_MEMBER_GROUPING)
    public void autoMemberGroupingDay() {
        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            Map<String, String> map = Optional.ofNullable(JSON.parseObject(param, Map.class)).orElse(new HashMap<>());
            // 指定的会员分群组ID
            List<Long> ids = Optional.ofNullable(map.get("memberGroupingIds")).map(str -> {
                if (StringUtil.isNotEmpty(str)) {
                    String[] list = str.split(StrUtil.COMMA);
                    return Arrays.stream(list).map(v -> Long.parseLong(v)).distinct().collect(Collectors.toList());
                }
                return new ArrayList<Long>();
            }).orElse(new ArrayList<>());
            // 过滤已启用任务
            StringBuilder sql = new StringBuilder();
            sql.append("select id from rbp_member_grouping where status=100 ");
            if (CollUtil.isNotEmpty(ids)) {
                sql.append(" and id in").append(AppendSqlUtil.getInSqlByLong(ids));
            } else {
                Date now = DateUtil.getNowDateShort();
                // 每天
                sql.append(" and update_frequency in(1");
                // 每周一
                if (1 == DateUtil.getWeek(now)) {
                    sql.append(",2");
                }
                // 每月一号
                if (1 == DateUtil.getDay(now)) {
                    sql.append(",3");
                }
                sql.append(" )");
            }
            XxlJobHelper.log(String.format("查询满足条件会员分群任务SQL：%s", sql));
            // 会员分群编码
            ids = baseDbDao.getLongListDataBySql(sql.toString());
            if (CollUtil.isEmpty(ids)) {
                XxlJobHelper.log("无会员分群任务");
                return;
            }
            // 使用多线程
            for (Long id : ids) {
                executor.execute(() -> {
                    LocalDateTime begin = LocalDateTime.now();
                    XxlJobHelper.log(String.format("开始执行会员分组任务ID：%s", id));
                    // 会员分群
                    this.executeMemberGrouping(id);
                    XxlJobHelper.log(String.format("结束会员分组任务ID：%s, 耗时 %s 秒", id, Duration.between(begin, LocalDateTime.now()).getSeconds()));
                });
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    /**
     * 单次执行会员分群计划
     *
     * @param id
     */
    private void executeMemberGrouping(Long id) {
        // 获取关系表
        String tableName = this.getMemberGroupingTableName(id);
        try {
            // 清空会员群
            baseDbDao.deleteSql(String.format("TRUNCATE %s", tableName));
            // 根据会员标签范围插入
            StringBuilder sql = new StringBuilder();

            sql.append("insert into ").append(tableName);
            sql.append(" (member_grouping_id,member_id) ");
            sql.append(String.format(" select %s as member_grouping_id,a.content_id as member_id from rbp_data_label_member_content a ", id));
            sql.append(" inner join rbp_member_grouping_data_label b on b.member_grouping_id=").append(id);
            sql.append(" and b.data_label_id=a.data_label_id and b.data_label_value_id=a.data_label_value_id group by a.content_id");

            XxlJobHelper.log(String.format("批量插入会员群关系SQL：%s", sql));
            baseDbDao.insertSql(sql.toString());
            // 更新会员分群信息
            Integer num = baseDbDao.getIntegerDataBySql(String.format("select count(1) from %s", tableName));
            XxlJobHelper.log(String.format("会员群编码[%s]批量插入会员群数：%s", id, num));
            sql.setLength(0);
            sql.append(String.format("update rbp_member_grouping set last_refresh_time=now(),task_number=(task_number+1),people_number=%s where id=%s", num, id));
            baseDbDao.updateSql(sql.toString());
        } catch (Exception e) {
            XxlJobHelper.handleFail(String.format("会员分群关系表：%s", e.getMessage()));
        } finally {
            XxlJobHelper.log(String.format("会员分群关系表：%s, 当前线程ID：%s,name：%s", tableName, Thread.currentThread().getId(), Thread.currentThread().getName()));
        }
    }


    /**
     * 获取会员分群会员关联表
     *
     * @param id
     * @return
     */
    private String getMemberGroupingTableName(Long id) {
        String tableName = "rbp_member_grouping_member_".concat(id.toString());

        StringBuilder createTableSql = new StringBuilder();
        createTableSql.append("CREATE TABLE IF NOT EXISTS ").append(tableName);
        createTableSql.append("(   `id` BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '编码',");
        createTableSql.append("    `member_grouping_id` BIGINT(20) NOT NULL   COMMENT '会员分群编码',");
        createTableSql.append("    `member_id` BIGINT(20) NOT NULL   COMMENT '会员编码',");
        createTableSql.append("    `updated_time` DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',");
        createTableSql.append("     PRIMARY KEY (`id`)");
        createTableSql.append(") COMMENT = '会员分群的会员列表';");
        // 初始化表
        baseDbDao.insertSql(createTableSql.toString());

        return tableName;
    }


}
