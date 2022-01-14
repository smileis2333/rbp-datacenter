package com.regent.rbp.api.service.member.context;

import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;

/**
 * @program: rbp-datacenter
 * @description: 会员保存上下文对象
 * @author: HaiFeng
 * @create: 2021-09-15 16:58
 */
@Data
public class MemberCardSaveContext {

    private MemberCard memberCard;

    public MemberCardSaveContext() { this(null); }

    public MemberCardSaveContext(MemberCardSaveParam param) {
        this.memberCard = new MemberCard();
        Long userId = ThreadLocalGroup.getUserId();
        this.memberCard.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
        this.memberCard.setCreatedBy(userId);
        this.memberCard.setUpdatedBy(userId);

        if(param != null) {
            this.readProperties(param);
        }
    }

    /**
     * 利用属性值更新当前货品对象
     * @param param
     */
    public void readProperties(MemberCardSaveParam param) {
        if(this.memberCard == null) {
            return;
        }
        this.memberCard.setCode(param.getCode());
        this.memberCard.setPassword(param.getPassword());
        this.memberCard.setName(param.getName());
        this.memberCard.setAreaCode(param.getAreaCode());
        this.memberCard.setPhone(param.getPhone());
        this.memberCard.setReferrerCardNo(param.getReferrerCardNo());
        this.memberCard.setNation(param.getNation());
        this.memberCard.setProvince(param.getProvince());
        this.memberCard.setCity(param.getCity());
        this.memberCard.setArea(param.getArea());
        this.memberCard.setAddress(param.getAddress());
        this.memberCard.setBirthdayYear(param.getBirthdayYear());
        this.memberCard.setBirthdayMouth(param.getBirthdayMouth());
        this.memberCard.setBirthdayDay(param.getBirthdayDay());
        this.memberCard.setEmail(param.getEmail());
        this.memberCard.setWeixin(param.getWeixin());
        this.memberCard.setNotes(param.getNotes());
        this.memberCard.setUpdatedOrigin(param.getUpdatedOrigin());
        this.memberCard.setUnionId(param.getUnionId());
        this.memberCard.setBeginDate(DateUtil.getDate(param.getBeginDate(), DateUtil.SHORT_DATE_FORMAT));
        this.memberCard.setEndDate(DateUtil.getDate(param.getEndDate(), DateUtil.SHORT_DATE_FORMAT));
        this.memberCard.setBirthdayDate(DateUtil.getDate(param.getBirthday(), DateUtil.SHORT_DATE_FORMAT));
        this.memberCard.setOriginType(param.getOriginType());
        this.memberCard.setOrigin(param.getOrigin());
    }

}
