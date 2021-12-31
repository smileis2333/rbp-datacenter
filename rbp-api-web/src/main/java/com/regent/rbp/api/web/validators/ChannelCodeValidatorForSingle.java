package com.regent.rbp.api.web.validators;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class ChannelCodeValidatorForSingle implements ConstraintValidator<ChannelCodeCheck, String> {
    @Autowired
    private ChannelDao channelDao;

    @Override
    public boolean isValid(String channelCode, ConstraintValidatorContext context) {
        if (StrUtil.isNotEmpty(channelCode)) {
            Channel channel = channelDao.selectOne(new QueryWrapper<Channel>().eq("code", channelCode));
            return channel != null;
        }
        return true;
    }
}
