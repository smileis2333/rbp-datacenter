package com.regent.rbp.api.service.bean.employee;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regent.rbp.api.core.base.Position;
import com.regent.rbp.api.core.base.Sex;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.core.employee.Employee;
import com.regent.rbp.api.dao.base.PositionDao;
import com.regent.rbp.api.dao.base.SexDao;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dao.employee.EmployeeDao;
import com.regent.rbp.api.dto.base.CustomizeDataDto;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.employee.EmployeeQueryParam;
import com.regent.rbp.api.dto.employee.EmployeeQueryResult;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.api.service.base.BaseDbService;
import com.regent.rbp.api.service.constants.TableConstants;
import com.regent.rbp.api.service.employee.EmployeeService;
import com.regent.rbp.api.service.employee.context.EmployeeQueryContext;
import com.regent.rbp.api.service.employee.context.EmployeeSaveContext;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Service
public class EmployeeServiceBean implements EmployeeService {
    @Autowired
    EmployeeDao employeeDao;
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
        EmployeeQueryContext context = new EmployeeQueryContext();
        //将入参转换成查询的上下文对象
        convertEmployeeQueryContext(param, context);
        //查询数据
        PageDataResponse<EmployeeQueryResult> response = searchPage(context);
        return response;
    }

    private PageDataResponse<EmployeeQueryResult> searchPage(EmployeeQueryContext context) {
        PageDataResponse<EmployeeQueryResult> result = new PageDataResponse<>();

        Page<Employee> pageModel = new Page<>(context.getPageNo(), context.getPageSize());
        QueryWrapper queryWrapper = this.processQueryWrapper(context);

        IPage<Employee> employeePageData = employeeDao.selectPage(pageModel, queryWrapper);
        List<EmployeeQueryResult> list = converEmployeeQueryResult(employeePageData.getRecords());
        result.setTotalCount(employeePageData.getTotal());
        result.setData(list);
        return result;
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
            employeeQueryResult.setAddress(employee.getAddress());
            employeeQueryResult.setIdCard(employee.getIdCard());
            employeeQueryResult.setBirthdayDate(DateUtil.getDateStr(employee.getBirthdayDate()));
            employeeQueryResult.setEntryDate(DateUtil.getDateStr(employee.getEntryDate()));
            employeeQueryResult.setLeaveDate(DateUtil.getDateStr(employee.getLeaveDate()));
            employeeQueryResult.setJobNumber(employee.getJobNumber());
            employeeQueryResult.setNotes(employee.getNotes());
            Position position = positionDao.selectById(employee.getPositionId());
            if (position != null) {
                employeeQueryResult.setPositionCode(position.getCode());
                employeeQueryResult.setPositionName(position.getName());
            }
            employeeQueryResult.setWorkStatus(employee.getWorkStatus());
            Channel channel = channelDao.selectById(employee.getChannelId());
            if (channel != null) {
                employeeQueryResult.setChannelCode(channel.getCode());
            }
            if (customizeColumnMap.containsKey(employee.getId())){
                employeeQueryResult.setCustomizeData(customizeColumnMap.get(employee.getId()));
            }
            employeeQueryResult.setBirthdayDate(DateUtil.getDateStr(employee.getCreatedTime()));
            employeeQueryResult.setUpdatedTime(DateUtil.getDateStr(employee.getUpdatedTime()));
            employeeQueryResult.setCheckTime(DateUtil.getDateStr(employee.getCheckTime()));
            resultArrayList.add(employeeQueryResult);
        }
        return resultArrayList;
    }

    private void convertEmployeeQueryContext(EmployeeQueryParam param, EmployeeQueryContext context) {
        context.setPageNo(param.getPageNo());
        context.setPageSize(param.getPageSize());
        context.setCode(param.getCode());
        context.setName(param.getName());
        // 性别
        if (param.getSexName() != null && param.getSexName().length > 0) {
            List<Sex> list = sexDao.selectList(new QueryWrapper<Sex>().in("name", param.getSexName()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setSex(ids);
            }
        }
        context.setMobile(param.getMobile());
        // 渠道
        if (param.getChannelCode() != null && param.getChannelCode().length > 0) {
            List<Channel> list = channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getChannelCode()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                List<String> codeList = list.stream().filter(x -> StringUtil.isNotEmpty(x.getCode())).map(Channel::getCode).collect(Collectors.toList());
                String[] codes = codeList.toArray(new String[codeList.size()]);
                context.setChannelCode(codes);
                context.setChannelId(ids);
            }
        }
        context.setEntryDate(param.getEntryDate());
        context.setJobNumber(param.getJobNumber());
        //职位名称
        if (StringUtil.isNotEmpty(param.getPositionName())) {
            context.setPositionName(param.getPositionName());
            List<Position> positions = positionDao.selectList(new LambdaQueryWrapper<Position>().in(Position::getName, param.getPositionName()));
            long[] ids = positions.stream().mapToLong(map -> map.getId()).toArray();
            context.setPositionId(ids);
        }
        context.setWorkStatus(param.getWorkStatus());
        context.setFields(param.getFields());
        if (StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if (StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if (StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        if (StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if (StringUtil.isNotBlank(param.getCheckDateEnd())) {
            Date checkDateEnd = DateUtil.getDate(param.getCheckDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateEnd(checkDateEnd);
        }
    }

    @Override
    public DataResponse save(EmployeeSaveParam param) {
        boolean createFlag = true;
        EmployeeSaveContext context = new EmployeeSaveContext(param);
        //判断是新增还是更新
        Employee item = employeeDao.selectOne(new QueryWrapper<Employee>().eq("code", param.getCode()));
        if (item != null) {
            context.getEmployee().setId(item.getId());
            createFlag = false;
        }
        //验证数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if (errorMsgList.size() > 0) {
            String message = StringUtil.join(errorMsgList, ",");
            return DataResponse.errorParameter(message);
        }
        saveEmployee(createFlag, context.getEmployee());
        baseDbService.saveOrUpdateCustomFieldData(InformationConstants.ModuleConstants.EMPLOYEE_INFO, TableConstants.EMPLOYEE, context.getEmployee().getId(), param.getCustomizeData());
        return ModelDataResponse.Success(context.getEmployee().getId());
    }

    private List<String> verificationProperty(EmployeeSaveParam param, EmployeeSaveContext context) {
        List<String> errorMsgList = new ArrayList<>();
        if (StringUtils.isBlank(param.getCode())) {
            errorMsgList.add("员工编号(code)不能为空");
        }
        if (StringUtils.isBlank(param.getName())) {
            errorMsgList.add("姓名(name)不能为空");
        }

        if (StringUtil.isNotEmpty(param.getSexName())) {
            List<Sex> sexList = sexDao.selectList(new LambdaQueryWrapper<Sex>().eq(Sex::getName, param.getSexName()));
            if (CollUtil.isEmpty(sexList)) {
                errorMsgList.add("性别名称(sexName)错误");
            } else {
                context.getEmployee().setSexId(sexList.get(0).getId());
            }
        }
        if (StringUtils.isBlank(param.getChannelCode())) {
            errorMsgList.add("所属渠道编号(channelCode)不能为空");
        } else {
            List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
            if (CollUtil.isEmpty(channelList)) {
                errorMsgList.add("所属渠道编号(channelCode)有误");
            } else {
                context.getEmployee().setChannelId(channelList.get(0).getId());
            }
        }

        if (StrUtil.isNotEmpty(param.getPositionName())){
            Position position = positionDao.selectOne(new QueryWrapper<Position>().eq("name", param.getPositionName()));
            if (position == null) {
                errorMsgList.add("职位(positionName)不存在");
            }
            context.getEmployee().setPositionId(position.getId());
        }
        return errorMsgList;
    }

    /**
     * 写入员工档案
     *
     * @param createFlag
     * @param employee
     */
    private void saveEmployee(Boolean createFlag, Employee employee) {
        if (createFlag) {
            employeeDao.insert(employee);
        } else {
            employeeDao.updateById(employee);
        }
    }
}
