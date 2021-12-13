package com.regent.rbp.api.service.user;

import com.regent.rbp.api.dto.core.DataResponse;
import com.regent.rbp.api.dto.core.PageDataResponse;
import com.regent.rbp.api.dto.user.UserQueryParam;
import com.regent.rbp.api.dto.user.UserQueryResult;
import com.regent.rbp.api.dto.user.UserSaveParam;

/**
 * @author chenchungui
 * @date 2021/12/10
 * @description 用户档案接口
 */
public interface UserService {

    /**
     * 查询
     *
     * @param param
     * @return
     */
    PageDataResponse<UserQueryResult> query(UserQueryParam param);

    /**
     * 新增
     *
     * @param param
     * @return
     */
    DataResponse save(UserSaveParam param);

}
