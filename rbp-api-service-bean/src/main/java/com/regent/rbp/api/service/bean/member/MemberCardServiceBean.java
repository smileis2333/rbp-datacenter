package com.regent.rbp.api.service.bean.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.*;
import com.regent.rbp.api.core.channel.*;
import com.regent.rbp.api.core.fundAccount.FundAccount;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.member.MemberPolicy;
import com.regent.rbp.api.core.member.MemberStatus;
import com.regent.rbp.api.core.member.MemberType;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.base.UserDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.member.MemberPolicyDao;
import com.regent.rbp.api.dao.member.MemberStatusDao;
import com.regent.rbp.api.dao.member.MemberTypeDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.member.MemberCardQueryParam;
import com.regent.rbp.api.dto.member.MemberCardQueryResult;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.api.service.member.context.MemberCardQueryContext;
import com.regent.rbp.api.service.member.context.MemberCardSaveContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: rbp-datacenter
 * @description: 会员档案 Bean
 * @author: HaiFeng
 * @create: 2021-09-14 13:34
 */
@Service
public class MemberCardServiceBean implements MemberCardService {

    @Autowired
    MemberCardDao memberCardDao;
    @Autowired
    SexDao sexDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    UserDao userDao;
    @Autowired
    MemberTypeDao memberTypeDao;
    @Autowired
    MemberPolicyDao memberPolicyDao;
    @Autowired
    MemberStatusDao memberStatusDao;


    @Override
    public PageDataResponse<MemberCardQueryResult> query(MemberCardQueryParam param) {
        MemberCardQueryContext context = new MemberCardQueryContext();
        // 将入参转换成查询的上下文对象
        this.convertMemberCardQueryContext(param, context);
        // 查询数据
        PageDataResponse<MemberCardQueryResult> response = searchPage(context);
        return response;
    }

