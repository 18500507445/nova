package com.nova.limit.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Description: 请求拦截器
 * @Author: wangzehui
 * @Date: 2022/8/30 15:29
 */
@ControllerAdvice
public class RequestAdvice implements RequestBodyAdvice {

    /**
     * 该方法用于判断当前请求，是否要执行beforeBodyRead方法
     * methodParameter方法的参数对象
     * type方法的参数类型
     * aClass 将会使用到的Http消息转换器类类型
     * 注意：此判断方法，会在beforeBodyRead 和 afterBodyRead方法前都触发一次。
     *
     * @return 返回true则会执行beforeBodyRead
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    /**
     * 在Http消息转换器执转换，之前执行
     * inputMessage 客户端的请求数据
     * parameter方法的参数对象
     * targetType方法的参数类型
     * converterType 将会使用到的Http消息转换器类类型
     *
     * @return 返回 一个自定义的HttpInputMessage
     * @RequestBody的请求才生效
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return null;
    }

    /**
     * 在Http消息转换器执转换，之后执行
     * body 转换后的对象
     * inputMessage 客户端的请求数据
     * parameter handler方法的参数类型
     * targetType handler方法的参数类型
     * converterType 使用的Http消息转换器类类型
     *
     * @return 返回一个新的对象
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }

    /**
     * 参数与afterBodyRead相同，不过这个方法处理的是，body为空的情况
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return null;
    }
}
