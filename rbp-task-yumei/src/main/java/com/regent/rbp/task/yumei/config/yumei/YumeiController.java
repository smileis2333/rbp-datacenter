package com.regent.rbp.task.yumei.config.yumei;

import com.regent.rbp.api.dto.core.ModelDataResponse;
import com.regent.rbp.task.yumei.config.yumei.api.SaleOrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangjie
 * @date : 2022/04/29
 * @description
 */
@RestController
@RequestMapping("yumei")
public class YumeiController {
    @Autowired
    YumeiTokenManager tokenManager;
    @Autowired
    SaleOrderResource saleOrderResource;

    @GetMapping("token/invalidate")
    public ModelDataResponse<String> invalidateToken(){
        tokenManager.invalidateToken();
        return ModelDataResponse.Success("ok");
    }

}
