package com.regent.rbp.api.service.bean.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regent.rbp.api.core.base.Category;
import com.regent.rbp.api.core.base.Year;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.user.UserCashierChannel;
import com.regent.rbp.api.core.user.UserCashierLowerDiscount;
import com.regent.rbp.api.core.user.UserProfile;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.user.UserCashierChannelDao;
import com.regent.rbp.api.dao.user.UserCashierLowerDiscountDao;
import com.regent.rbp.api.dao.user.UserProfileDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.user.UserCashierChannelDto;
import com.regent.rbp.api.dto.user.UserCashierDiscountDto;
import com.regent.rbp.api.dto.user.UserQueryParam;
import com.regent.rbp.api.dto.user.UserQueryResult;
import com.regent.rbp.api.dto.user.UserSaveParam;
import com.regent.rbp.api.service.user.UserService;
import com.regent.rbp.api.service.user.context.UserQueryContext;
import com.regent.rbp.api.service.user.context.UserSaveContext;
import com.regent.rbp.common.model.basic.dto.IdNameCodeDto;
import com.regent.rbp.common.model.basic.dto.IdNameDto;
import com.regent.rbp.common.service.basic.DbService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.enums.LanguageTableEnum;
import com.regent.rbp.infrastructure.enums.StatusEnum;
import com.regent.rbp.infrastructure.util.EncryptUtil;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.OptionalUtil;
import com.regent.rbp.infrastructure.util.StreamUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chenchungui
 * @date 2021/12/13
 * @description 用户档案 实现类
 */
@Service
public class UserServiceBean extends ServiceImpl<UserProfileDao, UserProfile> implements UserService {

    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private UserCashierChannelDao userCashierChannelDao;
    @Autowired
    private UserCashierLowerDiscountDao userCashierLowerDiscountDao;
    @Autowired
    private ChannelDao channelDao;
    @Autowired
    private DbService dbService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageDataResponse<UserQueryResult> query(UserQueryParam param) {
        UserQueryContext context = new UserQueryContext();
        //将入参转换成查询的上下文对象
        convertQueryContext(param, context);
        //查询数据
        PageDataResponse<UserQueryResult> result = new PageDataResponse<>();
        Page<UserProfile> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);
        queryWrapper.orderByDesc("updated_time");
        IPage<UserProfile> salesPageData = userProfileDao.selectPage(pageModel, queryWrapper);
        List<UserQueryResult> list = convertQueryResult(salesPageData.getRecords());