    /**
     * 将查询参数转换成 查询的上下文
     * @param param
     * @param context
     */
    private void convertMemberCardQueryContext(MemberCardQueryParam param, MemberCardQueryContext context) {
        context.setCode(param.getCode());
        context.setName(param.getName());
        context.setAreaCode(param.getAreaCode());
        context.setPhone(param.getPhone());
        context.setOriginType(param.getOriginType());
        context.setOrigin(param.getOrigin());
        context.setStatus(param.getStatus());
        context.setReferrerCardNo(param.getReferrerCardNo());
        context.setFields(param.getFields());

        // 发卡人编号
        if (StringUtils.isNotBlank(param.getUserCode())) {
            User user = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getUserCode()));
            if (user != null) {
                context.setUserCode(user.getId());
            }
        }
        // 维护人编号
        if (StringUtils.isNotBlank(param.getMaintainerCode())) {
            User user = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getMaintainerCode()));
            if (user != null) {
                context.setMaintainerCode(user.getId());
            }
        }
        // 扩展人编号
        if (StringUtils.isNotBlank(param.getDeveloperCode())) {
            User user = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getDeveloperCode()));
            if (user != null) {
                context.setDeveloperCode(user.getId());
            }
        }

        // 性别
        if (param.getSexCode() != null && param.getSexCode().length > 0) {
            List<Sex> list = sexDao.selectList(new QueryWrapper<Sex>().in("name", param.getSexCode()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setSexCode(ids);
            }
        }
        // 发卡渠道
        if (param.getChannelCode() != null && param.getChannelCode().length > 0) {
            List<Channel> list =channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getChannelCode()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setChannelCode(ids);
            }
        }


        // 生效日期
        if(StringUtil.isNotBlank(param.getBeginDateStart())) {
            Date date = DateUtil.getDate(param.getBeginDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setBeginDateStart(date);
        }
        if(StringUtil.isNotBlank(param.getBeginDateEnd())) {
            Date date = DateUtil.getDate(param.getBeginDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setBeginDateEnd(date);
        }
        // 失效日期
        if(StringUtil.isNotBlank(param.getEndDateStart())) {
            Date date = DateUtil.getDate(param.getEndDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setEndDateStart(date);
        }
        if(StringUtil.isNotBlank(param.getEndDateEnd())) {
            Date date = DateUtil.getDate(param.getEndDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setEndDateEnd(date);
        }
        // 创建日期
        if(StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date date = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(date);
        }
        if(StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date date = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(date);
        }
        // 审核日期
        if(StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date date = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(date);
        }
        if(StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date date = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(date);
        }
        // 修改日期
        if(StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date date = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(date);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date date = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(date);
        }

    }

    /**
     * 查询数据
     * @param context
     * @return
     */
    private PageDataResponse<MemberCardQueryResult> searchPage(MemberCardQueryContext context) {

        PageDataResponse<MemberCardQueryResult> result = new PageDataResponse<MemberCardQueryResult>();

        Page<MemberCard> pageModel = new Page<MemberCard>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = new QueryWrapper<MemberCard>();

        IPage<MemberCard> pageData = memberCardDao.selectPage(pageModel, queryWrapper);
        List<MemberCardQueryResult> list = convertQueryResult(pageData.getRecords());

        result.setTotalCount(pageData.getTotal());
        result.setData(list);

        return result;
    }

    /**
     * 处理查询结果的属性
     * 1.读取相同的属性
     * 2.将内部编码id转换成名称name
     */
    private List<MemberCardQueryResult> convertQueryResult(List<MemberCard> list) {
        List<MemberCardQueryResult> queryResults = new ArrayList<>(list.size());
        List<Long> memberCardIds = list.stream().map(MemberCard::getId).collect(Collectors.toList());

        // 加载所有 会员类型
        List<MemberType> memberTypeList = memberTypeDao.selectList(new QueryWrapper<MemberType>().select("id", "name"));
        Map<Long, MemberType> mapMemberType = memberTypeList.stream().collect(Collectors.toMap(MemberType::getId, t -> t));
        // 加载所有 会员政策
        List<MemberPolicy> memberPolicyList = memberPolicyDao.selectList(new QueryWrapper<MemberPolicy>().select("id", "grade_code", "grade_name"));
        Map<Long, MemberPolicy> mapMemberPolicy = memberPolicyList.stream().collect(Collectors.toMap(MemberPolicy::getId, t -> t));
        // 加载所有 性别
        List<Sex> sexList = sexDao.selectList(new QueryWrapper<Sex>().select("id", "name"));
        Map<Long, String> mapSex = sexList.stream().collect(Collectors.toMap(Sex::getId, Sex::getName));
        // 加载所有 会员状态
        List<MemberStatus> memberStatusList = memberStatusDao.selectList(new QueryWrapper<MemberStatus>().select("id", "name"));
        Map<Long, String> mapMemberStatus = memberStatusList.stream().collect(Collectors.toMap(MemberStatus::getId, MemberStatus::getName));
        // 加载当前所有会员 渠道
        List<Long> channelIds = list.stream().map(MemberCard::getChannelId).collect(Collectors.toList());
        channelIds.addAll(list.stream().map(MemberCard :: getRepairChannelId).collect(Collectors.toList()));
        List<Channel> channelList = channelDao.selectBatchIds(channelIds.stream().distinct().collect(Collectors.toList()));
        Map<Long, Channel> mapChannel = channelList.stream().collect(Collectors.toMap(Channel::getId, t -> t));
        // 加载当前所有会员 用户
        List<Long> userIds = list.stream().map(MemberCard::getMaintainerId).collect(Collectors.toList());
        userIds.addAll(list.stream().map(MemberCard::getDeveloperId).collect(Collectors.toList()));
        userIds.addAll(list.stream().map(MemberCard::getCreatedBy).collect(Collectors.toList()));
        userIds.addAll(list.stream().map(MemberCard::getUpdatedBy).collect(Collectors.toList()));
        userIds.addAll(list.stream().map(MemberCard::getCheckBy).collect(Collectors.toList()));
        userIds.addAll(list.stream().map(MemberCard::getCancelBy).collect(Collectors.toList()));
        userIds.addAll(list.stream().map(MemberCard::getUncheckBy).collect(Collectors.toList()));
        List<User> userList = userDao.selectBatchIds(userIds.stream().distinct().collect(Collectors.toList()));
        Map<Long, User> mapUser = userList.stream().collect(Collectors.toMap(User::getId, t -> t));

        for (MemberCard memberCard : list) {
            MemberCardQueryResult queryResult = new MemberCardQueryResult();
            queryResult.setMemberCardId(memberCard.getId());
            queryResult.setCode(memberCard.getCode());
            queryResult.setPassword(memberCard.getPassword());
            queryResult.setName(memberCard.getName());
            queryResult.setAreaCode(memberCard.getAreaCode());
            queryResult.setPhone(memberCard.getPhone());
            queryResult.setOriginType(memberCard.getOriginType());
            queryResult.setOrigin(memberCard.getOrigin());
            queryResult.setBeginDate(memberCard.getBeginDate());
            queryResult.setEndDate(memberCard.getEndDate());
            queryResult.setReferrerCardNo(memberCard.getReferrerCardNo());
            queryResult.setNation(memberCard.getNation());
            queryResult.setProvince(memberCard.getProvince());
            queryResult.setCity(memberCard.getCity());
            queryResult.setArea(memberCard.getArea());
            queryResult.setAddress(memberCard.getAddress());
            queryResult.setBirthday(memberCard.getBirthday().toString());
            queryResult.setBirthdayYear(memberCard.getBirthdayYear());
            queryResult.setBirthdayMouth(memberCard.getBirthdayMouth());
            queryResult.setBirthdayDay(memberCard.getBirthdayDay());
            queryResult.setStatus(memberCard.getStatus());
            queryResult.setEmail(memberCard.getEmail());
            queryResult.setWeixin(memberCard.getWeixin());
            queryResult.setNotes(memberCard.getNotes());
            queryResult.setCreatedTime(memberCard.getCreatedTime());
            queryResult.setUpdatedTime(memberCard.getUpdatedTime());
            queryResult.setCheckTime(memberCard.getCheckTime());
            queryResult.setCancelTime(memberCard.getCancelTime());
            queryResult.setUncheckTime(memberCard.getUncheckTime());

            // 会员类型
            if (memberCard.getMemberTypeId() != null && mapMemberType.containsKey(memberCard.getMemberTypeId())) {
                MemberType memberType = mapMemberType.get(memberCard.getMemberTypeId());
                queryResult.setMemberTypeId(memberType.getId());
                queryResult.setMemberTypeName(memberType.getName());
            }
            // 会员政策
            if (memberCard.getMemberPolicyId() != null && mapMemberPolicy.containsKey(memberCard.getMemberPolicyId())) {
                MemberPolicy memberPolicy = mapMemberPolicy.get(memberCard.getMemberPolicyId());
                queryResult.setMemberPolicyId(memberPolicy.getId());
                queryResult.setMemberPolicyName(memberPolicy.getGradeName());
            }
            // 性别
            if (memberCard.getSexId() != null && mapSex.containsKey(memberCard.getSexId())) {
                queryResult.setSexId(memberCard.getSexId());
                queryResult.setSexName(mapSex.get(memberCard.getSexId()));
            }
            // 发卡渠道
            if (memberCard.getChannelId() != null && mapChannel.containsKey(memberCard.getChannelId())) {
                Channel channel = mapChannel.get(memberCard.getChannelId());
                queryResult.setChannelCode(channel.getCode());
            }
            // 维护渠道编号
            if (memberCard.getRepairChannelId() != null && mapChannel.containsKey(memberCard.getRepairChannelId())) {
                Channel channel = mapChannel.get(memberCard.getRepairChannelId());
                queryResult.setRepairChannelCode(channel.getCode());
            }
            // 维护人编号
            if (memberCard.getMaintainerId() != null && mapUser.containsKey(memberCard.getMaintainerId())) {
                User user = mapUser.get(memberCard.getMaintainerId());
                queryResult.setMaintainerCode(user.getCode());
            }
            // 拓展人编号
            if (memberCard.getDeveloperId() != null && mapUser.containsKey(memberCard.getDeveloperId())) {
                User user = mapUser.get(memberCard.getDeveloperId());
                queryResult.setDeveloperCode(user.getCode());
            }
            // 会员状态
            if (memberCard.getMemberStatusId() != null && mapMemberStatus.containsKey(memberCard.getMemberStatusId())) {
                queryResult.setMemberStatus(mapMemberStatus.get(memberCard.getMemberStatusId()));
            }
            // 创建人
            if (memberCard.getCreatedBy() != null && mapUser.containsKey(memberCard.getCreatedBy())) {
                User user = mapUser.get(memberCard.getCreatedBy());
                queryResult.setCreatedBy(user.getName());
            }
            // 更新人
            if (memberCard.getUpdatedBy() != null && mapUser.containsKey(memberCard.getUpdatedBy())) {
                User user = mapUser.get(memberCard.getUpdatedBy());
                queryResult.setUpdatedBy(user.getName());
            }
            // 审核人
            if (memberCard.getCheckBy() != null && mapUser.containsKey(memberCard.getCheckBy())) {
                User user = mapUser.get(memberCard.getCheckBy());
                queryResult.setCheckBy(user.getName());
            }
            // 失效人
            if (memberCard.getCancelBy() != null && mapUser.containsKey(memberCard.getCancelBy())) {
                User user = mapUser.get(memberCard.getCancelBy());
                queryResult.setCancelBy(user.getName());
            }
            // 反审核人
            if (memberCard.getUncheckBy() != null && mapUser.containsKey(memberCard.getUncheckBy())) {
                User user = mapUser.get(memberCard.getUncheckBy());
                queryResult.setUncheckBy(user.getName());
            }
        }
        return queryResults;
    }

    @Override
    public DataResponse save(MemberCardSaveParam param) {
        boolean createFlag = true;
        MemberCardSaveContext context = new MemberCardSaveContext(param);
        //判断是新增还是更新
        MemberCard item = memberCardDao.selectOne(new QueryWrapper<MemberCard>().eq("code", param.getCode()));
        if(item != null) {
            context.getMemberCard().setId(item.getId());
            createFlag = false;
        }
        //验证渠道数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            //throw new BusinessException(ErrorC, "");
        }
        // 自动补充不存在的数据字典
        processAutoCompleteDictionary(param, context);
        // 写入会员表
        saveMemberCard(createFlag, context.getMemberCard());
        //
        return null;
    }

    /**
     * 验证属性
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(MemberCardSaveParam param, MemberCardSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        MemberCard memberCard = context.getMemberCard();

        if(StringUtils.isBlank(param.getCode())) {
            errorMsgList.add("卡号(code)不能为空");
        } else {
            MemberCard item = memberCardDao.selectOne(new QueryWrapper<MemberCard>().eq("code", param.getCode()));
            if(item != null) {
                memberCard.setId(item.getId());
            }
        }

        if (StringUtils.isBlank(param.getMemberType())) {
            errorMsgList.add("会员类型(memberType)不能为空");
        } else {
            MemberType item = memberTypeDao.selectOne(new QueryWrapper<MemberType>().eq("name", param.getMemberType()));
            if(item != null) {
                memberCard.setMemberTypeId(item.getId());
            }
        }

        if (StringUtils.isBlank(param.getOriginType())) {
            errorMsgList.add("来源类别(originType)不能为空");
        }

        if (StringUtils.isBlank(param.getOrigin())) {
            errorMsgList.add("来源(origin)不能为空");
        }

        // 性别
        if (StringUtils.isNotBlank(param.getSexName())) {
            Sex item = sexDao.selectOne(new QueryWrapper<Sex>().eq("name", param.getSexName()));
            if (item != null) {
                memberCard.setSexId(item.getId());
            }
        }
        // 发卡渠道
        if (StringUtils.isNotBlank(param.getChannelCode())) {
            Channel item = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getChannelCode()));
            if (item != null) {
                memberCard.setChannelId(item.getId());
            }
        }
        // 发卡人编号
        if (StringUtils.isNotBlank(param.getUserCode())) {
            User item = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getUserCode()));
            if (item != null) {
                memberCard.setUserId(item.getId());
            }
        }
        // 维护渠道编号
        if (StringUtils.isNotBlank(param.getRepairChannelCode())) {
            Channel item = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", param.getRepairChannelCode()));
            if (item != null) {
                memberCard.setRepairChannelId(item.getId());
            }
        }
        // 维护人编号
        if (StringUtils.isNotBlank(param.getMaintainerCode())) {
            User item = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getMaintainerCode()));
            if (item != null) {
                memberCard.setMaintainerId(item.getId());
            }
        }
        // 拓展人编号
        if (StringUtils.isNotBlank(param.getDeveloperCode())) {
            User item = userDao.selectOne(new QueryWrapper<User>().eq("code", param.getDeveloperCode()));
            if (item != null) {
                memberCard.setDeveloperId(item.getId());
            }
        }

        return errorMsgList;
    }

    /**
     * 自动补充不存在的数据字典
     * @param param
     */
    private void processAutoCompleteDictionary(MemberCardSaveParam param, MemberCardSaveContext context) {
    }

    /**
     * 写入会员
     * @param createFlag
     * @param memberCard
     */
    private void saveMemberCard(Boolean createFlag, MemberCard memberCard) {
        if (createFlag) {
            memberCardDao.insert(memberCard);
        } else {
            memberCardDao.updateById(memberCard);
        }
    }
}