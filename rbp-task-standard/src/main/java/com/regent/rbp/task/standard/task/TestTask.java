package com.regent.rbp.task.standard.task;

import com.regent.rbp.api.core.constants.SubscriberKey;
import com.regent.rbp.api.core.constants.TaskBillType;
import com.regent.rbp.api.core.constants.TaskType;
import com.xxl.job.core.biz.model.Task;
import com.xxl.job.core.biz.model.TaskResult;
import com.xxl.job.core.constants.TaskResultCode;
import com.xxl.job.core.handler.annotation.TaskJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 测试发货单新增
 * @author xuxing
 */
@Slf4j
@Component
public class TestTask {
//
//    public void initTask() {
//        log.info("TestTask init");
//    }
//
//    public void destroyTask() {
//        log.info("TestTask destroy");
//    }
//
//    @TaskJob(billType = TaskBillType.SEND_BILL, taskType = TaskType.CREATE, subscriberKey = SubscriberKey.WMS_SHUNFENG,
//            init = "initTask", destroy = "destroyTask" )
//    public TaskResult SendBillCreate(Task task) {
//        TaskResult taskResult = new TaskResult();
//        try {
//            log.info("task execute begin:{}", task);
//            TimeUnit.SECONDS.sleep(10);
//            log.info("task execute end:{}", task);
//        }catch (Exception ex){
//            log.info("task execute error:{}", task);
//        }
//        return taskResult;
//    }
}
