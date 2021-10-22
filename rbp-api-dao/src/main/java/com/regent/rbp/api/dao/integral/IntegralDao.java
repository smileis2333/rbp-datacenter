package com.regent.rbp.api.dao.integral;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regent.rbp.api.core.goods.SizeDisable;
import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.integral.MemberIntegralChangeRecord;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/20 13:32
 */
public interface IntegralDao extends BaseMapper<SizeDisable>{
    /**
     * 通过会员编码获取当前积分
     * @param memberCardId
     * @return
     */
    BigDecimal get(Long memberCardId);

    /**
     * 插入会员积分
     * @param memberIntegral
     * @return
     */
    int insertMemberIntegral(MemberIntegral memberIntegral);

    /**
     * 修改会员积分
     * @param memberCardId
     * @param integral
     */
    void updateMemberIntegral(@Param("memberCardId") Long memberCardId,@Param("integral") Double integral);

    /**
     * 插入会员积分变动记录
     * @param memberIntegralChangeRecord
     */
    void insertMemberIntegralChangeRecord(MemberIntegralChangeRecord memberIntegralChangeRecord);

    /**
     * 统计会员积分流水数量
     * @param memberCardId
     * @param beginDate
     * @param endDate
     * @return
     */
    int countMemberIntegralChangeRecord(@Param("memberCardId") Long memberCardId,@Param("beginDate") Date beginDate,
                                        @Param("endDate") Date endDate);

    /**
     * 查询会员积分流水记录
     * @param memberCardId
     * @param beginDate
     * @param endDate
     * @param sort
     * @param offset
     * @param end
     * @return
     */
    List<MemberIntegralChangeRecord> queryMemberIntegralChangeRecord(@Param("memberCardId") Long memberCardId,@Param("beginDate") Date beginDate,
                                                         @Param("endDate") Date endDate,@Param("sort") String sort,
                                                         @Param("offset") int offset,@Param("end") int end);
}
