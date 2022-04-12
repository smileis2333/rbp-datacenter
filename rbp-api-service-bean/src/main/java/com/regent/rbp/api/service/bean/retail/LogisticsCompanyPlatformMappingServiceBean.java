package com.regent.rbp.api.service.bean.retail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netflix.discovery.converters.Auto;
import com.regent.rbp.api.core.retail.LogisticsCompany;
import com.regent.rbp.api.core.retail.LogisticsCompanyPlatformMapping;
import com.regent.rbp.api.dao.retail.LogisticsCompanyDao;
import com.regent.rbp.api.dao.retail.LogisticsCompanyPlatformMappingDao;
import com.regent.rbp.api.service.retail.LogisticsCompanyPlatformMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuzhicheng
 * @createTime 2022-04-12
 * @Description
 */
@Service
public class LogisticsCompanyPlatformMappingServiceBean extends ServiceImpl<LogisticsCompanyPlatformMappingDao, LogisticsCompanyPlatformMapping> implements LogisticsCompanyPlatformMappingService {

    @Autowired
    private LogisticsCompanyPlatformMappingDao logisticsCompanyPlatformMappingDao;

    @Autowired
    private LogisticsCompanyDao logisticsCompanyDao;

    @Override
    public LogisticsCompany getLogisticsCompanyCode(String onlinePlatformLogisticsCode, Integer onlinePlatformTypeId) {
        LogisticsCompany logisticsCompany = null;
        if (StrUtil.isEmpty(onlinePlatformLogisticsCode) || ObjectUtil.isEmpty(onlinePlatformTypeId)) {
            return logisticsCompany;
        }

        List<LogisticsCompanyPlatformMapping> logisticsCompanyPlatformMappingList = logisticsCompanyPlatformMappingDao
                .selectList(new LambdaQueryWrapper<LogisticsCompanyPlatformMapping>()
                .eq(LogisticsCompanyPlatformMapping::getOnlinePlatformLogisticsCode, onlinePlatformLogisticsCode)
                .eq(LogisticsCompanyPlatformMapping::getOnlinePlatformTypeId, onlinePlatformTypeId));
        if (CollUtil.isNotEmpty(logisticsCompanyPlatformMappingList)) {
            logisticsCompany = logisticsCompanyDao.selectById(logisticsCompanyPlatformMappingList.get(0).getLogisticsCompanyId());
        }
        return logisticsCompany;
    }

    @Override
    public LogisticsCompanyPlatformMapping getOnlinePlatformLogisticsCode(String logisticsCompanyCode, Integer onlinePlatformTypeId) {
        LogisticsCompanyPlatformMapping logisticsCompanyPlatformMapping = null;
        if (StrUtil.isEmpty(logisticsCompanyCode) || ObjectUtil.isEmpty(onlinePlatformTypeId)) {
            return logisticsCompanyPlatformMapping;
        }

        LogisticsCompany logisticsCompany = logisticsCompanyDao.selectOne(new LambdaQueryWrapper<LogisticsCompany>()
                .eq(LogisticsCompany::getCode, logisticsCompanyCode));
        if (null != logisticsCompany) {
            logisticsCompanyPlatformMapping = logisticsCompanyPlatformMappingDao.selectOne(new LambdaQueryWrapper<LogisticsCompanyPlatformMapping>()
                    .eq(LogisticsCompanyPlatformMapping::getLogisticsCompanyId, logisticsCompany.getId())
                    .eq(LogisticsCompanyPlatformMapping::getOnlinePlatformTypeId, onlinePlatformTypeId));
        }

        return logisticsCompanyPlatformMapping;
    }
}
