package com.regent.rbp.task.yumei.config.yumei;


import cn.hutool.core.util.StrUtil;
import com.regent.rbp.api.dao.base.BaseDbDao;
import feign.Request;
import feign.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.HashMap;

@Setter
@Slf4j
public abstract class YumeiDecoder {
    private JavaMailSender mailSender;
    private String notifyMail;
    private String fromMail;
    private BaseDbDao baseDbDao;


    protected void send(String url, String request, String repsonse) {
        if (StrUtil.isNotBlank(notifyMail) && mailSender != null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(notifyMail);
            message.setSubject(String.format("玉美接口调用失败--%s", url));
            message.setText(String.format("%s\r\n%s", request, repsonse));
            try {
                mailSender.send(message);
                log.info(String.format("调用失败，邮件已发送, %s-%s-%s", url, request, repsonse));
            } catch (Exception e) {
                log.error(String.format("调用失败，邮件发送失败, %s-%s-%s,msg:%s", url, request, repsonse, e.getMessage()));
            }
        }
    }

    protected void log(Response response, String body) {
        Request request = response.request();
        Request.HttpMethod httpMethod = request.httpMethod();
        String provider = "yumei";
        Request.Body param = request.requestBody();
        String url = request.url();
        String startTime = request.headers().get(YumeiResouceClientConfiguration.LOG_REQUEST_TIME).stream().findFirst().orElse(null);
        String endTime = LocalDateTime.now().toString();
        HashMap<String, Object> item = new HashMap<>();
        item.put("method", httpMethod.name());
        item.put("provider", provider);
        if (httpMethod == Request.HttpMethod.POST) {
            String requestParam = new String(param.asBytes());
            send(url, requestParam, body);
            item.put("param", requestParam);
        }
        item.put("response", body);
        item.put("url", url);
        item.put("request_time", startTime);
        item.put("response_time", endTime);
        baseDbDao.insertMap("rbp_third_party_invoke_log", item);
    }
}
