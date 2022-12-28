package com.nova.limit.interceptor;

import com.nova.common.constant.Constants;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @description: 响应拦截器 (@ResponseBody的返回生效)
 * @author: wangzehui
 * @date: 2022/8/30 15:30
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        System.out.println("ResponseAdvice-supports:" + Constants.IS_OPEN);
        return Constants.IS_OPEN;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("beforeBodyWrite");
        return body;
    }
}
