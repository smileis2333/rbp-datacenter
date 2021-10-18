package com.regent.rbp.task.inno.service.impl;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dto.goods.GoodsQueryParam;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.api.service.goods.context.GoodsQueryContext;
import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.api.service.member.context.MemberCardSaveContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.service.CustomerVipService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author xuxing
 */
@Service
public class CustomerVipServiceImpl implements CustomerVipService {

    static String MEMBER_STATUS_NAME = "正常";

    @Autowired
    MemberCardDao memberCardDao;

    @Autowired
    private MemberCardService memberCardService;

    @Override
    public void create(CustomerVipDto param) throws Exception {
        XxlJobHelper.log("创建会员的入参:" + JSON.toJSONString(param));
        MemberCardSaveParam memberCardSaveParam = new MemberCardSaveParam();

        String message = verificationProperty(param, memberCardSaveParam);
        if(StringUtil.isNotEmpty(message)) {
            throw new Exception(message);
        }
        memberCardSaveParam.setCode(param.getVIP());
        memberCardSaveParam.setName(param.getName());
        memberCardSaveParam.setPhone(param.getMobileTel());
        //来源类别 origin_type 1.线上2.线下3.后台
        memberCardSaveParam.setOriginType("1");
        //来源origin 1.pos,2.英朗,3.微盟,4.有赞5.yike
        memberCardSaveParam.setOrigin("2");
        memberCardSaveParam.setBeginDate(param.getBegainDate());
        memberCardSaveParam.setEndDate(param.getExpireDate());
        memberCardSaveParam.setBirthday(param.getBirthDate());
        memberCardSaveParam.setSexName(param.getSex());
        memberCardSaveParam.setNotes(param.getRemark());
        memberCardSaveParam.setChannelCode(param.getCustomer_ID());
        memberCardSaveParam.setRepairChannelCode(param.getOriginalCustomer());
        memberCardSaveParam.setMemberStatus(MEMBER_STATUS_NAME);

        memberCardService.save(memberCardSaveParam);
    }

    /**
     * 验证数据有效性
     * @param param
     */
    private String verificationProperty(CustomerVipDto param, MemberCardSaveParam memberCardSaveParam) {
        if(param == null) {
            return "参数不能为空";
        }
        if(StringUtil.isEmpty(param.getVIP())) {
            return "VIP卡号不能为空";
        }
        if(memberCardService.checkExistMemberCard(param.getVIP())) {
            return "VIP已存在" + param.getVIP();
        }
        if(StringUtil.isNotEmpty(param.getMobileTel())) {
            if(memberCardService.checkExistMobile(param.getMobileTel())) {
                return "手机号已存在" + param.getVIP();
            }
        }

        return "";
    }
}
