package com.regent.rbp.api.service.bean.member;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.member.MemberPolicy;
import com.regent.rbp.api.core.member.MemberStatus;
import com.regent.rbp.api.core.member.MemberType;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCard;
import com.regent.rbp.api.core.storedvaluecard.StoredValueCardAssets;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dao.member.*;
import com.regent.rbp.api.dao.storedvaluecard.StoredValueCardAssetsDao;
import com.regent.rbp.api.dao.storedvaluecard.StoredValueCardDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.member.MemberCardQueryParam;
import com.regent.rbp.api.dto.member.MemberCardQueryResult;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.api.service.member.context.MemberCardQueryContext;
import com.regent.rbp.api.service.member.context.MemberCardSaveContext;
import com.regent.rbp.common.dao.UserDao;
import com.regent.rbp.common.model.system.entity.User;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.SnowFlakeUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    EmployeeDao employeeDao;
    @Autowired
    MemberTypeDao memberTypeDao;
    @Autowired
    MemberPolicyDao memberPolicyDao;
    @Autowired
    MemberStatusDao memberStatusDao;
    @Autowired
    MemberIntegralDao memberIntegralDao;
    @Autowired
    StoredValueCardDao storedValueCardDao;
    @Autowired
    StoredValueCardAssetsDao storedValueCardAssetsDao;


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
     *
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
            Employee employee = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getUserCode()));
            if (employee != null) {
                context.setUserCode(employee.getId());
            }
        }
        // 维护人编号
        if (StringUtils.isNotBlank(param.getMaintainerCode())) {
            Employee employee = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getMaintainerCode()));
            if (employee != null) {
                context.setMaintainerCode(employee.getId());
            }
        }
        // 扩展人编号
        if (StringUtils.isNotBlank(param.getDeveloperCode())) {
            Employee employee = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getMaintainerCode()));
            if (employee != null) {
                context.setDeveloperCode(employee.getId());
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
            List<Channel> list = channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getChannelCode()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setChannelCode(ids);
            }
        }


        // 生效日期
        if (StringUtil.isNotBlank(param.getBeginDateStart())) {
            Date date = DateUtil.getDate(param.getBeginDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setBeginDateStart(date);
        }
        if (StringUtil.isNotBlank(param.getBeginDateEnd())) {
            Date date = DateUtil.getDate(param.getBeginDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setBeginDateEnd(date);
        }
        // 失效日期
        if (StringUtil.isNotBlank(param.getEndDateStart())) {
            Date date = DateUtil.getDate(param.getEndDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setEndDateStart(date);
        }
        if (StringUtil.isNotBlank(param.getEndDateEnd())) {
            Date date = DateUtil.getDate(param.getEndDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setEndDateEnd(date);
        }
        // 创建日期
        if (StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date date = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(date);
        }
        if (StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date date = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(date);
        }
        // 审核日期
        if (StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date date = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(date);
        }
        if (StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date date = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(date);
        }
        // 修改日期
        if (StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date date = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(date);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date date = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(date);
        }

    }

    /**
     * 查询数据
     *
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
        channelIds.addAll(list.stream().map(MemberCard::getRepairChannelId).collect(Collectors.toList()));
        List<Channel> channelList = channelDao.selectBatchIds(channelIds.stream().distinct().collect(Collectors.toList()));
        Map<Long, Channel> mapChannel = channelList.stream().collect(Collectors.toMap(Channel::getId, t -> t));
        // 加载 维护人，扩展人,发卡人
        List<Long> employeeIds = list.stream().map(MemberCard::getMaintainerId).collect(Collectors.toList());
        employeeIds.addAll(list.stream().map(MemberCard::getDeveloperId).collect(Collectors.toList()));
        employeeIds.addAll(list.stream().map(MemberCard::getUserId).collect(Collectors.toList()));
        List<Employee> employeeList = employeeDao.selectBatchIds(employeeIds.stream().distinct().collect(Collectors.toList()));
        Map<Long, Employee> mapEmployee = employeeList.stream().collect(Collectors.toMap(Employee::getId, t -> t));
        // 加载 ，创建人，更新人，审核人，失效人，反审核人
        List<Long> userIds = list.stream().map(MemberCard::getCreatedBy).collect(Collectors.toList());
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
            queryResult.setBirthday(memberCard.getBirthdayDate().toString());
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
            if (memberCard.getMaintainerId() != null && mapEmployee.containsKey(memberCard.getMaintainerId())) {
                Employee employee = mapEmployee.get(memberCard.getMaintainerId());
                queryResult.setMaintainerCode(employee.getCode());
            }
            // 拓展人编号
            if (memberCard.getDeveloperId() != null && mapEmployee.containsKey(memberCard.getDeveloperId())) {
                Employee employee = mapEmployee.get(memberCard.getDeveloperId());
                queryResult.setDeveloperCode(employee.getCode());
            }
            // 会员状态
            if (memberCard.getMemberStatusId() != null && mapMemberStatus.containsKey(memberCard.getMemberStatusId())) {
                queryResult.setMemberStatus(mapMemberStatus.get(memberCard.getMemberStatusId()));
            }
            // 发卡人
            if (memberCard.getUserId() != null && mapEmployee.containsKey(memberCard.getUserId())) {
                Employee employee = mapEmployee.get(memberCard.getUserId());
                queryResult.setUserCode(employee.getCode());
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

    @Transactional
    @Override
    public DataResponse save(MemberCardSaveParam param) {
        boolean createFlag = true;
        MemberCardSaveContext context = new MemberCardSaveContext(param);
        //判断是新增还是更新
        MemberCard item = memberCardDao.selectOne(new QueryWrapper<MemberCard>().eq("code", param.getCode()));
        if (item != null) {
            context.getMemberCard().setId(item.getId());
            createFlag = false;
        }
        //验证渠道数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        synchronized (this) {
            // 自动补充不存在的数据字典
            processAutoCompleteDictionary(param, context);
            // 写入会员表
            this.saveMemberCard(createFlag, context.getMemberCard());
            if (createFlag) {
                // 审核会员
                this.checkMemberCard(context.getMemberCard());
            }
            // 新增会员积分
            this.saveMemberIntegral(context.getMemberCard());
            // 新增储值卡
            this.saveStoredValueCard(context.getMemberCard());
        }
        return DataResponse.success();
    }

    /**
     * 检测会员卡是否已存在
     *
     * @param memberCard
     * @return
     */
    @Override
    public boolean checkExistMemberCard(String memberCard) {
        return memberCardDao.selectCount(new QueryWrapper<MemberCard>().eq("code", memberCard)) > 0;
    }

    /**
     * 检测手机号是否已存在
     *
     * @param mobile
     * @return
     */
    @Override
    public boolean checkExistMobile(String mobile) {
        return memberCardDao.selectCount(new QueryWrapper<MemberCard>().eq("phone", mobile)) > 0;
    }

    @Override
    public boolean checkExistStatus(String memberNo) {
        return memberCardDao.selectCount(new LambdaQueryWrapper<MemberCard>().eq(MemberCard::getCode, memberNo).eq(MemberCard::getStatus, 3)) > 0;
    }

    /**
     * 验证属性
     *
     * @param param
     * @param context
     * @return
     */
    private List<String> verificationProperty(MemberCardSaveParam param, MemberCardSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        MemberCard memberCard = context.getMemberCard();

        MemberCard oldMemberCard = memberCardDao.selectOne(new QueryWrapper<MemberCard>().eq("code", param.getCode()));
        if (oldMemberCard != null)
            memberCard.setId(oldMemberCard.getId());

        MemberType memberType = memberTypeDao.selectOne(new QueryWrapper<MemberType>().eq("name", param.getMemberType()));
        memberCard.setMemberTypeId(memberType.getId());

        if (StringUtils.isNotBlank(param.getMemberStatus())) {
            MemberStatus memberStatus = memberStatusDao.selectOne(new QueryWrapper<MemberStatus>().eq("type_value", param.getMemberStatus()));
            if (memberStatus != null) {
                memberCard.setMemberStatusId(memberStatus.getId());
            }
        }

        // 性别
        if (StringUtils.isNotBlank(param.getSexName())) {
            Sex item = sexDao.selectOne(new QueryWrapper<Sex>().eq("name", param.getSexName()));
            if (item != null) {
                memberCard.setSexId(item.getId());
            } else {
                errorMsgList.add("性别(sexName)不存在");
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
            Employee item = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getUserCode()));
            if (item != null) {
                memberCard.setUserId(item.getId());
            } else {
                //errorMsgList.add("发卡人编号(userCode)不存在");
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
            Employee item = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getMaintainerCode()));
            if (item != null) {
                memberCard.setMaintainerId(item.getId());
            } else {
                //errorMsgList.add("维护人编号(maintainerCode)不存在");
            }
        }
        // 拓展人编号
        if (StringUtils.isNotBlank(param.getDeveloperCode())) {
            Employee item = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getDeveloperCode()));
            if (item != null) {
                memberCard.setDeveloperId(item.getId());
            } else {
                //errorMsgList.add("拓展人编号(developerCode)不存在");
            }
        }
        // 会员政策
        if (StringUtils.isNotBlank(param.getMemberPolicyCode())) {
            MemberPolicy policy = memberPolicyDao.selectOne(new QueryWrapper<MemberPolicy>().eq("grade_code", param.getMemberPolicyCode()));
            if (policy != null) {
                memberCard.setMemberPolicyId(policy.getId());
            } else {
                errorMsgList.add("会员政策编号(memberPolicyCode)不存在");
            }
        } else {
            MemberPolicy policy = memberPolicyDao.selectOne(new QueryWrapper<MemberPolicy>().eq("is_default", 1));
            if (policy == null) {
                errorMsgList.add("默认会员政策编号(memberPolicyCode)不存在");
            } else {
                memberCard.setMemberPolicyId(policy.getId());
            }
        }
        return errorMsgList;
    }

    /**
     * 自动补充不存在的数据字典
     *
     * @param param
     */
    private void processAutoCompleteDictionary(MemberCardSaveParam param, MemberCardSaveContext context) {
    }

    /**
     * 写入会员
     *
     * @param createFlag
     * @param memberCard
     */
    private void saveMemberCard(Boolean createFlag, MemberCard memberCard) {
        if (createFlag) {
            memberCard.setStatus(0);
            memberCardDao.insert(memberCard);
        } else {
            memberCardDao.updateById(memberCard);
        }
    }

    /**
     * 审核会员
     *
     * @param memberCard
     */
    private void checkMemberCard(MemberCard memberCard) {
        Long userId = ThreadLocalGroup.getUserId();
        memberCard.setStatus(1);
        memberCard.setCheckBy(userId);
        memberCard.setCheckTime(new Date());
        memberCardDao.updateById(memberCard);
    }

    /**
     * 新增 会员积分基础表
     *
     * @param memberCard
     */
    private void saveMemberIntegral(MemberCard memberCard) {
        MemberIntegral integral = memberIntegralDao.selectOne(new LambdaQueryWrapper<MemberIntegral>().eq(MemberIntegral::getMemberCardId, memberCard.getId()));
        if (integral == null) {
            Long userId = ThreadLocalGroup.getUserId();
            integral = new MemberIntegral();
            integral.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
            integral.setMemberCardId(memberCard.getId());
            integral.setCardNo(memberCard.getCode());
            integral.setIntegral(0);
            integral.setFrozenIntegral(BigDecimal.ZERO);
            integral.setManualId("");
            integral.setNotes("");
            integral.setCreatedBy(userId);
            integral.setCreatedTime(new Date());
            integral.setUpdatedBy(userId);
            integral.setUpdatedTime(new Date());
            memberIntegralDao.insert(integral);
        }
    }

    /**
     * 新建储值卡
     *
     * @param memberCard
     */
    private void saveStoredValueCard(MemberCard memberCard) {
        // 判断会员政策是否充值
        MemberPolicy policy = memberPolicyDao.selectOne(new LambdaQueryWrapper<MemberPolicy>().eq(MemberPolicy::getId, memberCard.getMemberPolicyId()));
        if (policy.getIsRecharge().equals(1)) {
            StoredValueCard storedValueCard = storedValueCardDao.selectOne(new LambdaQueryWrapper<StoredValueCard>().eq(StoredValueCard::getMemberCardId, memberCard.getId()));
            if (storedValueCard == null) {
                storedValueCard = new StoredValueCard();
                storedValueCard.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                storedValueCard.setCardNo(memberCard.getCode());
                storedValueCard.setPassword(memberCard.getPassword());
                storedValueCard.setName(memberCard.getName());
                storedValueCard.setType(2);
                storedValueCard.setStatus(1);
                storedValueCard.setOpenCardDate(new Date());
                storedValueCard.setBeginDate(memberCard.getBeginDate());
                storedValueCard.setEndDate(memberCard.getEndDate());
                storedValueCard.setMemberCardId(memberCard.getId());
                storedValueCard.setNotes("");
                storedValueCard.setCreatedBy(memberCard.getCreatedBy());
                storedValueCard.setCreatedTime(memberCard.getCreatedTime());
                storedValueCard.setUpdatedBy(memberCard.getUpdatedBy());
                storedValueCard.setUpdatedTime(memberCard.getUpdatedTime());
                storedValueCardDao.insert(storedValueCard);

                // 新增储值卡资产
                StoredValueCardAssets assets = new StoredValueCardAssets();
                assets.setId(SnowFlakeUtil.getDefaultSnowFlakeId());
                assets.setStoredValueCardId(storedValueCard.getId());
                assets.setPayAmount(BigDecimal.ZERO);
                assets.setCreditAmount(BigDecimal.ZERO);
                assets.setCumulativeConsumeAmount(BigDecimal.ZERO);
                assets.setCumulativeConsumeDenomination(BigDecimal.ZERO);
                assets.setCreatedBy(memberCard.getCreatedBy());
                assets.setCreatedTime(memberCard.getCreatedTime());
                assets.setUpdatedBy(memberCard.getUpdatedBy());
                assets.setUpdatedTime(memberCard.getUpdatedTime());
                storedValueCardAssetsDao.insert(assets);
            }
        }
    }
}
