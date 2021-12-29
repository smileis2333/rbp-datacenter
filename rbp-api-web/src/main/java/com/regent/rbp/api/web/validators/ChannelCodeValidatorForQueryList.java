package com.regent.rbp.api.web.validators;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.regent.rbp.api.core.channel.Channel;
import com.regent.rbp.api.dao.channel.ChannelDao;
import com.regent.rbp.api.dto.validate.ChannelCodeCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author huangjie
 * @date : 2021/12/22
 * @description
 */
@Component
public class ChannelCodeValidatorForQueryList implements ConstraintValidator<ChannelCodeCheck, List<String>> {
    @Autowired
    private ChannelDao channelDao;

    @Override
    public boolean isValid(List<String> channelCodes, ConstraintValidatorContext context) {
        if (CollUtil.isEmpty(channelCodes)){
            return true;
        }
        List<Channel> cscs = channelDao.selectList(new QueryWrapper<Channel>().in("code", channelCodes));
        if (CollUtil.isNotEmpty(cscs)){
            return true;
        }
        return false;
    }
}
