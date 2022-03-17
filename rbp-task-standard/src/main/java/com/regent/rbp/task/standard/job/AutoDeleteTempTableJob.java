package com.regent.rbp.task.standard.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @program: rbp-datacenter
 * @description: 自动数据库删除临时表
 * @author: chenchungui
 * @create: 2021-12-17
 */
@Slf4j
@Component
public class AutoDeleteTempTableJob {

    @Autowired
    private BaseDbDao baseDbDao;

    private static final String tableNamePrefix = "temp_xxx_table_";
    private static final String reportTableNamePrefix = "temp_report_xxx_table_";

    /**
     * 自动数据库删除临时表
     * 入参格式：{ "action": "default"}
     * 删除逻辑：default-删除今天之前；all-删除所有；DTable-仅删除临时表；DReport-仅删除报表临时表
     * temp_xxx_table_{模块名称}_{日期+6位流水}，temp_report_xxx_table_{模块名称}_{日期+6位流水}
     */
    @XxlJob(SystemConstants.AUTO_DELETE_TEMP_TABLE)
    public void autoDeleteTempTable() {
        try {
            //读取参数
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            Map<String, String> map = JSON.parseObject(param, Map.class);
            ActionTypeEnum actionTypeEnum = ActionTypeEnum.getEnum(map.get("action"));
            // 类型校验
            List<String> deleteTableList = new ArrayList<>();
            switch (actionTypeEnum) {
                case DEFAULT: {
                    deleteTableList.addAll(this.getTableList(tableNamePrefix, true));
                    deleteTableList.addAll(this.getTableList(reportTableNamePrefix, true));
                    break;
                }
                case ALL: {
                    deleteTableList.addAll(this.getTableList(tableNamePrefix, false));
                    deleteTableList.addAll(this.getTableList(reportTableNamePrefix, false));
                    break;
                }
                case DELETE_TABLE: {
                    deleteTableList.addAll(this.getTableList(tableNamePrefix, true));
                    break;
                }
                case DELETE_REPORT: {
                    deleteTableList.addAll(this.getTableList(reportTableNamePrefix, true));
                    break;
                }
                default:
                    break;
            }
            // 批量删除
            if (CollUtil.isNotEmpty(deleteTableList)) {
                StringBuilder sql = new StringBuilder("drop table if exists ");
                String lastOne = deleteTableList.get(deleteTableList.size() - 1);
                for (String table : deleteTableList) {
                    sql.append(table);
                    if (!table.equals(lastOne)) {
                        sql.append(StrUtil.COMMA);
                    }
                }
                // 删除临时表
                baseDbDao.deleteSql(sql.toString());
                XxlJobHelper.log(String.format("删除临时表 %s 张: Delete table sql [%s]", deleteTableList.size(), sql));
            } else {
                XxlJobHelper.log("删除临时表 0 张");
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }

    private List<String> getTableList(String prefix, Boolean isNowDay) {
        StringBuilder sql = new StringBuilder("select table_name from information_schema.tables where table_schema = database() ");
        if (isNowDay) {
            sql.append(" and create_time <= DATE_FORMAT(NOW(), '%Y-%m-%d') ");
        }
        sql.append(" and table_name like '").append(prefix).append("%'");
        return Optional.ofNullable(baseDbDao.getStringListDataBySql(sql.toString())).orElse(new ArrayList<>());
    }

    /**
     * 操作类型
     */
    private enum ActionTypeEnum {

        DEFAULT("default"),
        ALL("all"),
        DELETE_TABLE("DTable"),
        DELETE_REPORT("DReport"),
        ;
        private String type;

        public String getType() {
            return type;
        }

        ActionTypeEnum(String type) {
            this.type = type;
        }

        public static ActionTypeEnum getEnum(String type) {
            return Arrays.stream(ActionTypeEnum.values()).filter(f -> f.getType().equals(type)).findFirst().orElse(DEFAULT);
        }
    }

    public static void main(String[] args) {
        System.out.println(String.format("======= '%s%' ====", 111));
    }

}