        result.setTotalCount(salesPageData.getTotal());
        result.setData(list);
        return result;
    }

    /**
     * 创建/更新
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public DataResponse save(UserSaveParam param) {
        UserSaveContext context = new UserSaveContext();
        UserProfile userProfile = null;
        // 是否创建
        if (StringUtil.isNotEmpty(param.getCode())) {
            userProfile = userProfileDao.selectOne(new QueryWrapper<UserProfile>().select("id, code").eq("code", param.getCode()).last(" limit 1"));
        }
        // 校验并转换
        List<String> messageList = new ArrayList<>();
        this.convertSaveContext(null == userProfile, Optional.ofNullable(userProfile).orElse(UserProfile.build()), context, param, messageList);
        if (CollUtil.isNotEmpty(messageList)) {
            return new ModelDataResponse(ResponseCode.PARAMS_ERROR, getMessageByParams("paramVerifyError", new String[]{String.join(StrUtil.COMMA, messageList)}));
        }
        UserProfile user = context.getUser();
        List<UserCashierChannel> cashierChannelList = context.getChannelList();
        List<UserCashierLowerDiscount> cashierLowerDiscountList = context.getLowerDiscountList();
        if (null == userProfile) {
            // 新增
            userProfileDao.insert(user);
        } else {
            user.setId(userProfile.getId());
            user.setCreatedBy(userProfile.getCreatedBy());
            user.setCreatedTime(user.getCreatedTime());
            userProfileDao.updateById(user);
            // 删除子表
            userCashierLowerDiscountDao.delete(new QueryWrapper<UserCashierLowerDiscount>().eq("user_id", user.getId()));
            userCashierChannelDao.delete(new QueryWrapper<UserCashierChannel>().eq("user_id", user.getId()));
        }
        if (CollUtil.isNotEmpty(cashierChannelList)) {
            userProfileDao.batchInsertUserCashierChannelList(cashierChannelList);
        }
        if (CollUtil.isNotEmpty(cashierLowerDiscountList)) {
            userProfileDao.batchInsertUserCashierLowerDiscountList(cashierLowerDiscountList);
        }

        return ModelDataResponse.Success(user.getId());
    }


    /**
     * 创建转换器
     *
     * @param createFlag 是否创建
     * @param user
     * @param context
     * @param param
     */
    private void convertSaveContext(Boolean createFlag, UserProfile user, UserSaveContext context, UserSaveParam param, List<String> messageList) {
        /****************   主体    ******************/

        BeanUtil.copyProperties(param, user);
        // 密码加密
//        if (StringUtil.isNotEmpty(user.getPassword())) {
        user.setPassword(EncryptUtil.encryptByAES(user.getPassword()));
//        }
        context.setUser(user);
        // 主体校验
//        if (StringUtil.isEmpty(user.getCode())) {
//            messageList.add(getNotNullMessage("userCode"));
//        }
//        if (StringUtil.isEmpty(user.getName())) {
//            messageList.add(getNotNullMessage("userName"));
//        }
//        if (null == user.getStatus()) {
//            messageList.add(getNotNullMessage("status"));
//        }
//        if (StringUtil.isEmpty(user.getPassword())) {
//            messageList.add(getNotNullMessage("password"));
//        }
        // 判断用户编号是否重复
        if (createFlag && messageList.size() == 0) {
            Integer count = userProfileDao.selectCount(new QueryWrapper<UserProfile>().eq("code", user.getCode()));
            if (null != count && count > 0) {
                messageList.add(getMessageByParams("dataRepeated", new String[]{LanguageUtil.getMessage("userCode")}));
            }
        }
        // 判断是否为收银员
//        if (null != param.getCashierTag() && param.getCashierTag().equals(1) && null == param.getDiscount()) {
//            messageList.add(getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage("discount")}));
//        }
        if (CollUtil.isNotEmpty(messageList)) {
            return;
        }
        //  收银员渠道范围
        if (CollUtil.isNotEmpty(param.getCashierChannels())) {
            List<UserCashierChannel> cashierChannelList = new ArrayList<>();
            context.setChannelList(cashierChannelList);
            List<Channel> channelList = channelDao.selectList(new QueryWrapper<Channel>().select("id,code")
                    .eq("status", StatusEnum.CHECK.getStatus()).in("code", StreamUtil.toSet(param.getCashierChannels(), UserCashierChannelDto::getChannelCode)));
            if (CollUtil.isEmpty(channelList)) {
                messageList.add(getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage("singleChannelCode")}));
                return;
            }
            Map<String, Long> channelMap = channelList.stream().collect(Collectors.toMap(Channel::getCode, Channel::getId, (x1, x2) -> x1));
            int i = 0;
            for (UserCashierChannelDto dto : param.getCashierChannels()) {
                i++;
                UserCashierChannel entity = UserCashierChannel.build();
                cashierChannelList.add(entity);

                entity.setUserId(user.getId());
                entity.setChannelId(channelMap.get(dto.getChannelCode()));
                if (null == entity.getChannelId()) {
                    messageList.add(getNotExistMessage(i, "singleChannelCode"));
                }
            }
        }
        //  收银员渠道范围
        if (CollUtil.isNotEmpty(param.getCashierDiscount())) {
            List<UserCashierLowerDiscount> userCashierLowerDiscountList = new ArrayList<>();
            context.setLowerDiscountList(userCashierLowerDiscountList);
            // 货品类别
            List<IdNameDto> categoryList = dbService.selectIdNameList(new QueryWrapper<Category>().in("name", StreamUtil.toSet(param.getCashierDiscount(), UserCashierDiscountDto::getCategory)), Category.class);
            if (CollUtil.isEmpty(categoryList)) {
                messageList.add(getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage("goodsCategory")}));
                return;
            }
            Map<String, Long> categoryMap = categoryList.stream().collect(Collectors.toMap(IdNameDto::getName, v -> (Long) v.getId(), (x1, x2) -> x1));
            // 年份
            List<IdNameDto> yearList = dbService.selectIdNameList(new QueryWrapper<Year>().in("name", StreamUtil.toSet(param.getCashierDiscount(), UserCashierDiscountDto::getYear)), Year.class);
            if (CollUtil.isEmpty(yearList)) {
                messageList.add(getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage("year")}));
                return;
            }
            Map<String, Long> yearMap = yearList.stream().collect(Collectors.toMap(IdNameDto::getName, v -> (Long) v.getId(), (x1, x2) -> x1));
            int i = 0;
            for (UserCashierDiscountDto dto : param.getCashierDiscount()) {
                i++;
                UserCashierLowerDiscount entity = UserCashierLowerDiscount.build();
                userCashierLowerDiscountList.add(entity);

                entity.setUserId(user.getId());
                entity.setLowerDiscount(dto.getLowerDiscount());
                entity.setCategoryId(categoryMap.get(dto.getCategory()));
                entity.setYearId(yearMap.get(dto.getYear()));
                if (null == entity.getCategoryId()) {
                    messageList.add(getNotExistMessage(i, "goodsCategory"));
                }
                if (null == entity.getYearId()) {
                    messageList.add(getNotExistMessage(i, "year"));
                }
                if (null == entity.getLowerDiscount()) {
                    messageList.add(getNotNullMessage(i, "lowerDiscount"));
                }
            }
        }

    }

    /**
     * 将查询参数转换成 查询的上下文
     *
     * @param param
     * @param context
     */
    private void convertQueryContext(UserQueryParam param, UserQueryContext context) {
        BeanUtil.copyProperties(param, context);
    }


    /**
     * 整理查询条件构造器
     *
     * @param context
     * @return
     */
    private QueryWrapper processQueryWrapper(UserQueryContext context) {
        QueryWrapper queryWrapper = new QueryWrapper<UserProfile>();

        queryWrapper.eq(StringUtil.isNotEmpty(context.getCode()), "code", context.getCode());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getName()), "name", context.getName());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getNotes()), "notes", context.getNotes());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getDepartment()), "department", context.getDepartment());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getPosition()), "position", context.getPosition());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getMobile()), "mobile", context.getMobile());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getEmail()), "email", context.getEmail());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getIdCard()), "idCard", context.getIdCard());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getWeixin()), "weixin", context.getWeixin());
        queryWrapper.eq(StringUtil.isNotEmpty(context.getQyweixin()), "qyweixin", context.getQyweixin());

        queryWrapper.in(null != context.getStatus() && context.getStatus().length > 0, "status", context.getStatus());
        queryWrapper.in(null != context.getType() && context.getType().length > 0, "type", context.getType());
        queryWrapper.in(null != context.getSex() && context.getSex().length > 0, "sex", context.getSex());
        queryWrapper.in(null != context.getCashierTag() && context.getCashierTag().length > 0, "cashier_tag", context.getCashierTag());


        queryWrapper.eq(null != context.getBirthdayDate(), "birthday_date", context.getBirthdayDate());
        queryWrapper.eq(null != context.getWorkDate(), "work_date", context.getWorkDate());
        queryWrapper.ge(null != context.getCreatedDateStart(), "created_time", context.getCreatedDateStart());
        queryWrapper.le(null != context.getCreatedDateEnd(), "created_time", context.getCreatedDateEnd());
        queryWrapper.ge(null != context.getUpdatedDateStart(), "updated_time", context.getUpdatedDateStart());
        queryWrapper.le(null != context.getUpdatedDateEnd(), "updated_time", context.getUpdatedDateEnd());

        return queryWrapper;
    }

    /**
     * 处理查询结果的属性
     *
     * @param list
     * @return
     */
    private List<UserQueryResult> convertQueryResult(List<UserProfile> list) {
        List<UserQueryResult> queryResults = new ArrayList<>(list.size());
        if (CollUtil.isEmpty(list)) {
            return queryResults;
        }
        List<Long> userIdList = list.stream().map(UserProfile::getId).distinct().collect(Collectors.toList());
        //收银员渠道范围
        Map<Long, List<UserCashierChannelDto>> cashierChannelListMap = new HashMap<>();
        List<UserCashierChannel> cashierChannelList = userCashierChannelDao.selectList(new QueryWrapper<UserCashierChannel>().in("user_id", userIdList));
        if (CollUtil.isNotEmpty(cashierChannelList)) {
            Map<Long, IdNameCodeDto> channelMap = dbService.selectIdNameCodeMapByLanguage(new QueryWrapper<Channel>().in("id", StreamUtil.toSet(cashierChannelList, UserCashierChannel::getChannelId)), Channel.class, LanguageTableEnum.CHANNEL);
            cashierChannelList.stream().collect(Collectors.groupingBy(UserCashierChannel::getUserId)).forEach((userId, cashierChannels) -> {
                List<UserCashierChannelDto> dtoList = new ArrayList<>();
                cashierChannels.forEach(item -> {
                    UserCashierChannelDto dto = new UserCashierChannelDto();
                    dtoList.add(dto);
                    dto.setChannelCode(OptionalUtil.ofNullable(channelMap.get(item.getChannelId()), IdNameCodeDto::getCode));
                });
                cashierChannelListMap.put(userId, dtoList);
            });
        }
        //收银员最低折扣
        Map<Long, List<UserCashierDiscountDto>> cashierDiscountListMap = new HashMap<>();
        List<UserCashierLowerDiscount> cashierLowerDiscountList = userCashierLowerDiscountDao.selectList(new QueryWrapper<UserCashierLowerDiscount>().in("user_id", userIdList));
        if (CollUtil.isNotEmpty(cashierLowerDiscountList)) {
            Map<Object, IdNameDto> categoryMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<Category>().in("id", StreamUtil.toSet(cashierLowerDiscountList, UserCashierLowerDiscount::getCategoryId)), Category.class, LanguageTableEnum.CATEGORY);
            Map<Object, IdNameDto> yearMap = dbService.selectIdNameMapByLanguage(new QueryWrapper<Year>().in("id", StreamUtil.toSet(cashierLowerDiscountList, UserCashierLowerDiscount::getYearId)), Year.class, LanguageTableEnum.CATEGORY);
            cashierLowerDiscountList.stream().collect(Collectors.groupingBy(UserCashierLowerDiscount::getUserId)).forEach((userId, discounts) -> {
                List<UserCashierDiscountDto> dtoList = new ArrayList<>();
                discounts.forEach(item -> {
                    UserCashierDiscountDto dto = new UserCashierDiscountDto();
                    dtoList.add(dto);
                    dto.setLowerDiscount(item.getLowerDiscount());
                    dto.setCategory(OptionalUtil.ofNullable(categoryMap.get(item.getCategoryId()), IdNameDto::getName));
                    dto.setYear(OptionalUtil.ofNullable(yearMap.get(item.getYearId()), IdNameDto::getName));
                });
                cashierDiscountListMap.put(userId, dtoList);
            });
        }
        // 遍历
        for (UserProfile user : list) {
            UserQueryResult queryResult = new UserQueryResult();
            BeanUtil.copyProperties(user, queryResult);
            queryResults.add(queryResult);
            // 渠道范围
            queryResult.setCashierChannels(cashierChannelListMap.get(user.getId()));
            // 最低折扣
            queryResult.setCashierDiscount(cashierDiscountListMap.get(user.getId()));
        }
        return queryResults;
    }

    /**
     * 返回非空信息
     *
     * @param key
     * @return
     */
    private static String getNotNullMessage(String key) {
        return getMessageByParams("dataNotNull", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotExistMessage(String key) {
        return getMessageByParams("dataNotExist", new String[]{LanguageUtil.getMessage(key)});
    }

    private static String getNotNullMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotNullMessage(key)});
    }

    private static String getNotExistMessage(Integer index, String key) {
        return getMessageByParams("dataWhichRow", new Object[]{index, getNotExistMessage(key)});
    }

    public static String getMessageByParams(String languageKey, Object[] params) {
        return LanguageUtil.getMessage(LanguageUtil.ZH, languageKey, params);
    }

}
