package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.member.MemberIntegral;
import com.regent.rbp.api.core.member.MemberPolicy;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.member.MemberIntegralDao;
import com.regent.rbp.api.dao.member.MemberPolicyDao;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.MemberDto;
import com.regent.rbp.task.inno.model.req.MemberReqDto;
import com.regent.rbp.task.inno.model.resp.ChannelRespDto;
import com.regent.rbp.task.inno.model.resp.MemberRespDto;
import com.regent.rbp.task.inno.service.MemberService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Impl
 * @author: HaiFeng
 * @create: 2021-09-24 11:04
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final String API_URL_USERS = "api/Users/Post_ErpUsers";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    MemberCardDao memberCardDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    SexDao sexDao;
    @Autowired
    MemberPolicyDao memberPolicyDao;
    @Autowired
    MemberIntegralDao memberIntegralDao;

    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    OnlinePlatformService onlinePlatformService;


    @Override
    public void uploadingMember(String onlinePlatformCode) {
        String key = SystemConstants.POST_ERP_USERS;
        Long onlinePlatformId = onlinePlatformService.getOnlinePlatformById(onlinePlatformCode);

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

        QueryWrapper<MemberCard> queryWrapper = new QueryWrapper<MemberCard>();
        if (uploadingDate != null) {
            queryWrapper.le("updated_time", uploadingDate);
        }

        List<MemberCard> memberCardList = memberCardDao.selectList(queryWrapper);
        if (memberCardList != null && memberCardList.size() > 0) {
            // 发卡渠道
            List<Long> channelIds = memberCardList.stream().map(MemberCard::getChannelId).distinct().collect(Collectors.toList());
            List<Channel> channelList = channelDao.selectBatchIds(channelIds);
            Map<Long, String> channelMap = channelList.stream().collect(Collectors.toMap(Channel::getId, Channel::getCode));
            // 性别
            List<Long> sexIds = memberCardList.stream().map(MemberCard::getSexId).distinct().collect(Collectors.toList());
            List<Sex> sexList = sexDao.selectBatchIds(sexIds);
            Map<Long, String> sexMap = sexList.stream().collect(Collectors.toMap(Sex::getId, Sex::getName));
            // 会员政策
            List<Long> memberPolicyIds = memberCardList.stream().map(MemberCard::getMemberPolicyId).distinct().collect(Collectors.toList());
            List<MemberPolicy> memberPolicyList = memberPolicyDao.selectBatchIds(memberPolicyIds);
            Map<Long, String> memberPolicyMap = memberPolicyList.stream().collect(Collectors.toMap(MemberPolicy::getId, MemberPolicy::getGradeCode));

            Map<Integer, List<MemberDto>> listMap = new HashMap<>();
            Integer sumQty = memberCardList.size();
            double pageNo = Math.ceil(Double.valueOf(sumQty) / new Double(100));
            for(int i = 0; i < pageNo; i++) {
                Integer rowIndex = 1;
                List<MemberDto> dtoList = new ArrayList<>();
                List<MemberCard> cardList = new ArrayList<>();
                if (sumQty < 100 || i == pageNo-1) {
                    cardList = memberCardList.subList(i * 100, sumQty);
                } else {
                    cardList = memberCardList.subList(i * 100, ((i + 1) * 100));
                }
                for (MemberCard card : cardList) {
                    MemberDto dto = new MemberDto();
                    dto.setRowIndex(rowIndex);
                    dto.setUserName(card.getName());
                    dto.setCardNo(card.getCode());
                    dto.setNickName(card.getName());
                    dto.setEmail(card.getEmail());
                    dto.setSex(sexMap.get(card.getSexId()));
                    dto.setBirthday(DateUtil.getFullDateStr(card.getBirthdayDate()));
                    dto.setMobileNo(card.getPhone());
                    dto.setCreateDate(DateUtil.getFullDateStr(card.getCreatedTime()));
                    dto.setModifyDate(DateUtil.getFullDateStr(card.getUpdatedTime()));

                    if (card.getChannelId() != null && channelMap.containsKey(card.getChannelId())) {
                        dto.setStoreCode(channelMap.get(card.getChannelId()));
                        dto.setStaffCode(channelMap.get(card.getChannelId()));
                    }
                    if (card.getMemberPolicyId() != null && memberPolicyMap.containsKey(card.getMemberPolicyId())) {
                        dto.setRankCode(memberPolicyMap.get(card.getMemberPolicyId()));
                    }

                    MemberIntegral memberIntegral = memberIntegralDao.selectOne(new QueryWrapper<MemberIntegral>().eq("member_card_id", card.getId()));
                    if (memberIntegral != null) {
                        dto.setHistoryPoint(memberIntegral.getIntegral());
                    } else {
                        dto.setHistoryPoint(0);
                    }
                    dtoList.add(dto);
                    rowIndex++;
                }
                listMap.put(i + 1, dtoList);
            }
            Date uploadingTime = memberCardList.stream().max(Comparator.comparing(MemberCard::getUpdatedTime)).get().getUpdatedTime();
            //请求接口
            for (Map.Entry<Integer, List<MemberDto>> entry : listMap.entrySet()) {

                MemberReqDto memberReqDto = new MemberReqDto();
                memberReqDto.setApp_key(innoConfig.getAppkey());
                memberReqDto.setApp_secrept(innoConfig.getAppsecret());
                memberReqDto.setData(entry.getValue());

                String api_url = String.format("%s%s", innoConfig.getUrl(), API_URL_USERS);
                String result = HttpUtil.post(api_url, JSON.toJSONString(memberReqDto));
                MemberRespDto respDto = JSON.parseObject(result, MemberRespDto.class);
                if (respDto.getCode().equals("-1")) {
                    new Exception(respDto.getMsg());
                }
                XxlJobHelper.log("请求成功：" + JSON.toJSONString(respDto));
            }
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
    }
}
