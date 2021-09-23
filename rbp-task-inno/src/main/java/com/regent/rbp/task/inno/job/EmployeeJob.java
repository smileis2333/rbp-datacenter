package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.task.inno.model.param.EmployeeUploadingParam;
import com.regent.rbp.task.inno.model.resp.EmployeeRespDto;
import com.regent.rbp.task.inno.service.EmployeeService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
@Slf4j
@Component
public class EmployeeJob {
    private static final String ERROR_EMPLOYEE_ONLINEPLATFORMCODE = "[inno推送员工档案信息]:onlinePlatformCode电商平台编号参数值不存在";
    private static final String ERROR_EMPLOYEE_LIST = "[inno推送员工档案信息]:当前无员工档案信息需要同步";
    @Autowired
    EmployeeService employeeService;

    /**
     * 同步员工档案
     * 请求Json：{ "onlinePlatformCode": "RBP" }
     */
    @XxlJob(SystemConstants.POST_ERP_EMPLOYEE)
    public void uploadingEmployee() {
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            EmployeeUploadingParam employeeUploadingParam = JSON.parseObject(param, EmployeeUploadingParam.class);
            OnlinePlatform onlinePlatform = employeeService.getOnlinePlatform(employeeUploadingParam.getOnlinePlatformCode());

            if(onlinePlatform == null) {
                XxlJobHelper.log(ERROR_EMPLOYEE_ONLINEPLATFORMCODE);
                XxlJobHelper.handleFail(ERROR_EMPLOYEE_ONLINEPLATFORMCODE);
                return;
            }
            //开始推送
            if (onlinePlatform.getWarehouseId() != null) {
                EmployeeRespDto resp = employeeService.uploadingEmployee(onlinePlatform.getId(), onlinePlatform.getChannelId());
                if (resp == null) {
                    XxlJobHelper.log(ERROR_EMPLOYEE_LIST);
                }
                else if (resp.getCode().equals("-1")) {
                    new Exception(resp.getMsg());
                }
                XxlJobHelper.log("请求成功：" + JSON.toJSONString(resp));
            } else {
                XxlJobHelper.log(ERROR_EMPLOYEE_LIST);
            }
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

}
