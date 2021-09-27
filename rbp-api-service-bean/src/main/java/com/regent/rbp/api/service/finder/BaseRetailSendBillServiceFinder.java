package com.regent.rbp.api.service.finder;

import cn.hutool.core.util.ReflectUtil;
import com.regent.rbp.api.service.retail.BaseRetailSendBillService;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基础全渠道发货单事件实现类
 *
 * @author chenchungui
 * @date 2021-09-26
 */
@Component
public class BaseRetailSendBillServiceFinder implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * PLATFORM_EVENT_KEY，值为对应导入服务实现类
     */
    private static final Map<String, BaseRetailSendBillService> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * 获取具体实现类
     *
     * @param key
     * @return
     */
    public static BaseRetailSendBillService findServiceImpl(String key) {
        BaseRetailSendBillService excelService = SERVICE_MAP.get(key);
        if (null == excelService) {
            throw new BusinessException(ResponseCode.INTERNAL_ERROR, "baseRetailSendServiceNotExist", new Object[]{key});
        }
        return excelService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, BaseRetailSendBillService> serviceMap = applicationContext.getBeansOfType(BaseRetailSendBillService.class);
        for (BaseRetailSendBillService service : serviceMap.values()) {
            //唯一标识
            String key = (String) ReflectUtil.getFieldValue(service, "SEND_BILL_KEY");
            if (StringUtils.isNotBlank(key)) {
                SERVICE_MAP.put(key, service);
            }
        }
    }
}
