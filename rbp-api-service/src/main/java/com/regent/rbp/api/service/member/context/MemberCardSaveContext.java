package com.regent.rbp.api.service.member.context;

import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.dto.channel.ChannelSaveParam;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

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
        long userId = ThreadLocalGroup.getUserId();
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

        this.memberCard.setBeginDate(DateUtil.getDate(param.getBeginDate(), DateUtil.FULL_DATE_FORMAT));
        this.memberCard.setEndDate(DateUtil.getDate(param.getEndDate(), DateUtil.FULL_DATE_FORMAT));
        this.memberCard.setBirthday(DateUtil.getDate(param.getBirthday(), DateUtil.FULL_DATE_FORMAT));

        if (StringUtils.isNotBlank(param.getOriginType())) {
            Integer originType = null;
            switch (param.getOriginType()) {
                case "线上":
                    originType = 1;
                    break;
                case "线下":
                    originType = 2;
                    break;
                case "后台":
                    originType = 3;
                    break;
            }
            this.memberCard.setOriginType(originType);
        }

        if (StringUtils.isNotBlank(param.getOrigin())) {
            Integer origint = null;
            switch (param.getOrigin()) {
                case "pos":
                    origint = 1;
                    break;
                case "英朗":
                    origint = 2;
                    break;
                case "微盟":
                    origint = 3;
                    break;
                case "有赞":
                    origint = 4;
                    break;
                case "yike":
                    origint = 5;
                    break;
            }
            this.memberCard.setOrigin(origint);
        }

        if (StringUtils.isNotBlank(param.getMemberStatus())) {
            Integer status = null;
            switch (param.getMemberStatus()) {
                case "未审核":
                    status = 0;
                    break;
                case "已审核":
                    status = 1;
                    break;
                case "反审核":
                    status = 2;
                    break;
                case "已作废":
                    status = 3;
                    break;
            }
            this.memberCard.setStatus(status);
        }

    }

}
