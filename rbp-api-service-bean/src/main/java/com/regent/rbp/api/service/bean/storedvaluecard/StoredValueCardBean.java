package com.regent.rbp.api.service.bean.storedvaluecard;

import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardAssets;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardChangeRecord;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardPolicy;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.storedvaluecard.StoredValueCardAssetsDao;
import com.regent.rbp.api.dao.storedvaluecard.StoredValueCardChangeRecordDao;
import com.regent.rbp.api.dao.storedvaluecard.StoredValueCardDao;
import com.regent.rbp.api.dto.constants.ApiResponseCode;
import com.regent.rbp.api.dto.core.ListDataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.service.storedvaluecard.StoredValueCardService;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author czd
 * @Date 2021/10/21 14:19
 */
@Service
public class StoredValueCardBean implements StoredValueCardService {
    @Autowired
    private StoredValueCardDao storedValueCardDao;
    @Autowired
    private MemberCardDao memberCardDao;
    @Autowired
    private StoredValueCardAssetsDao storedValueCardAssetsDao;
    @Autowired
    private StoredValueCardChangeRecordDao storedValueCardChangeRecordDao;

    @Override
    public ModelDataResponse<BigDecimal> get(String memberCardNo) {
        ModelDataResponse<BigDecimal> result = new ModelDataResponse<>();
        //通过vip卡号获取会员编码
        Long memberCardId = memberCardDao.getIdByCode(memberCardNo);
        if (memberCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
            result.setMessage("会员不存在");
            return result;
        }
        Long storedValueCardId = storedValueCardDao.getByMemberCardId(memberCardId);
        if (storedValueCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR);
            result.setMessage("该会员没有储值卡");
            return result;
        }
        StoredValueCardAssets storedValueCardAssets = storedValueCardDao.selectAssetsById(storedValueCardId);
        if (storedValueCardAssets != null) {
            result.setData(storedValueCardAssets.getPayAmount().add(storedValueCardAssets.getCreditAmount()));
        } else {
            result.setData(new BigDecimal(0));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ModelDataResponse<String> updateStoredValueCardAssets(String memberCardNo, String channelId,
                                                                 String employeeId, String billNo,
                                                                 Date createTime, Date postDate,
                                                                 BigDecimal payAmount, BigDecimal creditAmount,
                                                                 BigDecimal integral) {
        ModelDataResponse<String> result = new ModelDataResponse<>();
        //通过vip卡号获取会员编码
        Long memberCardId = memberCardDao.getIdByCode(memberCardNo);
        if (memberCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
            result.setMessage("会员不存在");
            return result;
        }
        Long storedValueCardId = storedValueCardDao.getByMemberCardId(memberCardId);
        if (storedValueCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR);
            result.setMessage("该会员没有储值卡");
            return result;
        }
        StoredValueCardAssets storedValueCardAssets = storedValueCardDao.selectAssetsById(storedValueCardId);
        BigDecimal changeSurplusAmount;
        if (storedValueCardAssets == null) {
            storedValueCardAssets = new StoredValueCardAssets();
            storedValueCardAssets.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            storedValueCardAssets.setStoredValueCardId(storedValueCardId);
            storedValueCardAssets.setPayAmount(payAmount);
            storedValueCardAssets.setCreditAmount(creditAmount);
            storedValueCardAssets.setCreatedTime(createTime);
            storedValueCardAssetsDao.insert(storedValueCardAssets);
            changeSurplusAmount = payAmount.add(creditAmount);
        } else {
            changeSurplusAmount = payAmount.add(creditAmount).add(storedValueCardAssets.getPayAmount()).add(storedValueCardAssets.getCreditAmount());
            storedValueCardAssetsDao.updateAssetsById(storedValueCardAssets.getId(), payAmount, creditAmount);
        }
        StoredValueCardChangeRecord storedValueCardChangeRecord = new StoredValueCardChangeRecord();
        storedValueCardChangeRecord.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        storedValueCardChangeRecord.setBillNo(billNo);
        storedValueCardChangeRecord.setNotes("inno储值增减");
        storedValueCardChangeRecord.setStoredValueCardId(storedValueCardId);
        storedValueCardChangeRecord.setChangePayAmount(payAmount.add(creditAmount));
        storedValueCardChangeRecord.setChangeCreditAmount(creditAmount);
        storedValueCardChangeRecord.setChangeSurplusAmount(changeSurplusAmount);
        storedValueCardChangeRecord.setChangeType(2);
        storedValueCardChangeRecord.setCreatedTime(createTime);
        storedValueCardChangeRecordDao.insert(storedValueCardChangeRecord);
        return result;
    }


    @Override
    public ListDataResponse<StoredValueCardChangeRecord> query(String memberCardNo) {
        ListDataResponse<StoredValueCardChangeRecord> result = new ListDataResponse<>();
        //通过vip卡号获取会员编码
        Long memberCardId = memberCardDao.getIdByCode(memberCardNo);
        if (memberCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_EXISTS_ERROR);
            result.setMessage("会员不存在");
            return result;
        }
        Long storedValueCardId = storedValueCardDao.getByMemberCardId(memberCardId);
        if (storedValueCardId == null) {
            result.setCode(ApiResponseCode.MEMBER_NOT_STORED_CADD_ERROR);
            result.setMessage("该会员没有储值卡");
            return result;
        }
        List<StoredValueCardChangeRecord> list = storedValueCardDao.query(storedValueCardId);
        if (CollectionUtils.isNotEmpty(list)) {
            MemberCard memberCard = memberCardDao.selectById(memberCardId);
            list.forEach(item -> {
                item.setMemberCardNo(memberCard.getCode());
                item.setMemberCardName(memberCard.getName());
                if (item.getChangeType() == 1) {
                    item.setChangeTypeName("自营核销单");
                } else if (item.getChangeType() == 2) {
                    item.setChangeTypeName("金额充值单");
                } else if (item.getChangeType() == 3) {
                    item.setChangeTypeName("储值调整单");
                }
            });
        }
        result.setData(list);
        return result;
    }


}
