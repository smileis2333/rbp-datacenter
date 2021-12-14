package com.regent.rbp.api.web.config;

import com.alibaba.fastjson.JSON;
import com.regent.rbp.api.dto.base.TokenResultDto;
import com.regent.rbp.api.web.constants.DataCenterRedisPrefix;
import com.regent.rbp.common.constants.InformationConstants;
import com.regent.rbp.infrastructure.annotation.PassToken;
import com.regent.rbp.infrastructure.constants.ErrorMessage;
import com.regent.rbp.infrastructure.constants.ResourceConstant;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.RedisUtil;
import com.regent.rbp.infrastructure.util.ThreadLocalGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * API验签
 *
 * @author xuxing
 */
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token

        Object value = redisUtil.get(DataCenterRedisPrefix.DATACENTER_TOKEN_KEY + token);
        if (value == null) {
            throw new BusinessException(ResponseCode.TOKEN_EMPTY, ErrorMessage.CODE_MAP.get(ResponseCode.TOKEN_EMPTY));
        }
        TokenResultDto tokenResult = JSON.parseObject(value.toString(), TokenResultDto.class);
        if (!token.equalsIgnoreCase(tokenResult.getToken())) {
            throw new BusinessException(ResponseCode.TOKEN_ERROR, ErrorMessage.CODE_MAP.get(ResponseCode.TOKEN_ERROR));
        }
        //存储当前线程变量
        ThreadLocalGroup.setUserId(InformationConstants.AdminConstants.ADMIN_CODE);
        ThreadLocalGroup.set(ResourceConstant.LANG, LanguageUtil.ZH);
        ThreadLocalGroup.set("sessionId", httpServletRequest.getSession().getId());
        ThreadLocalGroup.set("token", token);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
