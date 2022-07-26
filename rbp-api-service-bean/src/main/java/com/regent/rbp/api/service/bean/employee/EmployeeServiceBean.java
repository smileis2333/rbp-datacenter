package com.regent.rbp.api.service.bean.employee;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.regent.rbp.api.core.base.Position;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.dao.base.PositionDao;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.employee.EmployeeQueryParam;
import com.regent.rbp.api.dto.employee.EmployeeQueryResult;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.api.dto.user.UserQueryParam;
import com.regent.rbp.api.dto.user.UserQueryResult;
import com.regent.rbp.api.dto.user.UserSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.employee.EmployeeService;
import com.regent.rbp.api.service.employee.context.EmployeeQueryContext;
import com.regent.rbp.api.service.user.UserService;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Service
public class EmployeeServiceBean implements EmployeeService {

    @Autowired
    private UserService userService;
    @Autowired
    ChannelDao channelDao;
    @Autowired
    SexDao sexDao;
    @Autowired
    PositionDao positionDao;
    @Autowired
    BaseDbService baseDbService;

    @Override
    public PageDataResponse<EmployeeQueryResult> query(EmployeeQueryParam param) {
        UserQueryParam userQueryParam = BeanUtil.copyProperties(param, UserQueryParam.class);
        PageDataResponse<UserQueryResult> query = userService.query(userQueryParam);
        List<EmployeeQueryResult> employeeQueryResults = converEmployeeQueryResult2(query.getData());
        return new PageDataResponse<>(query.getTotalCount(), employeeQueryResults);
    }

    private List<EmployeeQueryResult> converEmployeeQueryResult2(List<UserQueryResult> employeeList) {
        List<EmployeeQueryResult> collect = employeeList.stream().map(
                e -> {
                    EmployeeQueryResult cookItem = BeanUtil.copyProperties(e, EmployeeQueryResult.class);
                    cookItem.setSexName(e.getSex() == null ? null : e.getSex() == 0 ? "男" : "女");
                    return cookItem;
                }
        ).collect(Collectors.toList());
        return collect;
    }


    private QueryWrapper processQueryWrapper(EmployeeQueryContext context) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        if (context.getCode() != null && context.getCode().length > 0) {
            queryWrapper.in("code", context.getCode());
        }
        if (StringUtil.isNotEmpty(context.getName())) {
            queryWrapper.eq("name", context.getName());
        }
        if (context.getSex() != null && context.getSex().length > 0) {
            queryWrapper.in("sex_id", context.getSex());
        }
        if (StringUtil.isNotEmpty(context.getMobile())) {
            queryWrapper.eq("mobile", context.getMobile());
        }
        if (context.getChannelId() != null && context.getChannelId().length > 0) {
            queryWrapper.in("channel_id", context.getChannelId());
        }
        if (context.getEntryDate() != null) {
            queryWrapper.ge("entry_date", context.getEntryDate());
        }
        if (context.getLeaveDate() != null) {
            queryWrapper.ge("leave_date", context.getLeaveDate());
        }
        if (StringUtil.isNotEmpty(context.getJobNumber())) {
            queryWrapper.eq("job_number", context.getJobNumber());
        }
        if (context.getPositionId() != null && context.getPositionId().length > 0) {
            queryWrapper.in("position_id", context.getPositionId());
        }
        if (context.getWorkStatus() != null && context.getWorkStatus().length > 0) {
            queryWrapper.in("work_status", context.getWorkStatus());
        }
        if (context.getCreatedDateStart() != null) {
            queryWrapper.ge("created_time", context.getCreatedDateStart());
        }
        if (context.getCreatedDateEnd() != null) {
            queryWrapper.le("created_time", context.getCreatedDateEnd());
        }
        if (context.getUpdatedDateStart() != null) {
            queryWrapper.ge("updated_time", context.getUpdatedDateStart());
        }
        if (context.getUpdatedDateEnd() != null) {
            queryWrapper.le("updated_time", context.getUpdatedDateEnd());
        }
        if (context.getCheckDateStart() != null) {
            queryWrapper.ge("check_time", context.getCheckDateStart());
        }
        if (context.getCheckDateEnd() != null) {
            queryWrapper.le("check_time", context.getCheckDateEnd());
        }

