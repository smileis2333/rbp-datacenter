package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.task.inno.model.param.ChannelUploadingParam;
import com.regent.rbp.task.inno.model.param.MemberUploadingParam;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.service.MemberService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: rbp-datacenter
 * @description: 会员档案
 * @author: HaiFeng
 * @create: 2021-09-24 10:52
 */
@Slf4j
@Component
public class MemberJob {

    @Autowired
    MemberService memberService;

    /**
     * 上传会员
     * 请求Json：{ "onlinePlatformCode": "RBP" }
     */
    @XxlJob(SystemConstants.POST_ERP_USERS)
    public void uploadingMember() {
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            MemberUploadingParam memberUploadingParam = JSON.parseObject(param, MemberUploadingParam.class);

            //开始推送会员
            memberService.uploadingMember(memberUploadingParam.getOnlinePlatformCode());
        }catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

}
