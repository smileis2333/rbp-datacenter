package com.regent.rbp.task.inno.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatform;
import com.regent.rbp.api.core.onlinePlatform.OnlinePlatformSyncCache;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformDao;
import com.regent.rbp.api.dao.onlinePlatform.OnlinePlatformSyncCacheDao;
import com.regent.rbp.api.service.base.OnlinePlatformSyncCacheService;
import com.regent.rbp.api.service.constants.SystemConstants;
import com.regent.rbp.common.dao.UserDao;
import com.regent.rbp.infrastructure.annotation.PassToken;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.task.inno.model.dto.EmployeeDto;
import com.regent.rbp.task.inno.model.req.EmployeeReqDto;
import com.regent.rbp.task.inno.model.resp.EmployeeRespDto;
import com.regent.rbp.task.inno.service.EmployeeService;
import com.xxl.job.core.context.XxlJobHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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
    private static final String ERROR_EMPLOYEE_LIST = "[inno推送员工信息]:当前无数据需要同步";

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
    @Autowired
    OnlinePlatformSyncCacheService onlinePlatformSyncCacheService;

    @Override
    public OnlinePlatform getOnlinePlatform(String onlinePlatformCode) {
        // 获得需要同步的平台信息
        OnlinePlatform onlinePlatform = onlinePlatformDao.selectOne(new QueryWrapper<OnlinePlatform>()
                .eq("code", onlinePlatformCode));

        return onlinePlatform;
    }

    /***
     * 员工档案上传
     * @param onlinePlatform
     * @return
     */
    @Override
    @PassToken
    public void uploadingEmployee(OnlinePlatform onlinePlatform) {
        try {
            Long onlinePlatformId = onlinePlatform.getId();
            String key = SystemConstants.POST_ERP_EMPLOYEE;
            Date uploadingDate = onlinePlatformSyncCacheService.getOnlinePlatformSyncCacheByDate(onlinePlatformId, key);

            QueryWrapper<Employee> queryWrapper = new QueryWrapper<Employee>();
            if (uploadingDate != null) {
                queryWrapper.ge("updated_time", uploadingDate);
            }
            queryWrapper.orderByAsc("updated_time");
            List<Employee> employeeList = employeeDao.selectList(queryWrapper);
            if (CollUtil.isNotEmpty(employeeList)) {
                Date uploadingTime = employeeList.stream().max(Comparator.comparing(Employee::getUpdatedTime)).get().getUpdatedTime();
                for (Employee employee : employeeList) {
                    List<EmployeeDto> reqList = new ArrayList<>();
                    Channel channel = channelDao.selectById(employee.getChannelId());
                    String isEnabled = "1";
                    String updateTimeStr = DateUtil.getFullDateStr(employee.getUpdatedTime());
                    String channelCode = channel !=null ? channel.getCode() : StrUtil.EMPTY;
                    String openId = StrUtil.EMPTY;
                    Integer status = employee.getWorkStatus() != 2 ? 1 : 0;
                    EmployeeDto employeeDto = new EmployeeDto(employee.getCode(),employee.getName(),channelCode,employee.getMobile(),openId,updateTimeStr,isEnabled,status);
                    reqList.add(employeeDto);

                    EmployeeReqDto employeeReqDto = new EmployeeReqDto();
                    employeeReqDto.setApp_key(onlinePlatform.getAppKey());
                    employeeReqDto.setApp_secrept(onlinePlatform.getAppSecret());
                    employeeReqDto.setData(reqList);
                    String api_url = String.format("%s%s", onlinePlatform.getExternalApplicationApiUrl(), POST_ERP_STORESTAFF);
                    String result = HttpUtil.post(api_url, JSON.toJSONString(employeeReqDto));
                    EmployeeRespDto respDto = JSON.parseObject(result, EmployeeRespDto.class);
                    if (respDto.getCode().equals("-1")) {
                        XxlJobHelper.log(respDto.getMsg());
                    }
                    XxlJobHelper.log(respDto.getMsg());
                }
                this.saveOnlinePlatformSyncCache(onlinePlatformId, key, uploadingTime);
                XxlJobHelper.log("上传完成");
            }else{
                XxlJobHelper.log(ERROR_EMPLOYEE_LIST);
            }
        } catch (Exception e) {
            XxlJobHelper.handleFail(e.getMessage());
        }
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
            syncCache = syncCache.build(onlinePlatformId, key, DateUtil.getDateStr(uploadingTime, DateUtil.FULL_DATE_FORMAT));
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