        return queryWrapper;
    }

    private List<EmployeeQueryResult> converEmployeeQueryResult(List<Employee> employeeList) {
        List<EmployeeQueryResult> resultArrayList = new ArrayList<>(employeeList.size());
        ArrayList<Long> employeeIds = CollUtil.distinct(CollUtil.map(employeeList, Employee::getId, true));
        Map<Long, List<CustomizeDataDto>> customizeColumnMap = employeeIds.isEmpty() ? new HashMap<>() : baseDbService.getCustomizeColumnMap(TableConstants.EMPLOYEE, employeeIds);
        for (Employee employee : employeeList) {
            EmployeeQueryResult employeeQueryResult = new EmployeeQueryResult();
            employeeQueryResult.setCode(employee.getCode());
            employeeQueryResult.setName(employee.getName());
            Sex sex = sexDao.selectById(employee.getSexId());
            employeeQueryResult.setSexName(sex != null ? sex.getName() : null);
            employeeQueryResult.setMobile(employee.getMobile());
//            employeeQueryResult.setAddress(employee.getAddress());
            employeeQueryResult.setIdCard(employee.getIdCard());
//            employeeQueryResult.setBirthdayDate(DateUtil.getDateStr(employee.getBirthdayDate()));
//            employeeQueryResult.setEntryDate(DateUtil.getDateStr(employee.getEntryDate()));
//            employeeQueryResult.setLeaveDate(DateUtil.getDateStr(employee.getLeaveDate()));
//            employeeQueryResult.setJobNumber(employee.getJobNumber());
//            employeeQueryResult.setNotes(employee.getNotes());
            Position position = positionDao.selectById(employee.getPositionId());
            if (position != null) {
//                employeeQueryResult.setPositionCode(position.getCode());
//                employeeQueryResult.setPositionName(position.getName());
            }
            employeeQueryResult.setWorkStatus(employee.getWorkStatus());
            Channel channel = channelDao.selectById(employee.getChannelId());
            if (channel != null) {
                employeeQueryResult.setChannelCode(channel.getCode());
            }
            if (customizeColumnMap.containsKey(employee.getId())) {
//                employeeQueryResult.setCustomizeData(customizeColumnMap.get(employee.getId()));
            }
//            employeeQueryResult.setBirthdayDate(DateUtil.getDateStr(employee.getCreatedTime()));
//            employeeQueryResult.setUpdatedTime(DateUtil.getDateStr(employee.getUpdatedTime()));
//            employeeQueryResult.setCheckTime(DateUtil.getDateStr(employee.getCheckTime()));
            resultArrayList.add(employeeQueryResult);
        }
        return resultArrayList;
    }

    private void convertEmployeeQueryContext(EmployeeQueryParam param, EmployeeQueryContext context) {
//        context.setPageNo(param.getPageNo());
//        context.setPageSize(param.getPageSize());
//        context.setCode(param.getCode());
//        context.setName(param.getName());
//        // 性别
//        if (param.getSexName() != null && param.getSexName().length > 0) {
//            List<Sex> list = sexDao.selectList(new QueryWrapper<Sex>().in("name", param.getSexName()));
//            if (list != null && list.size() > 0) {
//                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
//                context.setSex(ids);
//            }
//        }
//        context.setMobile(param.getMobile());
//        // 渠道
//        if (param.getChannelCode() != null && param.getChannelCode().length > 0) {
//            List<Channel> list = channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getChannelCode()));
//            if (list != null && list.size() > 0) {
//                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
//                List<String> codeList = list.stream().filter(x -> StringUtil.isNotEmpty(x.getCode())).map(Channel::getCode).collect(Collectors.toList());
//                String[] codes = codeList.toArray(new String[codeList.size()]);
//                context.setChannelCode(codes);
//                context.setChannelId(ids);
//            }
//        }
//        context.setEntryDate(param.getEntryDate());
//        context.setJobNumber(param.getJobNumber());
//        //职位名称
//        if (StringUtil.isNotEmpty(param.getPositionName())) {
//            context.setPositionName(param.getPositionName());
//            List<Position> positions = positionDao.selectList(new LambdaQueryWrapper<Position>().in(Position::getName, param.getPositionName()));
//            long[] ids = positions.stream().mapToLong(map -> map.getId()).toArray();
//            context.setPositionId(ids);
//        }
//        context.setWorkStatus(param.getWorkStatus());
//        context.setFields(param.getFields());
//        if (StringUtil.isNotBlank(param.getCreatedDateStart())) {
//            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
//            context.setCreatedDateStart(createdDateStart);
//        }
//        if (StringUtil.isNotBlank(param.getCreatedDateEnd())) {
//            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
//            context.setCreatedDateEnd(createdDateEnd);
//        }
//        if (StringUtil.isNotBlank(param.getUpdatedDateStart())) {
//            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
//            context.setUpdatedDateStart(updatedDateStart);
//        }
//        if (StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
//            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
//            context.setUpdatedDateEnd(updatedDateEnd);
//        }
//        if (StringUtil.isNotBlank(param.getCheckDateStart())) {
//            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
//            context.setCheckDateStart(checkDateStart);
//        }
//        if (StringUtil.isNotBlank(param.getCheckDateEnd())) {
//            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
//            context.setCheckDateEnd(checkDateEnd);
//        }
    }

    @Override
    public DataResponse save(EmployeeSaveParam param) {
        UserSaveParam userParam = BeanUtil.copyProperties(param, UserSaveParam.class);
        Channel channel = channelDao.selectOne(Wrappers.lambdaQuery(Channel.class).eq(Channel::getCode, param.getChannelCode()));
        userParam.setBusinessManFlag(param.getBusinessManFlag() == 1);
        userParam.setChannelId(channel.getId());
        userService.save(userParam);
        return ModelDataResponse.Success(userParam.getId());
    }

}
