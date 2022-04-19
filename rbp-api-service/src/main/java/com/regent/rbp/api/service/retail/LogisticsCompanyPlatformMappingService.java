package com.regent.rbp.api.service.retail;

import com.baomidou.mybatisplus.extension.service.IService;
import com.regent.rbp.api.core.retail.LogisticsCompany;
import com.regent.rbp.api.core.retail.LogisticsCompanyPlatformMapping;

/**
 * @author liuzhicheng
 * @createTime 2022-04-12
 * @Description
 */
public interface LogisticsCompanyPlatformMappingService extends IService<LogisticsCompanyPlatformMapping> {

    /**
     * 根据平台类型编号获取系统对应的物流公司
     *
     * @param onlinePlatformLogisticsCode 平台物流编号
     * @param onlinePlatformTypeId 平台id
     * @return
     */
    LogisticsCompany getLogisticsCompanyCode(String onlinePlatformLogisticsCode, Integer onlinePlatformTypeId);

    /**
     * 根据物流编号获取平台物流编号
     *
     * @param logisticsCompanyCode 物流编号
     * @param onlinePlatformTypeId 平台id
     * @return
     */
    LogisticsCompanyPlatformMapping getOnlinePlatformLogisticsCode(String logisticsCompanyCode, Integer onlinePlatformTypeId);

    /**
     * 根据平台物流名称获取系统对应的物流公司
     *
     * @param onlinePlatformLogisticsName 平台物流名称
     * @param onlinePlatformTypeId 平台id
     * @return
     */
    LogisticsCompany getLogisticsCompanyCodeByName(String onlinePlatformLogisticsName, Integer onlinePlatformTypeId);
}
