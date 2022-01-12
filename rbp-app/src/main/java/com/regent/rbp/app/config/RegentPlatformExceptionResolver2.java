package com.regent.rbp.app.config;

import cn.hutool.core.util.StrUtil;
import com.regent.rbp.infrastructure.constants.ErrorMessage;
import com.regent.rbp.infrastructure.constants.ResourceConstant;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.response.config.RegentPlatformExceptionResolver;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.infrastructure.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * copy from
 * com.regent.rbp.infrastructure.response.config.RegentPlatformExceptionResolver
 */
@Component
public class RegentPlatformExceptionResolver2 extends DefaultErrorAttributes implements HandlerExceptionResolver {

    private static Logger logger = LoggerFactory.getLogger(RegentPlatformExceptionResolver.class);

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        String language = request.getHeader(ResourceConstant.LANG);
        ModelAndView mav = new ModelAndView();
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        int code = 500;
        String message = ErrorMessage.CODE_MAP.get(ResponseCode.INTERNAL_ERROR);
        if (e.getClass() == BusinessException.class) {
            BusinessException businessException = (BusinessException) e;
            code = businessException.getCode();
            message = getExceptionMessage(businessException, language);
        } else if (e instanceof MethodArgumentNotValidException) {
            // 获取validation 验证信息,默认拼接参数字段名
            code = 400;
            StringBuilder sb = new StringBuilder();
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ex.getBindingResult();
            String spaceStr = LanguageUtil.EN.contains(language) ? StrUtil.SPACE : StrUtil.EMPTY;
            bindingResult.getFieldErrors().stream().forEach(v -> {
                sb.append(v.getField()).append(": ").append(spaceStr).append(v.getDefaultMessage()).append("; ");
            });
            message = sb.toString();
        }

        e.printStackTrace();
        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("code", code);
        attributes.put("msg", message);
        attributes.put("errorStack", e.toString());
        view.setAttributesMap(attributes);
        mav.setView(view);
        logger.info("@AfterException：线程id：" + Thread.currentThread().getId() + " 状态码：" + code
                + ".信息" + message + "   errorStack: " + e.toString());
        return mav;
    }

    /**
     * 参数字段国际化信息处理
     *
     * @param businessException
     * @return
     * @author wy
     */
    public String getExceptionMessage(BusinessException businessException, String language) {
        if (StringUtil.isEmpty(language)) { //没有带语言,按中文处理
            language = LanguageUtil.ZH;
        }
        String message = businessException.getMessage();
        String key = StringUtil.isEmpty(businessException.getLanguageKey()) ? businessException.getCode() + "" : businessException.getLanguageKey();
        try {
            ResourceBundle externalResourceBundle = LanguageUtil.getLanguageMap().get(language);
            if(externalResourceBundle != null){
                if (externalResourceBundle.containsKey(key)) {
                    message = externalResourceBundle.getString(StringUtil.isEmpty(businessException.getLanguageKey()) ? businessException.getCode() + "" : businessException.getLanguageKey());  //通过key获取对应的
                    message = LanguageUtil.convertGlobalKey(message,language);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (businessException.getParams() != null && StringUtil.isNotEmpty(message)) {
            return MessageFormat.format(message, businessException.getParams());
        }
        return message;
    }

    /**
     * 获取验证参数字段国际化信息
     *
     * @param fieldName
     * @param language
     * @return
     */
    private String getValidationFiledMessage(String fieldName, String language) {
        if (StringUtils.isEmpty(language)) {
            language = LanguageUtil.ZH;
        }
        if (!StringUtils.isEmpty(fieldName)) {
            int index = fieldName.lastIndexOf(".");
            fieldName = fieldName.substring(index + 1);
        }
        try {
            Locale locale = new Locale(language);
            ResourceBundle resourceBundle = ResourceBundle.getBundle(ResourceConstant.VALIDATION_LANGUAGE_FILE_PREFIX, locale); //读取配置文件
            fieldName = resourceBundle.getString(fieldName);  // 获取对应的国际化字段名
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return fieldName;
    }
}
