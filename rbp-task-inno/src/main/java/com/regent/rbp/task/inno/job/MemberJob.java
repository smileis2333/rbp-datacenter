package com.regent.rbp.task.inno.job;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import com.regent.rbp.task.inno.controller.CustomerVipController;
import com.regent.rbp.task.inno.controller.StockController;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.model.dto.StockDto;
import com.regent.rbp.task.inno.model.param.DownloadMemberParam;
import com.regent.rbp.task.inno.model.param.MemberUploadingParam;
import com.regent.rbp.task.inno.model.resp.StockRespDto;
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
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
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

    /**
     * 下载会员
     *  请求Json：{ "onlinePlatformCode": "INNO" }
     *  请求Json：{ "onlinePlatformCode": "INNO", "cardnumList": [ "13545395618", "13599331689" ] }
     *  请求Json：{ "onlinePlatformCode": "INNO", "cardnumList": [ "13545395618", "13599331689" ] }
     *  onlinePlatformCode:平台编号
     *  mobileList：手机号
     *  cardnumList：会员卡
     */
    @XxlJob(SystemConstants.GET_USER_LIST)
    public void downloadMember() {
        ThreadLocalGroup.setUserId(SystemConstants.ADMIN_CODE);
        try {
            //读取参数(电商平台编号)
            String param = XxlJobHelper.getJobParam();
            XxlJobHelper.log(param);
            DownloadMemberParam downloadMemberParam = JSON.parseObject(param, DownloadMemberParam.class);

            //开始下载会员
            memberService.saveMember(downloadMemberParam);
        } catch (Exception ex) {
            String message = ex.getMessage();
            XxlJobHelper.log(message);
            XxlJobHelper.handleFail(message);
            return;
        }
    }

}
