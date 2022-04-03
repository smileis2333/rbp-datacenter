package com.regent.rbp.task.yumei.service.impl;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regent.rbp.infrastructure.constants.ResponseCode;
import com.regent.rbp.infrastructure.exception.BusinessException;
import com.regent.rbp.infrastructure.util.LanguageUtil;
import com.regent.rbp.task.yumei.constants.YumeiApiUrl;
import com.regent.rbp.task.yumei.model.YumeiCredential;
import com.regent.rbp.task.yumei.model.YumeiOrder;
import com.regent.rbp.task.yumei.service.SaleOrderService;
import lombok.Data;
import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huangjie
 * @date : 2022/04/03
 * @description
 */
@Data
@Service
public class SaleOrderServiceImpl implements SaleOrderService {
    @Autowired
    private YumeiCredential credential;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void confirmReceive(String storeNo, String orderSource, String outOrderNo) {

    }

    @Override
    public void pushOrder(String storeNo, String orderSource, List<YumeiOrder> orders) {
        HashMap<String, Object> body = new HashMap<>();
        try {
            body.put("storeNo", storeNo);
            body.put("orderSource", orderSource);
            body.put("orders", orders);
            String jsonBody = objectMapper.writeValueAsString(body);
            String returnJson = HttpUtil.createRequest(Method.POST, YumeiApiUrl.SALE_ORDER_PUSH)
                    .body(jsonBody)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .header("X-AUTH-TOKEN", credential.getAccessToken())
                    .execute()
                    .body();
            Map<String, Object> returnData = (Map<String, Object>) objectMapper.readValue(returnJson, Map.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "paramError");
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARAMS_ERROR, "returnDataError");
        }
    }
}
