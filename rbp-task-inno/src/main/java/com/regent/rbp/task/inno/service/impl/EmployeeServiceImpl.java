package com.regent.rbp.task.inno.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.base.User;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.base.UserDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.config.InnoConfig;
import com.regent.rbp.task.inno.model.dto.EmployeeDto;
import com.regent.rbp.task.inno.model.req.EmployeeReqDto;
import com.regent.rbp.task.inno.model.resp.EmployeeRespDto;
import com.regent.rbp.task.inno.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/23
 **/
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final String POST_ERP_STORESTAFF = "api/StoreStaff/Post_ErpStoreStaff";

    @Autowired
    private InnoConfig innoConfig;

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    OnlinePlatformDao onlinePlatformDao;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OnlinePlatformSyncCacheDao onlinePlatformSyncCacheDao;

    @Override
    public OnlinePlatform getOnlinePlatform(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>().select("id", "code", "name")
                .eq("code", onlinePlatformCode));

        return onlinePlatform;
    }

    /***
     * 员工档案上传
     * @param onlinePlatformId
     * @param channelId
     * @return
     */
    @Override
    public EmployeeRespDto uploadingEmployee(Long onlinePlatformId, Long channelId) {
        EmployeeRespDto respDto = null;
        String key = SystemConstants.POST_ERP_EMPLOYEE;
        Employee employee = employeeDao.selectById(channelId);
        if (employee != null) {
            Channel channel = channelDao.selectById(employee.getChannelId());
            User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getCode,employee.getCode()));
            Date uploadingDate = this.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);
            if (uploadingDate != null && employee.getUpdatedTime().getTime() <= uploadingDate.getTime())
            {
                return respDto;
            }
            List<EmployeeDto> reqList = new ArrayList<>();
            String isEnabled = "1";
            String updateTimeStr = DateUtil.getFullDateStr(employee.getUpdatedTime());
            String channelCode = channel !=null ? channel.getCode() : StrUtil.EMPTY;
            String openId = user !=null ? user.getQyweixin() : StrUtil.EMPTY;
            Integer status = employee.getWorkStatus() != 2 ? 1 : 0;
            EmployeeDto employeeDto = new EmployeeDto(employee.getCode(),employee.getName(),channelCode,employee.getMobile(),openId,updateTimeStr,isEnabled,status);
            reqList.add(employeeDto);

            EmployeeReqDto employeeReqDto = new EmployeeReqDto();
            employeeReqDto.setApp_key(innoConfig.getAppkey());
            employeeReqDto.setApp_secrept(innoConfig.getAppsecret());
            employeeReqDto.setData(reqList);

            String api_url = String.format("%s%s", innoConfig.getUrl(), POST_ERP_STORESTAFF);
            String result = HttpUtil.post(api_url, JSON.toJSONString(employeeReqDto));

            respDto = JSON.parseObject(result, EmployeeRespDto.class);

            Date uploadingTime = employee.getUpdatedTime();
            this.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
        }
        return respDto;
    }

    /***
     * 保存/修改 配置表
     * @param onlinePlatformId
     * @param key
     * @param uploadingTime
     */
    private void saveOnlinePlatformSyncCache(Long onlinePlatformId, String key, Date uploadingTime) {
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            syncCache.build(onlinePlatformId, key, DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.insert(syncCache);
        } else {
            syncCache.setData(DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
            onlinePlatformSyncCacheDao.updateById(syncCache);
        }
    }

    /***
     * 获取配置表最大时间
     * @param onlinePlatformId
     * @param key
     * @return
     */
    private Date getOnlinePlatformSyncCacheByDate(Long onlinePlatformId, String key) {
        // 获取任务执行缓存
        OnlinePlatformSyncCache syncCache = onlinePlatformSyncCacheDao.selectOne(new LambdaQueryWrapper<OnlinePlatformSyncCache>()
                .eq(OnlinePlatformSyncCache::getOnlinePlatformId, onlinePlatformId).eq(OnlinePlatformSyncCache::getSyncKey, key));
        if (syncCache == null) {
            return null;
        }
        Date cacheTime = DateUtil.getDate(syncCache.getData(), DateUtil.FULL_DATE_FORMAT);
        // 默认查询10分钟前
        Date time = new Date(cacheTime.getTime() - SystemConstants.DEFAULT_TEN_MINUTES);
        return time;
    }
}
