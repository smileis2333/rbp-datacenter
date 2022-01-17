package com.regent.rbp.task.standard.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 更新批次号管理
 * @author: chenchungui
 * @create: 2022-01-17
 */
@Slf4j
@Component
public class UpdateBatchManagementJob {

    @Autowired
    private BaseDbDao baseDbDao;

    /**
     * 更新批次号管理启用状态
     * ① 判断已启用的批次号的失效日期是否小于当前日期，是则执行下一步；否则无变动；
     * ② 判断批次号的使用状态是否为“已使用”，是则无变动，否则启用状态自动变为“已禁用”；
     */
    @XxlJob(SystemConstants.UPDATE_BATCH_MANAGEMENT_STATUS)
    public void updateBatchManagementStatus() {
        try {
            List<String> codeList = baseDbDao.getStringListDataBySql("select code from rbp_batch_management where status = 100 and usage_status = 0 and expired_date < now()");
            if (CollUtil.isNotEmpty(codeList)) {
                baseDbDao.updateSql("update rbp_batch_management set status = 101 where status = 100 and usage_status = 0 and expired_date < now() ");
                XxlJobHelper.log(String.format("更新批次号 %s 条, detail [%s]", codeList.size(), StringUtils.join(codeList, StrUtil.COMMA)));
            } else {
                XxlJobHelper.log("更新批次号 0 条");
            }

        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
    }


}
