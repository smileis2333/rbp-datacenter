package com.regent.rbp.task.inno.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.excel.util.DateUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.core.integral.MemberIntegral;
import com.regent.rbp.api.core.member.MemberCard;
import com.regent.rbp.api.core.member.MemberPolicy;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dao.member.MemberCardDao;
import com.regent.rbp.api.dao.member.MemberIntegralDao;
import com.regent.rbp.api.dao.member.MemberPolicyDao;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.member.MemberCardSaveParam;
import com.regent.rbp.api.service.base.OnlinePlatformService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.base.OnlinePlatformSyncErrorService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.api.service.member.MemberCardService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import com.regent.rbp.task.inno.model.dto.CustomerVipDto;
import com.regent.rbp.task.inno.model.dto.InnoMemberDto;
import com.regent.rbp.task.inno.model.dto.MemberPageDto;
import com.regent.rbp.task.inno.model.dto.SaveMemberDto;
import com.regent.rbp.task.inno.model.param.DownloadMemberParam;
import com.regent.rbp.task.inno.model.req.MemberReqDto;
import com.regent.rbp.task.inno.model.req.SaveMemberReqDto;
import com.regent.rbp.task.inno.model.resp.MemberPageDataRespDto;
import com.regent.rbp.task.inno.model.resp.MemberRespDto;
import com.regent.rbp.task.inno.service.MemberService;
import com.xxl.job.core.context.XxlJobHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
    private static final String API_Get_USERS_LIST = "api/Users/Get_User_List";

    private static final String MEMBER_TYPE = "普通会员";
    private static final String ORIGIN_TYPE = "线上";
    private static final String ORIGIN = "英朗";
    private static final String MEMBER_STATUS = "正常";

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
    EmployeeDao employeeDao;

    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;
    @Autowired
    OnlinePlatformService onlinePlatformService;
    @Autowired
    MemberCardService memberCardService;
    @Autowired
    OnlinePlatformSyncErrorService onlinePlatformSyncErrorService;


    @Override
    public void uploadingMember(OnlinePlatform onlinePlatform) {
        String key = SystemConstants.POST_ERP_USERS;
        Long onlinePlatformId = onlinePlatform.getId();
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        Map<String, Long> memberMap = new HashMap<>();
        if (map.size() > 0) {
            List<Long> memberIds = map.values().stream().collect(Collectors.toList());
            List<MemberCard> memberList = memberCardDao.selectList(new LambdaQueryWrapper<MemberCard>().in(MemberCard::getId, memberIds));
            memberMap = memberList.stream().collect(Collectors.toMap(v -> v.getCode(), v -> v.getId(), (x1, x2) -> x1));
            Map<Integer, List<InnoMemberDto>> listMap = this.packaging(memberList);
            for (Map.Entry<Integer, List<InnoMemberDto>> entry : listMap.entrySet()) {
                MemberRespDto respDto = this.pushMember(onlinePlatform, entry.getValue());
                if (respDto.getCode().equals("-1")) {
                    for (InnoMemberDto innoMemberDto : entry.getValue()) {
                        Long memberId = memberMap.get(innoMemberDto.getCard_no());
                        Long errorId = map.get(memberId.toString());
                        onlinePlatformSyncErrorService.failure(errorId);
                    }
                }
            }
        }

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

        QueryWrapper<MemberCard> queryWrapper = new QueryWrapper<MemberCard>();
        // 排除 （更新来源 0.RBP,1.INNO） 为 inno 的会员信息
        queryWrapper.ne("updated_origin", 1);
        if (uploadingDate != null) {
            queryWrapper.ge("updated_time", uploadingDate);
        }

        List<MemberCard> memberCardList = memberCardDao.selectList(queryWrapper);
        memberMap = memberCardList.stream().collect(Collectors.toMap(v -> v.getCode(), v -> v.getId(), (x1, x2) -> x1));
        Map<Integer, List<InnoMemberDto>> memberCardMap = this.packaging(memberCardList);
        for (Map.Entry<Integer, List<InnoMemberDto>> entry : memberCardMap.entrySet()) {
            MemberRespDto respDto = this.pushMember(onlinePlatform, entry.getValue());
            if (respDto.getCode().equals("-1")) {
                for (InnoMemberDto innoMemberDto : entry.getValue()) {
                    Long memberId = memberMap.get(innoMemberDto.getCard_no());
                    onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, key, memberId.toString());
                }
            }
        }
        Date uploadingTime = memberCardList.stream().max(Comparator.comparing(MemberCard::getUpdatedTime)).get().getUpdatedTime();
        onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
    }

    /**
     * 推送会员打包
     * @param memberCardList
     * @return
     */
    private Map<Integer, List<InnoMemberDto>> packaging(List<MemberCard> memberCardList) {
        Map<Integer, List<InnoMemberDto>> listMap = new HashMap<>();
        // 发卡渠道
        List<Long> channelIds = memberCardList.stream().map(MemberCard::getChannelId).distinct().collect(Collectors.toList());
        List<Channel> channelList = channelDao.selectBatchIds(channelIds);
        Map<Long, String> channelMap = channelList.stream().collect(Collectors.toMap(Channel::getId, Channel::getCode));
        /*// 性别
        List<Long> sexIds = memberCardList.stream().map(MemberCard::getSexId).distinct().collect(Collectors.toList());
        List<Sex> sexList = sexDao.selectBatchIds(sexIds);
        Map<Long, String> sexMap = sexList.stream().collect(Collectors.toMap(Sex::getId, Sex::getName));*/
        // 会员政策
        List<Long> memberPolicyIds = memberCardList.stream().map(MemberCard::getMemberPolicyId).distinct().collect(Collectors.toList());
        List<MemberPolicy> memberPolicyList = memberPolicyDao.selectBatchIds(memberPolicyIds);
        Map<Long, String> memberPolicyMap = memberPolicyList.stream().collect(Collectors.toMap(MemberPolicy::getId, MemberPolicy::getGradeCode));

        Integer sumQty = memberCardList.size();
        double pageNo = Math.ceil(Double.valueOf(sumQty) / new Double(100));
        for (int i = 0; i < pageNo; i++) {
            Integer rowIndex = 1;
            List<InnoMemberDto> dtoList = new ArrayList<>();
            List<MemberCard> cardList = new ArrayList<>();
            if (sumQty < 100 || i == pageNo - 1) {
                cardList = memberCardList.subList(i * 100, sumQty);
            } else {
                cardList = memberCardList.subList(i * 100, ((i + 1) * 100));
            }
            for (MemberCard card : cardList) {
                InnoMemberDto dto = new InnoMemberDto();
                dto.setRowIndex(rowIndex);
                dto.setUser_name(card.getName());
                dto.setCard_no(card.getCode());
                dto.setNick_name(card.getName());
                dto.setEmail(card.getEmail());
                dto.setSex(card.getSexName());
                dto.setBirthday(DateUtil.getFullDateStr(card.getBirthdayDate()));
                dto.setMobile_no(card.getPhone());
                dto.setCreate_date(DateUtil.getFullDateStr(card.getCreatedTime()));
                dto.setModify_date(DateUtil.getFullDateStr(card.getUpdatedTime()));

                if (card.getChannelId() != null && channelMap.containsKey(card.getChannelId())) {
                    dto.setStore_code(channelMap.get(card.getChannelId()));
                    dto.setStaff_code(channelMap.get(card.getChannelId()));
                }
                if (card.getMemberPolicyId() != null && memberPolicyMap.containsKey(card.getMemberPolicyId())) {
                    dto.setRank_code(memberPolicyMap.get(card.getMemberPolicyId()));
                }

                MemberIntegral memberIntegral = memberIntegralDao.selectOne(new QueryWrapper<MemberIntegral>().eq("member_card_id", card.getId()));
                if (memberIntegral != null) {
                    dto.setHistory_point(memberIntegral.getIntegral());
                } else {
                    dto.setHistory_point(0);
                }
                dtoList.add(dto);
                rowIndex++;
            }
            listMap.put(i + 1, dtoList);
        }
        return listMap;
    }

    /**
     * 推送会员
     * @param onlinePlatform
     * @param innoMemberDtoList
     * @return
     */
    private MemberRespDto pushMember(OnlinePlatform onlinePlatform, List<InnoMemberDto> innoMemberDtoList) {
        MemberReqDto memberReqDto = new MemberReqDto();
        memberReqDto.setApp_key(onlinePlatform.getAppKey());
        memberReqDto.setApp_secrept(onlinePlatform.getAppSecret());
        memberReqDto.setData(innoMemberDtoList);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), API_URL_USERS);
        String result = HttpUtil.post(api_url, JSON.toJSONString(memberReqDto));
        XxlJobHelper.log("请求Json：" + JSON.toJSONString(memberReqDto));
        XxlJobHelper.log("返回Json：" + result);
        MemberRespDto respDto = JSON.parseObject(result, MemberRespDto.class);
        return respDto;
    }

    @Override
    public void saveMember(DownloadMemberParam param) throws Exception {
        String key = SystemConstants.GET_USER_LIST;
        OnlinePlatform onlinePlatform = onlinePlatformService.getOnlinePlatform(param.getOnlinePlatformCode());
        Long onlinePlatformId = onlinePlatform.getId();
        Map<String, Long> map = onlinePlatformSyncErrorService.getErrorBillId(key);
        if (map.size() > 0) {
            SaveMemberDto memberDto = new SaveMemberDto();
            memberDto.setCardnumList(StringUtils.join(map.keySet(), ","));
            memberDto.setPageIndex(1);
            this.pullMember(onlinePlatform, memberDto, map);
        }

        Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

        try {
            SaveMemberDto dto = new SaveMemberDto();
            SimpleDateFormat sdf = new SimpleDateFormat(SystemConstants.FULL_DATE_FORMAT);

            // 设置结束时间
            dto.setEndTime(DateUtil.getNowDateString(SystemConstants.FULL_DATE_FORMAT));
            if (null == uploadingDate) {
                dto.setBeginTime(sdf.format(DateUtil.getDate("2021-10-01", DateUtil.SHORT_DATE_FORMAT)));
            } else {
                dto.setBeginTime(DateUtil.getDateStr(uploadingDate, SystemConstants.FULL_DATE_FORMAT));
            }

            if (param.getMobileList() != null && param.getMobileList().size() > 0) {
                dto.setMobileList(StringUtils.join(param.getMobileList(), ","));
            }
            if (param.getCardnumList() != null && param.getCardnumList().size() > 0) {
                dto.setCardnumList(StringUtils.join(param.getCardnumList(), ","));
            }
            dto.setPageIndex(1);
            this.pullMember(onlinePlatform, dto, null);
            onlinePlatformSyncCacheService.saveOnlinePlatformSyncCache(onlinePlatformId, key, DateUtils.parseDate(dto.getEndTime()));
            XxlJobHelper.handleSuccess();
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }

    }

    /**
     * 拉取会员
     * @param onlinePlatform
     * @param dto
     * @param map
     * @throws Exception
     */
    private void pullMember(OnlinePlatform onlinePlatform, SaveMemberDto dto, Map<String, Long> map) throws Exception {
        SaveMemberReqDto reqDto = new SaveMemberReqDto();
        reqDto.setApp_key(onlinePlatform.getAppKey());
        reqDto.setApp_secrept(onlinePlatform.getAppSecret());
        reqDto.setData(dto);

        String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), API_Get_USERS_LIST);
        String result = HttpUtil.post(api_url, JSON.toJSONString(reqDto));

        XxlJobHelper.log(String.format("请求Url：%s", api_url));
        XxlJobHelper.log(String.format("请求Json：%s", JSON.toJSONString(reqDto)));
        XxlJobHelper.log(String.format("返回Json：%s", result));

        MemberPageDataRespDto respDto = JSON.parseObject(result, MemberPageDataRespDto.class);
        if (respDto.getCode().equals("-1")) {
            throw new Exception(respDto.getMsg());
        }
        if (respDto.getData().getData().size() > 0) {
            for (MemberPageDto pageDto : respDto.getData().getData()) {
                this.saveMember(onlinePlatform.getId(), pageDto, map);
            }
            for (int i = 2; i <= reqDto.getData().getPageIndex(); i++) {
                dto.setPageIndex(i);
                this.pullMember(onlinePlatform, dto, map);
            }
        }
    }

    /**
     * 写入会员
     * @param onlinePlatformId
     * @param page
     * @param map
     */
    private void saveMember(Long onlinePlatformId, MemberPageDto page, Map<String, Long> map) {
        // 写入会员
        MemberCardSaveParam saveParam = new MemberCardSaveParam();
        saveParam.setCode(page.getCard_no());
        saveParam.setPassword(page.getPay_password());
        saveParam.setName(page.getNick_name());
        saveParam.setMemberType(MEMBER_TYPE);
        // 区号
        saveParam.setAreaCode("");
        saveParam.setPhone(page.getMobile_no());
        saveParam.setSex(page.getSex() == "男" ? 0:1);
        saveParam.setOriginType(1);
        saveParam.setOrigin(2);
        saveParam.setBeginDate(page.getCreate_date());
        saveParam.setEndDate("2999-12-01");
        saveParam.setChannelCode(page.getStore_code());
        saveParam.setUserCode(page.getStaff_code());
        saveParam.setRepairChannelCode(page.getStore_code());
        saveParam.setMaintainerCode(page.getStaff_code());
        saveParam.setNation("");
        saveParam.setProvince(page.getProvince_name());
        saveParam.setCity(page.getCity_name());
        saveParam.setArea(page.getDistrict_name());
        saveParam.setAddress(page.getAddress());
        // 生日
        saveParam.setBirthday(page.getBirthday());
        Date birthday = DateUtil.getDate(page.getBirthday(), DateUtil.SHORT_DATE_FORMAT);
        saveParam.setBirthdayYear(DateUtil.getYear(birthday));
        saveParam.setBirthdayMouth(DateUtil.getMonth(birthday));
        saveParam.setBirthdayDay(DateUtil.getDay(birthday));

        saveParam.setMemberStatus("正常");
        saveParam.setEmail(page.getEmail());
        saveParam.setWeixin(page.getWap_openid());
        saveParam.setNotes("定时拉取 Inno 会员信息生成");
        saveParam.setMemberPolicyCode(page.getRank_code());
        saveParam.setUpdatedOrigin(1);
        saveParam.setUnionId(page.getUnionid());

        DataResponse response = memberCardService.save(saveParam);
        if (map != null && map.containsKey(page.getCard_no())) {
            Long errorId = map.get(page.getCard_no());
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.failure(errorId);
            } else {
                onlinePlatformSyncErrorService.succeed(errorId);
            }
        } else {
            if (response.getCode() != ResponseCode.OK) {
                onlinePlatformSyncErrorService.saveOnlinePlatformSyncError(onlinePlatformId, SystemConstants.GET_USER_LIST, page.getCard_no());
                XxlJobHelper.log(String.format("错误信息：%s %s", saveParam.getCode(), response.getMessage()));
            }
        }
    }

    @Override
    public Map<String, String> save(CustomerVipDto dto, Boolean createFlag) {
        HashMap<String, String> response = new HashMap<>();

        MemberCardSaveParam saveParam = new MemberCardSaveParam();
        List<String> errorMsgList = this.verificationProperty(dto, saveParam, createFlag);
        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            response.put("Flag", "-1");
            response.put("Message", message);
            response.put("data", dto.getVIP());
            return response;
        }
        DataResponse saveResp = memberCardService.save(saveParam);
        if (saveResp.getCode() != ResponseCode.OK){
            response.put("Flag", "-1");
            response.put("Message", saveResp.getMessage());
            response.put("data", dto.getVIP());
        } else {
            response.put("Flag", "1");
            if (createFlag) {
                response.put("Message", "会员新增成功");
            } else {
                response.put("Message", "会员更新成功");
            }
            response.put("data", "");
        }
        return response;
    }

    /**
     * 数据校验
     * @param dto
     * @return
     */
    private List<String> verificationProperty(CustomerVipDto dto, MemberCardSaveParam saveParam, Boolean createFlag) {
        List<String> errorMsgList = new ArrayList<>();
        if (dto == null) {
            errorMsgList.add("参数不能为空");
        }
        if (StringUtils.isEmpty(dto.getVIP())) {
            errorMsgList.add("Vip卡号(VIP)不能为空！");
        }
        if(StringUtil.isEmpty(dto.getMobileTel())) {
            errorMsgList.add("手机(MobileTel) 会员手机号为空,已跳过！");
        }

        if (createFlag) {
            if (memberCardService.checkExistMemberCard(dto.getVIP())) {
                errorMsgList.add("Vip卡号(VIP)已存在！");
            }
            if(StringUtil.isNotEmpty(dto.getMobileTel())) {
                if(memberCardService.checkExistMobile(dto.getMobileTel())) {
                    errorMsgList.add("手机号(MobileTel)已存在！");
                }
            }
        } else {
            if (!memberCardService.checkExistMemberCard(dto.getVIP())) {
                errorMsgList.add("Vip卡号(VIP)不存在！");
            }
            if (memberCardService.checkExistStatus(dto.getVIP())) {
                errorMsgList.add("Vip卡号(VIP)审核状态为作废，不更新数据！");
            }
        }

        if (StringUtil.isNotEmpty(dto.getVipGrade())) {
            MemberPolicy policy = memberPolicyDao.selectOne(new QueryWrapper<MemberPolicy>().eq("grade_code", dto.getVipGrade()));
            if (policy != null) {
                saveParam.setMemberPolicyCode(dto.getVipGrade());
            } else {
                errorMsgList.add("等级(VipGrade)不存在");
            }
        }
        if (StringUtil.isNotEmpty(dto.getOriginalCustomer())) {
            Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", dto.getOriginalCustomer()));
            if (channel != null) {
                saveParam.setChannelCode(dto.getOriginalCustomer());
            } else {
                errorMsgList.add("原渠道(OriginalCustomer)不存在");
            }
        }
        if (StringUtil.isNotEmpty(dto.getCustomer_ID())) {
            Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", dto.getCustomer_ID()));
            if (channel != null) {
                saveParam.setRepairChannelCode(dto.getCustomer_ID());
            } else {
                errorMsgList.add("店铺编号(Customer_ID)不存在");
            }
        }
        if (StringUtil.isNotEmpty(dto.getBusinessManID())) {
            Employee item = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", dto.getBusinessManID()));
            if (item != null) {
                saveParam.setUserCode(dto.getBusinessManID());
                saveParam.setMaintainerCode(dto.getBusinessManID());
            } else {
                errorMsgList.add("营业员(BusinessManID)不存在");
            }
        }

        saveParam.setCode(dto.getVIP());
        saveParam.setPassword(dto.getPasswords());
        saveParam.setName(dto.getName());
        saveParam.setMemberType(MEMBER_TYPE);

        // 区号
        saveParam.setAreaCode("");
        saveParam.setPhone(dto.getMobileTel());
        saveParam.setSex(dto.getSex() == "男" ? 0 : 1);
        saveParam.setOriginType(1);
        saveParam.setOrigin(2);
        saveParam.setBeginDate(dto.getBegainDate());
        saveParam.setEndDate(dto.getExpireDate());
        saveParam.setRepairChannelCode(dto.getCustomer_ID());
        saveParam.setNation("");
        saveParam.setProvince(dto.getCity());
        saveParam.setCity(dto.getInCity());
        saveParam.setArea("");
        saveParam.setAddress(dto.getAddress());
        // 生日
        saveParam.setBirthday(dto.getBirthDate());
        Date birthday = DateUtil.getDate(dto.getBirthDate(), DateUtil.SHORT_DATE_FORMAT);
        saveParam.setBirthdayYear(DateUtil.getYear(birthday));
        saveParam.setBirthdayMouth(DateUtil.getMonth(birthday));
        saveParam.setBirthdayDay(DateUtil.getDay(birthday));

        saveParam.setMemberStatus(MEMBER_STATUS);
        saveParam.setEmail(dto.getEmail());
        saveParam.setWeixin("");
        //saveParam.setNotes("Inno 新建生成");

        saveParam.setUpdatedOrigin(1);
        return errorMsgList;
    }
}
