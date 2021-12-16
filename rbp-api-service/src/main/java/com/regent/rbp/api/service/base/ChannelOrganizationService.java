package com.regent.rbp.api.service.base;

import com.regent.rbp.api.dto.base.ChannelOrganizationSaveParam;
import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;

/**
 * @author huangjie
 * @date 2021/12/16
 * @description 组织架构
 */
public interface ChannelOrganizationService {
    /**
     * 添加组织架构
     * @return
     */
    DataResponse create(ChannelOrganizationSaveParam param);

    /**
     * 查询
     * @return
     */
    PageDataResponse query();
}
