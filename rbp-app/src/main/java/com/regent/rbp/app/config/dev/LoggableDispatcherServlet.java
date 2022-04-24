package com.regent.rbp.app.config.dev;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)//最高优先级 方便拦截404什么的
public class LoggableDispatcherServlet extends DispatcherServlet {

    private static final Logger logger = LoggerFactory.getLogger("HttpLogger");

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final long serialVersionUID = -2151909516770706554L;

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if ("GET".equals(request.getMethod())) {
//            super.doDispatch(request, response);
//            return;
//        }
        List<String> curlItemList = new ArrayList<>();
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        setThrowExceptionIfNoHandlerFound(true);
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode reqNode = mapper.createObjectNode();
        ObjectNode resNode = mapper.createObjectNode();
        String method = request.getMethod();
        curlItemList.add(MessageFormat.format("-X ''{0}''", method));
        rootNode.put("method", method);
        String requestUrl = request.getRequestURL().toString();
        rootNode.put("url", requestUrl);
        rootNode.put("remoteAddr", request.getRemoteAddr());
        rootNode.put("x-forwarded-for", request.getHeader("x-forwarded-for"));
        rootNode.set("request", reqNode);
        rootNode.set("response", resNode);
        reqNode.set("headers", mapper.valueToTree(getRequestHeaders(request)));

        try {
            reqNode.set("query", mapper.valueToTree(request.getParameterMap()));
            if ("GET".equals(method)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("'").append(requestUrl);
                Enumeration<String> parameterNames = request.getParameterNames();
                if (parameterNames.hasMoreElements()) {
                    stringBuilder.append("?");
                }
                while (parameterNames.hasMoreElements()) {
                    String name = parameterNames.nextElement();
                    String[] values = request.getParameterValues(name);
                    for (String value : values) {
                        stringBuilder.append(name).append("=").append(value).append("&");
                    }
                }
                stringBuilder.append("'");
                curlItemList.add(stringBuilder.toString());
                super.doDispatch(request, responseWrapper);
            } else {
                curlItemList.add("'" + requestUrl + "'");
                if (isFormPost(request)) {
                    ContentCachingRequestWrapper bufferedServletRequestWrapper = new ContentCachingRequestWrapper(request);
                    reqNode.set("body", mapper.valueToTree(request.getParameterMap()));
                    reqNode.put("bodyIsJson", false);
                    StringBuilder stringBuilder = new StringBuilder();
                    Enumeration<String> parameterNames = request.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String name = parameterNames.nextElement();
                        String[] values = request.getParameterValues(name);
                        for (String value : values) {
                            stringBuilder.append(name).append("=").append(value).append("&");
                        }
                    }
                    curlItemList.add(MessageFormat.format("--data-raw ''{0}''", stringBuilder.toString()));
                    super.doDispatch(bufferedServletRequestWrapper, responseWrapper);
                } else if (isJsonPost(request)) {
                    BufferedServletRequestWrapper bufferedServletRequestWrapper = new BufferedServletRequestWrapper(request);
                    ServletInputStream inputStream = bufferedServletRequestWrapper.getInputStream();
                    byte[] contentAsByteArray = IOUtils.toByteArray(inputStream);
                    reqNode.set("body", mapper.readTree(contentAsByteArray));
                    reqNode.put("bodyIsJson", true);
                    curlItemList.add(MessageFormat.format("--data-binary ''{0}''", mapper.readTree(contentAsByteArray)));
                    super.doDispatch(bufferedServletRequestWrapper, responseWrapper);
                } else if (isTextPost(request) || isXmlPost(request)) {
                    BufferedServletRequestWrapper bufferedServletRequestWrapper = new BufferedServletRequestWrapper(request);
                    ServletInputStream inputStream = bufferedServletRequestWrapper.getInputStream();
                    byte[] contentAsByteArray = IOUtils.toByteArray(inputStream);
                    reqNode.put("body", new String(contentAsByteArray));
                    reqNode.put("bodyIsJson", false);
                    curlItemList.add(MessageFormat.format("--data-binary ''{0}''", new String(contentAsByteArray)));
                    super.doDispatch(bufferedServletRequestWrapper, responseWrapper);
                } else if (isMediaPost(request)) {
                    reqNode.put("body", "Media Request Body ContentLength = " + request.getContentLengthLong());
                    reqNode.put("bodyIsJson", false);
                    super.doDispatch(request, responseWrapper);
                } else {
                    reqNode.put("body", "Unknown Request Body ContentLength = " + request.getContentLengthLong());
                    reqNode.put("bodyIsJson", false);
                    super.doDispatch(request, responseWrapper);
                }
            }
            HandlerExecutionChain handlerExecutionChain = getHandler(request);
            if (handlerExecutionChain == null) {
                //手动判断是不是404 不走系统流程 直接处理 因为会重定向/error
                resNode.put("status", HttpStatus.NOT_FOUND.value());
                logger.info(rootNode.toString());
                response.setStatus(HttpStatus.NOT_FOUND.value());
                PrintWriter writer = response.getWriter();
                writer.write("Request path not found");
                writer.flush();
                writer.close();
                return;
            }
            System.out.println(handlerExecutionChain);
        } finally {
            byte[] responseWrapperContentAsByteArray = responseWrapper.getContentAsByteArray();
            responseWrapper.copyBodyToResponse();//这里有顺序 必须先读body 然后再调用这个方法 才能继续读
            resNode.put("status", response.getStatus());
            Map<String, Object> responseHeaders = getResponseHeaders(response);

            //这里判断错误拦截是不是吧url改成error了 如果是就做一下替换 替换的值是错误拦截器写到header里面的
            String url = rootNode.get("url").asText();
            if (url.endsWith("/error")) {
                String path = (String) responseHeaders.get("x-error-path");
                if (!ObjectUtils.isEmpty(path)) {
                    rootNode.put("url", url.replace("/error", path));
                }
            }
            resNode.set("headers", mapper.valueToTree(responseHeaders));
            if (isProtoBufPost(responseWrapper) || "GET".equals(request.getMethod())) {
            } else {
                try {
                    resNode.set("body", mapper.readTree(responseWrapperContentAsByteArray));
                    resNode.put("bodyIsJson", true);
                } catch (Exception e) {
                    resNode.put("body", new String(responseWrapperContentAsByteArray));
                    resNode.put("bodyIsJson", false);
                }
            }
            logger.info(rootNode.toString());
            getCurlRequestHeaders(request, curlItemList);
            logger.info("curl " + CollectionUtil.join(curlItemList, "\n   "));
        }
    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private void getCurlRequestHeaders(HttpServletRequest request, List<String> list) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            if (!headerValue.contains("multipart/form-data")) {
                list.add(MessageFormat.format("-H ''{0}: {1}''", headerName, headerValue));
            }
        }
    }

    private Map<String, Object> getResponseHeaders(HttpServletResponse response) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }

    private boolean isFormPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        return (contentType != null && contentType.contains("x-www-form"));
    }

    private boolean isMediaPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("stream") || contentType.contains("image") || contentType.contains("video") || contentType.contains("audio");
        }
        return false;
    }

    private boolean isTextPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("text/plain") || contentType.contains("text/xml") || contentType.contains("text/html");
        }
        return false;
    }

    private boolean isJsonPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("application/json");
        }
        return false;
    }

    private boolean isXmlPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("application/xml");
        }
        return false;
    }

    private boolean isProtoBufPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("application") && contentType.contains("protobuf");
        }
        return false;
    }

    private boolean isProtoBufPost(HttpServletResponse response) {
        String contentType = response.getContentType();
        if (contentType != null) {
            return contentType.contains("application") && contentType.contains("protobuf");
        }
        return false;
    }

    private boolean isMultipartFormDataPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null) {
            return contentType.contains("multipart/form-data");
        }
        return false;
    }

    class BufferedServletInputStream extends ServletInputStream {
        private ByteArrayInputStream inputStream;
        private ServletInputStream is;

        public BufferedServletInputStream(byte[] buffer, ServletInputStream is) {
            this.is = is;
            this.inputStream = new ByteArrayInputStream(buffer);
        }

        @Override
        public int available() {
            return inputStream.available();
        }

        @Override
        public int read() {
            return inputStream.read();
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return is.isFinished();
        }

        @Override
        public boolean isReady() {
            return is.isReady();
        }

        @Override
        public void setReadListener(ReadListener listener) {
            is.setReadListener(listener);
        }
    }

    class BufferedServletRequestWrapper extends HttpServletRequestWrapper {
        private byte[] buffer;
        private ServletInputStream is;

        public BufferedServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.is = request.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(IOUtils.toByteArray(is));
            this.buffer = byteArrayOutputStream.toByteArray();
        }

        @Override
        public ServletInputStream getInputStream() {
            return new BufferedServletInputStream(this.buffer, this.is);
        }
    }
}