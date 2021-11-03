package com.regent.rbp.api.service.member;

import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.member.MemberCardQueryParam;
import com.regent.rbp.api.dto.member.MemberCardQueryResult;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Service
 * @author: HaiFeng
 * @create: 2021-09-14 11:44
 */
public interface MemberCardService  {

    /**
     * 查询
     * @param param
     * @return
     */
    PageDataResponse<MemberCardQueryResult> query(MemberCardQueryParam param);

    /**
     * 新增/修改
     * @param param
     * @return
     */
    DataResponse save(MemberCardSaveParam param);

    /**
     * 检测会员卡是否已存在
     * @param memberCard
     * @return
     */
    boolean checkExistMemberCard(String memberCard);

    /**
     * 检测手机号是否已存在
     * @param mobile
     * @return
     */
    boolean checkExistMobile(String mobile);

    /**
     * 检测会员是否是作废状态
     * @param memberNo
     * @return
     */
    boolean checkExistStatus(String memberNo);

}
