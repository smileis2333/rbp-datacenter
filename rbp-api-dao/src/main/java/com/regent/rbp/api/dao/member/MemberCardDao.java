package com.regent.rbp.api.dao.member;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.member.MemberCard;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Dao
 * @author: HaiFeng
 * @create: 2021-09-14 14:47
 */
public interface MemberCardDao extends BaseMapper<MemberCard> {
    /**
     * 通过卡号获取会员编码
     * @param code
     * @return
     */
    Long getIdByCode(String code);

    /**
     * 查询会员积分
     * @param memberCardId
     * @return
     */
    MemberIntegral getMemberIntegralById(Long memberCardId);
}
