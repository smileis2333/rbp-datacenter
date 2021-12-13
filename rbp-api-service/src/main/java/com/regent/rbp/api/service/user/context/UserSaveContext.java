package com.regent.rbp.api.service.user.context;

import com.regent.rbp.api.core.user.UserProfile;
import com.regent.rbp.api.core.user.UserCashierChannel;
import com.regent.rbp.api.core.user.UserCashierLowerDiscount;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 指令订单 保存上下文对象
 *
 * @author chenchungui
 * @date 2021-12-07
 */
@Data
public class UserSaveContext {

    @ApiModelProperty(notes = "用户档案")
    private UserProfile user;

    @ApiModelProperty(notes = "收银员渠道关系")
    private List<UserCashierChannel> channelList;

    @ApiModelProperty(notes = "收银员分类最低折扣")
    private List<UserCashierLowerDiscount> lowerDiscountList;

}
