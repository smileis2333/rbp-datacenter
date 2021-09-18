package com.regent.rbp.api.service.bean.employee;

import cn.hutool.core.collection.CollUtil;
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
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.employee.EmployeeQueryParam;
import com.regent.rbp.api.dto.employee.EmployeeQueryResult;
import com.regent.rbp.api.dto.employee.EmployeeSaveParam;
import com.regent.rbp.api.service.employee.EmployeeService;
import com.regent.rbp.api.service.employee.context.EmployeeQueryContext;
import com.regent.rbp.api.service.employee.context.EmployeeSaveContext;
import com.regent.rbp.infrastructure.util.DateUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 员工档案
 * @Author shaoqidong
 * @Date 2021/9/14
 **/
@Service
public class EmployeeServiceBean  implements EmployeeService {
    private EmployeeDao employeeDao;
    private ChannelDao channelDao;
    private SexDao sexDao;
    private PositionDao positionDao;

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
        QueryWrapper queryWrapper = new QueryWrapper<Employee>();

        IPage<Employee> employeePageData = employeeDao.selectPage(pageModel, queryWrapper);
        List<EmployeeQueryResult> list = converEmployeeQueryResult(employeePageData.getRecords());
        result.setTotalCount(employeePageData.getTotal());
        result.setData(list);
        return result;
    }

    private List<EmployeeQueryResult> converEmployeeQueryResult(List<Employee> employeeList) {
        List<EmployeeQueryResult> resultArrayList = new ArrayList<>(employeeList.size());
        for (Employee employee : employeeList) {
            EmployeeQueryResult employeeQueryResult = new EmployeeQueryResult();
            employeeQueryResult.setCode(employee.getCode());
            employeeQueryResult.setName(employee.getName());
            Sex sex = sexDao.selectById(employee.getSexId());
            employeeQueryResult.setSexName(sex != null ? sex.getName():null);
            employeeQueryResult.setMobile(employee.getMobile());
            employeeQueryResult.setAddress(employee.getAddress());
            employeeQueryResult.setIdCard(employee.getIdCard());
            employeeQueryResult.setBirthdayDate(DateUtil.getDateStr(employee.getBirthdayDate()));
            employeeQueryResult.setEntryDate(DateUtil.getDateStr(employee.getEntryDate()));
            employeeQueryResult.setLeaveDate(DateUtil.getDateStr(employee.getLeaveDate()));
            employeeQueryResult.setJobNumber(employee.getJobNumber());
            employeeQueryResult.setNotes(employee.getNotes());
            Position position = positionDao.selectById(employee.getPositionId());
            if(position!= null){
                employeeQueryResult.setPositionCode( position.getCode());
                employeeQueryResult.setPositionName( position.getName());
            }
            employeeQueryResult.setWorkStatus(employee.getWorkStatus());
            Channel channel = channelDao.selectById(employee.getChannelId());
            if(channel!= null){
                employeeQueryResult.setChannelCode(channel.getCode());
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
            List<Channel> list =channelDao.selectList(new QueryWrapper<Channel>().in("code", param.getChannelCode()));
            if (list != null && list.size() > 0) {
                long[] ids = list.stream().mapToLong(map -> map.getId()).toArray();
                context.setChannelCode(ids);
            }
        }
        context.setEntryDate(param.getEntryDate());
        context.setJobNumber(param.getJobNumber());
        context.setPositionName(param.getPositionName());
        context.setWorkStatus(param.getWorkStatus());
        context.setFields(param.getFields());
        if(StringUtil.isNotBlank(param.getCreatedDateStart())) {
            Date createdDateStart = DateUtil.getDate(param.getCreatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateStart(createdDateStart);
        }
        if(StringUtil.isNotBlank(param.getCreatedDateEnd())) {
            Date createdDateEnd = DateUtil.getDate(param.getCreatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setCreatedDateEnd(createdDateEnd);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateStart())) {
            Date updatedDateStart = DateUtil.getDate(param.getUpdatedDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateStart(updatedDateStart);
        }
        if(StringUtil.isNotBlank(param.getUpdatedDateEnd())) {
            Date updatedDateEnd = DateUtil.getDate(param.getUpdatedDateEnd(), DateUtil.FULL_DATE_FORMAT);
            context.setUpdatedDateEnd(updatedDateEnd);
        }
        if(StringUtil.isNotBlank(param.getCheckDateStart())) {
            Date checkDateStart = DateUtil.getDate(param.getCheckDateStart(), DateUtil.FULL_DATE_FORMAT);
            context.setCheckDateStart(checkDateStart);
        }
        if(StringUtil.isNotBlank(param.getCheckDateEnd())) {
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
        if(item != null) {
            context.getEmployee().setId(item.getId());
            createFlag = false;
        }
        //验证数据有效性
        List<String> errorMsgList = verificationProperty(param, context);
        if(errorMsgList.size() > 0 ) {
            String message = StringUtil.join(errorMsgList, ",");
            //throw new BusinessException();
        }
        saveEmployee(createFlag,context.getEmployee());
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

        if(StringUtil.isNotEmpty(param.getSexName())){
            List<Sex> sexList = sexDao.selectList(new LambdaQueryWrapper<Sex>().eq(Sex::getName,param.getSexName()));
            if(CollUtil.isEmpty(sexList)){
                errorMsgList.add("性别名称(sexName)错误");
            }else{
                context.getEmployee().setSexId(sexList.get(0).getId());
            }
        }
        if (StringUtils.isBlank(param.getChannelCode())) {
            errorMsgList.add("所属渠道编号(channelCode)不能为空");
        }else{
            List<Channel> channelList = channelDao.selectList(new LambdaQueryWrapper<Channel>().eq(Channel::getCode, param.getChannelCode()));
            if(CollUtil.isEmpty(channelList)){
                errorMsgList.add("所属渠道编号(channelCode)有误");
            }else{
                context.getEmployee().setChannelId(channelList.get(0).getId());
            }
        }
        return errorMsgList;
    }

    /**
     * 写入员工档案
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
