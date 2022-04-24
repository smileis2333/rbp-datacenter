package com.regent.rbp.task.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regent.rbp.api.core.task.TaskCare;
import com.regent.rbp.api.core.task.TaskChannel;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.dao.task.TaskCareDao;
import com.regent.rbp.api.dao.task.TaskChannelDao;
import com.regent.rbp.common.dao.DbDao;
import com.regent.rbp.common.service.basic.SystemCommonService;
import com.regent.rbp.infrastructure.request.TimeReq;
import com.regent.rbp.task.standard.module.param.TaskCareParam;
import com.regent.rbp.task.standard.service.TaskCareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author liuzhicheng
 * @createTime 2022-03-07
 * @Description
 */
@Slf4j
@Service
public class TaskCareServiceImpl implements TaskCareService {

    @Autowired
    private TaskCareDao taskCareDao;

    @Autowired
    private SystemCommonService systemCommonService;

    @Autowired
    private DbDao dbDao;

    @Autowired
    private BaseDbDao baseDbDao;
    
    @Autowired
    private TaskChannelDao taskChannelDao;

    @Autowired
    private TransactionDefinition transactionDefinition;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Resource(name = "applicationTaskExecutor")
    private Executor executor;

    @Override
    public String taskCareMemberHandler(TaskCareParam taskCareParam) {
        List<String> errorList = new ArrayList<>();
        List<TaskCare> taskCareList = this.getTaskCareList(taskCareParam);
        List<CompletableFuture> futures = new ArrayList();
        for (TaskCare taskCare : taskCareList) {
            futures.add(CompletableFuture.runAsync(() -> {
                String errorMsg = runTaskCareMember(taskCare);
                if (StrUtil.isNotEmpty(errorMsg)) {
                    errorList.add(errorMsg);
                }
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        return StrUtil.join(";", errorList);
    }

    @Override
    public String taskCareChannelHandler(TaskCareParam taskCareParam) {
        List<String> errorList = new ArrayList<>();
        List<TaskCare> taskCareList = this.getTaskCareList(taskCareParam);
        List<CompletableFuture> futures = new ArrayList();
        for (TaskCare taskCare : taskCareList) {
            futures.add(CompletableFuture.runAsync(() -> {
                String errorMsg = runTaskCareChannel(taskCare);
                if (StrUtil.isNotEmpty(errorMsg)) {
                    errorList.add(errorMsg);
                }
            }, executor));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
        return StrUtil.join(";", errorList);
    }

    /**
     * 执行单个关怀任务-刷新会员
     *
     * @param taskCare
     * @return
     */
    private String runTaskCareMember(TaskCare taskCare) {
        log.info("taskCareCode:{} runTaskCareMember begin", taskCare.getCode());
        String errorMsg = null;
        TimeReq timeReq = this.getCurrentCycleTime(taskCare);
        StringBuilder sb = new StringBuilder();
        // 创建回访会员表
        String visitMemberTableName = "rbp_task_care_visit_member_" + taskCare.getId();
        sb.append(" CREATE TABLE IF NOT EXISTS ").append(visitMemberTableName).append("( \n");
        sb.append(" `id` BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '编码' ,\n");
        sb.append(" `task_id` BIGINT(20) NOT NULL   COMMENT '任务主信息编码' ,\n");
        sb.append(" `begin_date` DATETIME NOT NULL   COMMENT '开始日期' ,\n");
        sb.append(" `end_date` DATETIME NOT NULL   COMMENT '截止日期' ,\n");
        sb.append(" `channel_id` BIGINT(20) NOT NULL   COMMENT '渠道编号' ,\n");
        sb.append(" `member_id` BIGINT(20) NOT NULL   COMMENT '会员编码' ,\n");
        sb.append(" `status` BIGINT(20) COMMENT '状态 (1.未回坊;2.已回访;3.无需回访;4.已过期)' ,\n");
        sb.append(" `created_time` DATETIME  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ,\n");
        sb.append(" `updated_time` DATETIME  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' ,\n");
        sb.append(" PRIMARY KEY (`id`) \n");
        sb.append(" ) COMMENT = '关怀任务的回访会员列表 这个是动态表：\"rbp_task_care_visit_member_\"+16位任务编码，每个任务建一个新的表。'\n");
        dbDao.create(sb.toString());

        // 会员分群表名列表
        sb.setLength(0);
        sb.append(" select distinct table_name from information_schema.tables \n");
        sb.append(" where table_schema = database() and table_name in ( \n");
        sb.append(" select concat('rbp_member_grouping_member_', member_grouping_id) from rbp_task_member_grouping \n");
        sb.append(" where task_id = ").append(taskCare.getId()).append(")");
        List<String> memberGroupTableNameList = baseDbDao.getStringListDataBySql(sb.toString());

        // 创建会员列表中间表
        String tableName = systemCommonService.getGenerateTempTableName("taskcaremember");
        sb.setLength(0);
        sb.append(" CREATE TABLE IF NOT EXISTS ").append(tableName).append("( \n");
        sb.append(" `channel_id` BIGINT(20) ,\n");
        sb.append(" `member_id` BIGINT(20) \n");
        sb.append(" ) COMMENT = '会员列表中间表'\n");
        dbDao.create(sb.toString());
        // 插入
        sb.setLength(0);
        sb.append(" INSERT INTO ").append(tableName).append("(channel_id, member_id) \n");
        sb.append(" select distinct b.channel_id, c.id member_id \n");
        sb.append(" from rbp_user a \n");
        sb.append(" inner join rbp_task_care_channel_list b on a.channel_id = b.channel_id \n");
        sb.append(" inner join rbp_member_card c on a.id = c.maintainer_id \n");
        sb.append(" where ");
        if (CollUtil.isEmpty(memberGroupTableNameList)) {
            sb.append(" 1=2 ");
        } else {
            sb.append(" ( \n");
            for (String name : memberGroupTableNameList) {
                sb.append(" exists (select 1 from ").append(name).append(" mg where mg.member_id = c.id) \n");
                sb.append(" or");
            }
            // 删除最后多余的or
            sb.delete(sb.length() - 3, sb.length());
            sb.append(" )");
        }
        sb.append(" and a.status = 100 and a.business_man_flag = 1 \n");
        sb.append(" and b.status = 0 and b.task_id = ").append(taskCare.getId());
        sb.append(" and b.begin_date = '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("' \n");

        dbDao.insert(sb.toString());

        // 开启事务
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            // 插入会员
            sb.setLength(0);
            sb.append(" INSERT INTO ").append(visitMemberTableName).append("(task_id, begin_date, end_date, channel_id, member_id, status, created_time, updated_time) \n");
            sb.append(" select ").append(taskCare.getId()).append(", '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("', ");
            sb.append(" '").append(DateUtil.formatDateTime(timeReq.getEndTime())).append("',");
            sb.append(" a.channel_id, a.member_id, 1, now(), now() \n");
            sb.append(" from ").append(tableName).append(" a \n");
            sb.append(" where not exists (select 1 from ").append(visitMemberTableName).append(" b \n");
            sb.append(" where a.channel_id = b.channel_id and a.member_id = b.member_id and b.task_id = ").append(taskCare.getId());
            sb.append(" and b.begin_date = '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("')");
            dbDao.insert(sb.toString());

            // 更新会员状态 (1.未回坊;2.已回访;3.无需回访;4.已过期)
            // 已回访状态不做修改
            sb.setLength(0);
            sb.append(" update ").append(visitMemberTableName).append(" a left join ").append(tableName).append(" b on a.channel_id = b.channel_id and a.member_id = b.member_id \n");
            sb.append(" set a.status = (case when b.channel_id is null then 3 ");
            sb.append(" when now() > DATE_ADD(a.begin_date, INTERVAL ").append(taskCare.getReturnVisitDays()).append(" DAY) then 4 ");
            sb.append(" else 1 end), a.updated_time = now() \n");
            sb.append(" where a.status in (1, 3, 4) and a.task_id = ").append(taskCare.getId());
            sb.append(" and a.begin_date = '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("'");
            dbDao.update(sb.toString());

            // 提交
            platformTransactionManager.commit(transactionStatus);
            log.info("taskCareCode:{} runTaskCareMember success", taskCare.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = "关怀任务编号：" + taskCare.getCode() + " 刷新会员范围执行失败：" + e.getMessage();
            log.error(errorMsg);
            // 回滚
            platformTransactionManager.rollback(transactionStatus);
        } finally {
            if (StrUtil.isNotEmpty(tableName)) {
                dbDao.dropTable(tableName);
            }
            log.info("taskCareCode:{} runTaskCareMember end", taskCare.getCode());
        }
        return errorMsg;
    }

    /**
     * 执行单个关怀任务-刷新渠道
     *
     * @param taskCare
     * @return
     */
    private String runTaskCareChannel(TaskCare taskCare) {
        log.info("taskCareCode:{} runTaskCareChannel begin", taskCare.getCode());
        String errorMsg = null;
        // 创建渠道列表中间表
        String tableName = systemCommonService.getGenerateTempTableName("taskcarechannel");
        StringBuilder sb = new StringBuilder();
        sb.setLength(0);
        sb.append(" CREATE TABLE IF NOT EXISTS ").append(tableName).append("( \n");
        sb.append(" `id` BIGINT(20) \n");
        sb.append(" ) COMMENT = '渠道列表中间表'\n");
        dbDao.create(sb.toString());
        sb.setLength(0);
        sb.append(" INSERT INTO ").append(tableName).append("(id) \n");
        sb.append(this.getTaskChannelSql(taskCare.getId()));
        dbDao.create(sb.toString());
        // 开启事务
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        try {
            TimeReq timeReq = this.getCurrentCycleTime(taskCare);
            // 插入渠道
            sb.setLength(0);
            sb.append(" INSERT INTO rbp_task_care_channel_list (task_id, begin_date, end_date, channel_id, status, updated_time) \n");
            sb.append(" select ").append(taskCare.getId()).append(", '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("', ");
            sb.append(" '").append(DateUtil.formatDateTime(timeReq.getEndTime())).append("',");
            sb.append(" a.id, 0, now() \n");
            sb.append(" from ").append(tableName).append(" a \n");
            sb.append(" where not exists (select 1 from rbp_task_care_channel_list b \n");
            sb.append(" where a.id = b.channel_id and b.task_id = ").append(taskCare.getId());
            sb.append(" and b.begin_date = '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("')");
            dbDao.insert(sb.toString());

            // 更新渠道状态(0.生效;1.失效;)
            sb.setLength(0);
            sb.append(" update rbp_task_care_channel_list a left join ").append(tableName).append(" b on a.channel_id = b.id \n");
            sb.append(" set a.status = (case when b.id is null then 1 else 0 end), a.updated_time = now() \n");
            sb.append(" where a.task_id = ").append(taskCare.getId());
            sb.append(" and a.begin_date = '").append(DateUtil.formatDateTime(timeReq.getStartTime())).append("'");
            dbDao.update(sb.toString());

            // 提交
            platformTransactionManager.commit(transactionStatus);
            log.info("taskCareCode:{} runTaskCareChannel success", taskCare.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = "关怀任务编号：" + taskCare.getCode() + " 刷新渠道范围执行失败：" + e.getMessage();
            log.error(errorMsg);
            // 回滚
            platformTransactionManager.rollback(transactionStatus);
        } finally {
            if (StrUtil.isNotEmpty(tableName)) {
                dbDao.dropTable(tableName);
            }
            log.info("taskCareCode:{} runTaskCareChannel end", taskCare.getCode());
        }
        return errorMsg;
    }

    /**
     * 获取当前任务起止日期
     *
     * @param taskCare
     * @return
     */
    private TimeReq getCurrentCycleTime(TaskCare taskCare) {
        TimeReq timeReq = new TimeReq();
        if (taskCare.getExecuteType() == 0) {
            timeReq.setStartTime(taskCare.getBeginTime());
            timeReq.setEndTime(taskCare.getEndTime());
        } else {
            Date today = DateUtil.beginOfDay(new Date());
            // 0.每日;1.每周;2.每月;3.每年;99.自定义;
            switch (taskCare.getExecuteFrequency()) {
                case 0 :
                    timeReq.setStartTime(today);
                    timeReq.setEndTime(DateUtil.endOfDay(today));
                    break;
                case 1 :
                    timeReq.setStartTime(DateUtil.beginOfWeek(today));
                    timeReq.setEndTime(DateUtil.endOfWeek(today));
                    break;
                case 2 :
                    timeReq.setStartTime(DateUtil.beginOfMonth(today));
                    timeReq.setEndTime(DateUtil.endOfMonth(today));
                    break;
                case 3 :
                    timeReq.setStartTime(DateUtil.beginOfYear(today));
                    timeReq.setEndTime(DateUtil.endOfYear(today));
                    break;
                case 99 :
                    // 计算自定义周期起始时间
                    Date startTime = DateUtil.beginOfDay(taskCare.getCreatedTime());
                    Date endTime = DateUtil.endOfDay(DateUtil.offsetDay(startTime, taskCare.getCustomFrequencyDays()));
                    int i = 0;
                    while (i < 9999) {
                        if (DateUtil.compare(startTime, today) <= 0 && DateUtil.compare(endTime, today) >= 0) {
                            break;
                        }
                        startTime = endTime;
                        endTime = DateUtil.endOfDay(DateUtil.offsetDay(startTime, taskCare.getCustomFrequencyDays()));
                        // 防止死循环
                        i++;
                    }

                    timeReq.setStartTime(startTime);
                    timeReq.setEndTime(endTime);
                    break;
                default:
                    break;
            }
        }
        return timeReq;
    }

    /**
     * 获取渠道范围sql
     *
     * @param taskId 关怀任务id
     * @return
     */
    private String getTaskChannelSql(Long taskId) {
        boolean flag = true;
        StringBuilder taskChannelSql = new StringBuilder();
        taskChannelSql.append("select distinct id from (");

        List<TaskChannel> taskChannelList = taskChannelDao.selectList(new LambdaQueryWrapper<TaskChannel>().eq(TaskChannel::getTaskId, taskId));
        //按组ID分组
        Map<String, List<TaskChannel>> groupMap = taskChannelList.stream().collect(Collectors.groupingBy(TaskChannel::getGroupNo, Collectors.toList()));
        for (List<TaskChannel> groupDataList : groupMap.values()) {
            flag = false;
            taskChannelSql.append("select id from rbp_channel where 1=1");
            //按key分组
            Map<String, List<TaskChannel>> keyMap = groupDataList.stream().collect(Collectors.groupingBy(TaskChannel::getConditionKey, Collectors.toList()));
            //遍历此组内key
            for (Map.Entry<String, List<TaskChannel>> entry : keyMap.entrySet()) {
                //key对应value值
                StringBuilder values = new StringBuilder();
                for (TaskChannel taskChannel : entry.getValue()) {
                    values.append("'");
                    values.append(taskChannel.getConditionValue());
                    values.append("',");
                }
                if (values.length() <= 0) {
                    continue;
                }
                values.deleteCharAt(values.length() - 1);

                switch (entry.getKey()) {
                    case "channelBusinessFormat":
                        taskChannelSql.append(" and business_format_id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(")");
                        break;
                    case "channelBalanceType":
                        taskChannelSql.append(" and balance_type_id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(")");
                        break;
                    case "areaTree":
                        taskChannelSql.append(" and (");
                        taskChannelSql.append("nation in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or region in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or province in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or city in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or county in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append("))");
                        break;
                    case "barrioAreaTree":
                        taskChannelSql.append(" and (");
                        taskChannelSql.append("barrio1 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or barrio2 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or barrio3 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or barrio4 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or barrio5 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append("))");
                        break;
                    case "organizationTree":
                        taskChannelSql.append(" and (");
                        taskChannelSql.append("organization1 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or organization2 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or organization3 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or organization4 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(") or organization5 in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append("))");
                        break;
                    case "brand":
                        taskChannelSql.append(" and id in ( select channel_id from rbp_channel_brand where brand_id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append("))");
                        break;
                    case "channelCode":
                        taskChannelSql.append(" and id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(")");
                        break;
                    case "channelBusinessNature":
                        taskChannelSql.append(" and business_nature_id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(")");
                        break;
                    case "fundAccount":
                        taskChannelSql.append(" and fund_account_id in (");
                        taskChannelSql.append(values);
                        taskChannelSql.append(")");
                        break;
                    default:
                        break;
                }
            }
            taskChannelSql.append(" union all "); //不同组不同数据集是or的关系，用union all合并所有结果
        }

        if (flag) {
            // 不存在数据
            taskChannelSql.append(" rbp_channel) where 1=2");
        } else {
            // 删除最后多余的union all
            taskChannelSql.delete(taskChannelSql.length() - 10, taskChannelSql.length() - 1);
            taskChannelSql.append(") t");
        }
        return taskChannelSql.toString();
    }

    /**
     * 查询关怀任务列表
     *
     * @param taskCareParam
     * @return
     */
    private List<TaskCare> getTaskCareList(TaskCareParam taskCareParam) {
        List<TaskCare> taskCareList = taskCareDao.selectList(new LambdaQueryWrapper<TaskCare>()
                // 查询未作废的任务
                .in(TaskCare::getStatus, new int[]{0, 1, 2})
                // 查询指定任务，为空查询全部
                .in(null != taskCareParam && CollUtil.isNotEmpty(taskCareParam.getTaskCodeList()),
                        TaskCare::getCode, null != taskCareParam ? taskCareParam.getTaskCodeList() : new ArrayList<String>()));
        return taskCareList;
    }
}
