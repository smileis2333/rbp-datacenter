package com.regent.rbp.task.inno.service;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Service
 * @author: HaiFeng
 * @create: 2021-09-24 10:54
 */
public interface MemberService {

    /**
     * 同步会员信息
     * @param onlinePlatformCode
     */
    void uploadingMember(String onlinePlatformCode);
}
