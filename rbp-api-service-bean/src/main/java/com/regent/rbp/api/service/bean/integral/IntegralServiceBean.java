package com.regent.rbp.api.service.bean.integral;

import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.integral.MemberIntegralChangeRecord;
import com.regent.rbp.api.dao.integral.IntegralDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dto.constants.ApiResponseCode;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.service.integral.IntegralService;
import com.regent.rbp.infrastructure.util.PageUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 14:06
 */
@Service
public class IntegralServiceBean implements IntegralService {
    @Autowired
    private MemberCardDao memberCardDao;
    @Autowired
    private IntegralDao integralDao;
    @Override
    public ModelDataResponse<BigDecimal> get(String code) {
        ModelDataResponse<BigDecimal> result = new ModelDataResponse<>();
        //通过vip卡号获取会员编码
        Long memberCardId = memberCardDao.getIdByCode(code);
        if (memberCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
            result.setMessage("会员不存在");
            return result;
        }
        BigDecimal integral = integralDao.get(memberCardId);
        if (integral == null) {
            result.setData(new BigDecimal(0));
        } else {
            result.setData(integral);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ModelDataResponse updateMemberIntegral(MemberIntegral memberIntegral) {
        ModelDataResponse result = new ModelDataResponse();
        //判断是否存在会员积分
        //通过vip卡号获取会员编码
        Long memberCardId = memberCardDao.getIdByCode(memberIntegral.getCardNo());
        if (memberCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
            result.setMessage("会员不存在");
            return result;
        }
        BigDecimal changeSurplusIntegral = new BigDecimal(memberIntegral.getIntegral());
        MemberIntegral oldMemberIntegral = memberCardDao.getMemberIntegralById(memberCardId);
        if (oldMemberIntegral == null) {
            memberIntegral.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            memberIntegral.setMemberCardId(memberCardId);
            memberIntegral.setFrozenIntegral(new BigDecimal(0));
            integralDao.insertMemberIntegral(memberIntegral);
        }else{
            changeSurplusIntegral = changeSurplusIntegral.add(new BigDecimal(oldMemberIntegral.getIntegral()));
            integralDao.updateMemberIntegral(memberCardId, memberIntegral.getIntegral().doubleValue());
        }
        // 新增会员积分变动记录
        MemberIntegralChangeRecord memberIntegralChangeRecord = new MemberIntegralChangeRecord();
        memberIntegralChangeRecord.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        memberIntegralChangeRecord.setMemberCardId(memberCardId);
        memberIntegralChangeRecord.setChangeIntegral(new BigDecimal(memberIntegral.getIntegral() + ""));
        memberIntegralChangeRecord.setChangeSurplusIntegral(changeSurplusIntegral);
        memberIntegralChangeRecord.setChangeType(1);
        memberIntegralChangeRecord.setBillNo(memberIntegral.getManualId());
        memberIntegralChangeRecord.setNotes(memberIntegral.getNotes() == null ? "inno增减积分" : memberIntegral.getNotes());
        memberIntegralChangeRecord.setCreatedTime(memberIntegral.getCreatedTime());
        integralDao.insertMemberIntegralChangeRecord(memberIntegralChangeRecord);
        return result;
    }

    @Override
    public PageDataResponse<MemberIntegralChangeRecord> query(String memberCardNo, Date beginDate, Date endDate,
                                                  String sort, int page, int pageSize) {
        PageDataResponse<MemberIntegralChangeRecord> result = new PageDataResponse<>();
        int offset = PageUtil.getOffset(page, pageSize);
        int end = PageUtil.getEnd(page, pageSize);
        Long memberCardId = null;
        if (StringUtils.isNotEmpty(memberCardNo)) {
            memberCardId = memberCardDao.getIdByCode(memberCardNo);
            if (memberCardId == null) {
                result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
                result.setMessage("会员不存在");
                return result;
            }
        }
        int count = integralDao.countMemberIntegralChangeRecord(memberCardId,beginDate,endDate);
        if (count > 0) {
            List<MemberIntegralChangeRecord> integralResultList = integralDao.queryMemberIntegralChangeRecord(memberCardId,beginDate,endDate,sort ,offset, end);
            if (CollectionUtils.isNotEmpty(integralResultList)) {
                integralResultList.forEach(item -> {
                    if (item.getChangeType() == 1) {
                        item.setChangeTypeName("自营核销单");
                    } else if (item.getChangeType() == 2) {
                        item.setChangeTypeName("充值单");
                    } else if (item.getChangeType() == 3) {
                        item.setChangeTypeName("积分调整单");
                    }
                });
            }
            result.setTotalCount(count);
            result.setData(integralResultList);
        }
        return result;
    }
}
