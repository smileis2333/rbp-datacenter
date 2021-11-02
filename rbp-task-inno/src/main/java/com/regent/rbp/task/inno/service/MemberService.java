package com.regent.rbp.task.inno.service;

import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.model.param.DownloadMemberParam;

import java.util.Map;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Service
 * @author: HaiFeng
 * @create: 2021-09-24 10:54
 */
public interface MemberService {

    /**
     * 同步会员信息
     * @param onlinePlatform
     */
    void uploadingMember(OnlinePlatform onlinePlatform);

    /**
     * 下载会员信息
     * @param param
     */
    void saveMember(DownloadMemberParam param);

    /**
     * inno 新增/修改 会员
     * @param dto
     * @param createFlag
     * @return
     */
    Map<String, String> save(CustomerVipDto dto, Boolean createFlag);

}
