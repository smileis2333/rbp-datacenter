package com.regent.rbp.api.dao.task;

import com.xxl.job.core.biz.model.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuxing
 */
@Mapper
public interface TaskDao {
    List<Task> selectTaskList(long readRecords);
    void deleteById(Long taskId);
    void update(Task task);
    int lock(Task task);
    /**
     * 解锁超时任务
     */
    void unlockTimeoutTasks(@Param("maxTimeLocked") long maxTimeLocked);
}
