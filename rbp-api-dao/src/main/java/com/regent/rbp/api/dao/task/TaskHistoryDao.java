package com.regent.rbp.api.dao.task;

import com.xxl.job.core.biz.model.TaskHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xuxing
 */
@Mapper
public interface TaskHistoryDao {
    void insert(TaskHistory taskHistory);
}
