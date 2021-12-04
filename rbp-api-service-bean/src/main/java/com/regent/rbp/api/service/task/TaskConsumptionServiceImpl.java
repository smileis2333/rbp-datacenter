package com.regent.rbp.api.service.task;


import com.regent.rbp.api.dao.task.TaskDao;
import com.xxl.job.core.biz.model.Task;
import com.xxl.job.core.service.TaskConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xuxing
 */
@Service
public class TaskConsumptionServiceImpl implements TaskConsumptionService {

    @Autowired
    TaskDao taskDao;

    @Override
    public List<Task> selectTaskList(long readRecords) {
        return taskDao.selectTaskList(readRecords);
    }

    @Override
    public void deleteById(Long taskId) {
        taskDao.deleteById(taskId);
    }

    @Override
    public void update(Task task) {
        taskDao.update(task);
    }

    @Override
    public int lock(Task task) {
        return taskDao.lock(task);
    }

    @Override
    public void unlockTimeoutTasks(long l) {
        taskDao.unlockTimeoutTasks(l);
    }
}
