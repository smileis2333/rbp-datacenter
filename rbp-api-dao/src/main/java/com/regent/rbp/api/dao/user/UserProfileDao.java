package com.regent.rbp.api.dao.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.user.UserProfile;
import com.regent.rbp.api.core.user.UserCashierChannel;
import com.regent.rbp.api.core.user.UserCashierLowerDiscount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: rbp-datacenter
 * @description: 用户档案 Dao
 * @author: chenchungui
 * @create: 2021-12-10
 */
public interface UserProfileDao extends BaseMapper<UserProfile> {

    /**
     * 批量插入收银员渠道关系
     *
     * @param insertList
     * @return
     */
    Integer batchInsertUserCashierChannelList(@Param("insertList") List<UserCashierChannel> insertList);

    /**
     * 批量插收银员分类最低折扣
     *
     * @param insertList
     * @return
     */
    Integer batchInsertUserCashierLowerDiscountList(@Param("insertList") List<UserCashierLowerDiscount> insertList);

}
