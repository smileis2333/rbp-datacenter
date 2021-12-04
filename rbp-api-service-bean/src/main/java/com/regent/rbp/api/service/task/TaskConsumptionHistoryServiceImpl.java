package com.regent.rbp.api.service.task;

import com.regent.rbp.api.dao.task.TaskHistoryDao;
import com.xxl.job.core.biz.model.TaskHistory;
import com.xxl.job.core.service.TaskConsumptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuxing
 */
@Service
public class TaskConsumptionHistoryServiceImpl implements TaskConsumptionHistoryService {

    @Autowired
    private TaskHistoryDao taskHistoryDao;

    @Override
    public void insert(TaskHistory taskHistory) {
        taskHistoryDao.insert(taskHistory);
    }
}